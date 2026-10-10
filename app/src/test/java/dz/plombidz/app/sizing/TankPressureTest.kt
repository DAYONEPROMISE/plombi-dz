package dz.plombidz.app.sizing

import org.junit.Assert.assertEquals
import org.junit.Test

class TankPressureTest {

    @Test
    fun tankVolume_familyCase() {
        assertEquals(1500L, TankPressure.tankVolumeLitres(5, 2, 150))
    }

    @Test
    fun tankVolume_singlePerson() {
        assertEquals(200L, TankPressure.tankVolumeLitres(1, 1, 200))
    }

    @Test
    fun tankVolume_invalidInputsGiveZero() {
        assertEquals(0L, TankPressure.tankVolumeLitres(0, 2, 150))
        assertEquals(0L, TankPressure.tankVolumeLitres(5, 0, 150))
        assertEquals(0L, TankPressure.tankVolumeLitres(5, 2, 0))
    }

    @Test
    fun staticPressure_tenMetresIsAboutOneBar() {
        assertEquals(0.981, TankPressure.staticPressureBar(10.0), 1e-3)
    }

    @Test
    fun staticPressure_zeroOrNegativeIsZero() {
        assertEquals(0.0, TankPressure.staticPressureBar(0.0), 1e-9)
        assertEquals(0.0, TankPressure.staticPressureBar(-3.0), 1e-9)
    }
}
