package dz.plombidz.app.sizing

/**
 * Fixture catalogue. Base flows (L/s, cold or mixed water) and minimum supply
 * bores (mm) follow NF DTU 60.11 Table 1: evier 0.20/12, lavabo 0.20/10,
 * douche 0.20/12, baignoire 0.33/13, WC reservoir 0.12/10,
 * poste 1/2 0.33/12, poste 3/4 0.42/13. Washing machine 0.20 L/s per the
 * same table; its 10 mm minimum bore is an app default.
 */
enum class Fixture(val baseFlowLs: Double, val minBoreMm: Double) {
    SINK(0.20, 12.0),
    BASIN(0.20, 10.0),
    SHOWER(0.20, 12.0),
    BATH(0.33, 13.0),
    WC(0.12, 10.0),
    WASHER(0.20, 10.0),
    TAP12(0.33, 12.0),
    TAP34(0.42, 13.0);
}

/** Tube material with (outer mm -> inner mm) standard sizes. */
enum class Material(val sizes: List<Pair<Int, Double>>) {
    PER(
        listOf(
            12 to 9.8,   // 12x1.1
            16 to 13.0,  // 16x1.5
            20 to 16.2,  // 20x1.9
            25 to 20.4,  // 25x2.3
        )
    ),
    MULTILAYER(
        listOf(
            16 to 12.0,  // 16x2
            20 to 16.0,  // 20x2
            26 to 20.0,  // 26x3
            32 to 26.0,  // 32x3
        )
    ),
    COPPER(
        listOf(
            10 to 8.0,
            12 to 10.0,
            14 to 12.0,
            16 to 14.0,
            18 to 16.0,
            22 to 20.0,
            28 to 26.0,
        )
    );
}
