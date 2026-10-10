package dz.plombidz.app.sizing

import kotlin.math.PI
import kotlin.math.sqrt

/** Result of a pipe-sizing guidance computation. */
data class SizingResult(
    val fixtureCount: Int,
    val rawFlowLs: Double,
    val simultaneity: Double,
    val probableFlowLs: Double,
    val theoreticalBoreMm: Double,
    val requiredBoreMm: Double,
    val outerMm: Int,
    val innerMm: Double,
    val velocityMs: Double,
    val upsizedForLength: Boolean,
    val overVelocity: Boolean,
)

/**
 * Guidance engine based on NF DTU 60.11.
 *
 * - Probable flow Qp = y * sum(Qbase), with y = 0.8 / sqrt(x - 1) for x > 5
 *   (collective simultaneity); y = 1 (no reduction) for x <= 5.
 * - Theoretical bore from the velocity criterion D = sqrt(4.Q / (pi.V))
 *   with V = 1.5 m/s (column reference).
 * - The chosen tube is the smallest standard size whose interior covers both
 *   the theoretical bore and every fixture's minimum bore. Runs longer than
 *   12 m are upsized one step as a documented rule of thumb to bound
 *   head loss; a flag reports it. Velocity above 2.0 m/s raises a flag.
 */
object SizingEngine {

    const val TARGET_VELOCITY = 1.5
    const val MAX_VELOCITY = 2.0
    const val LONG_RUN_M = 12.0

    fun simultaneity(count: Int): Double {
        if (count <= 5) return 1.0
        return 0.8 / sqrt((count - 1).toDouble())
    }

    fun theoreticalBoreMm(flowLs: Double, velocityMs: Double = TARGET_VELOCITY): Double {
        if (flowLs <= 0) return 0.0
        val q = flowLs / 1000.0 // m3/s
        return 1000.0 * sqrt(4.0 * q / (PI * velocityMs))
    }

    fun velocityMs(flowLs: Double, innerMm: Double): Double {
        if (innerMm <= 0) return 0.0
        val q = flowLs / 1000.0
        val d = innerMm / 1000.0
        return q / (PI * d * d / 4.0)
    }

    fun compute(
        counts: Map<Fixture, Int>,
        material: Material,
        lengthM: Double,
    ): SizingResult {
        val clean = counts.mapValues { (_, v) -> v.coerceAtLeast(0) }
        val n = clean.values.sum()
        val raw = clean.entries.sumOf { (f, c) -> f.baseFlowLs * c }
        val y = simultaneity(n)
        val qp = raw * y
        val theo = theoreticalBoreMm(qp)
        val minBore = clean.entries
            .filter { it.value > 0 }
            .maxOfOrNull { it.key.minBoreMm } ?: 0.0
        val required = maxOf(theo, minBore)

        val sizes = material.sizes
        var idx = sizes.indexOfFirst { it.second >= required }.let { if (it < 0) sizes.lastIndex else it }
        var upsized = false
        if (lengthM > LONG_RUN_M && idx < sizes.lastIndex) {
            idx += 1
            upsized = true
        }
        val (outer, inner) = sizes[idx]
        val v = velocityMs(qp, inner)
        return SizingResult(
            fixtureCount = n,
            rawFlowLs = raw,
            simultaneity = y,
            probableFlowLs = qp,
            theoreticalBoreMm = theo,
            requiredBoreMm = required,
            outerMm = outer,
            innerMm = inner,
            velocityMs = v,
            upsizedForLength = upsized,
            overVelocity = v > MAX_VELOCITY,
        )
    }
}
