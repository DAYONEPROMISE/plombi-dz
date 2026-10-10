package dz.plombidz.app.sizing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SizingEngineTest {

    @Test
    fun simultaneity_noReductionUpToFive() {
        assertEquals(1.0, SizingEngine.simultaneity(1), 1e-9)
        assertEquals(1.0, SizingEngine.simultaneity(5), 1e-9)
    }

    @Test
    fun simultaneity_formulaBeyondFive() {
        assertEquals(0.8 / Math.sqrt(5.0), SizingEngine.simultaneity(6), 1e-9)
        assertEquals(0.8 / 3.0, SizingEngine.simultaneity(10), 1e-9)
        assertEquals(0.8 / Math.sqrt(127.0), SizingEngine.simultaneity(128), 1e-9)
    }

    @Test
    fun singleBasinPer_picks20mm() {
        val r = SizingEngine.compute(mapOf(Fixture.BASIN to 1), Material.PER, 8.0)
        assertEquals(1, r.fixtureCount)
        assertEquals(0.20, r.rawFlowLs, 1e-9)
        assertEquals(1.0, r.simultaneity, 1e-9)
        assertEquals(0.20, r.probableFlowLs, 1e-9)
        // D = 1000*sqrt(4*0.0002/(pi*1.5)) ~= 13.03 mm -> PER 20 (16.2 bore)
        assertEquals(20, r.outerMm)
        assertEquals(16.2, r.innerMm, 1e-9)
        assertFalse(r.upsizedForLength)
        assertFalse(r.overVelocity)
        assertTrue(r.velocityMs < 1.5)
    }

    @Test
    fun singleBasinMultilayer_picks20mm() {
        val r = SizingEngine.compute(mapOf(Fixture.BASIN to 1), Material.MULTILAYER, 5.0)
        assertEquals(20, r.outerMm)
        assertEquals(16.0, r.innerMm, 1e-9)
    }

    @Test
    fun singleWcCopper_picks14mm() {
        val r = SizingEngine.compute(mapOf(Fixture.WC to 1), Material.COPPER, 4.0)
        assertEquals(0.12, r.probableFlowLs, 1e-9)
        assertEquals(14, r.outerMm)
    }

    @Test
    fun tenMixedMultilayer_picks32mm() {
        val counts = mapOf(Fixture.BASIN to 5, Fixture.SHOWER to 5)
        val r = SizingEngine.compute(counts, Material.MULTILAYER, 8.0)
        assertEquals(10, r.fixtureCount)
        assertEquals(2.0, r.rawFlowLs, 1e-9)
        assertEquals(0.8 / 3.0, r.simultaneity, 1e-9)
        assertEquals(32, r.outerMm)
        assertTrue(r.velocityMs in 0.5..1.5)
    }

    @Test
    fun longRun_upsizesOneStep() {
        val short = SizingEngine.compute(mapOf(Fixture.BASIN to 1), Material.PER, 8.0)
        val long = SizingEngine.compute(mapOf(Fixture.BASIN to 1), Material.PER, 20.0)
        assertEquals(20, short.outerMm)
        assertEquals(25, long.outerMm)
        assertTrue(long.upsizedForLength)
    }

    @Test
    fun emptyCounts_returnsZeroedResult() {
        val r = SizingEngine.compute(emptyMap(), Material.PER, 5.0)
        assertEquals(0, r.fixtureCount)
        assertEquals(0.0, r.probableFlowLs, 1e-9)
    }

    @Test
    fun velocity_directComputation() {
        // 1 L/s through 10 mm bore ~= 12.73 m/s
        assertEquals(12.73, SizingEngine.velocityMs(1.0, 10.0), 0.01)
        assertEquals(0.0, SizingEngine.theoreticalBoreMm(0.0), 1e-9)
    }

    @Test
    fun minBore_canDominateChoice() {
        // Single bath: Qp=0.33 -> theo ~= 16.7mm but min bore 13; PER 25 covers.
        val r = SizingEngine.compute(mapOf(Fixture.BATH to 1), Material.PER, 5.0)
        assertTrue(r.requiredBoreMm >= 13.0)
        assertEquals(25, r.outerMm)
    }
}
