package dz.plombidz.app.sizing

/** Tank and pressure helpers (pure, unit-tested). */
object TankPressure {

    /** Useful tank volume in litres. */
    fun tankVolumeLitres(persons: Int, days: Int, litresPerPersonDay: Int): Long {
        if (persons <= 0 || days <= 0 || litresPerPersonDay <= 0) return 0L
        return persons.toLong() * days.toLong() * litresPerPersonDay.toLong()
    }

    /**
     * Static pressure in bar from a water column height in metres.
     * p = rho.g.h = 1000 * 9.81 * h Pa, i.e. h * 0.0981 bar.
     */
    fun staticPressureBar(heightM: Double): Double {
        if (heightM <= 0) return 0.0
        return heightM * 0.0981
    }
}
