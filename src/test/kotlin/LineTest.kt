import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.sqrt

internal class LineTest {

    @Test
    fun `If we create a line and then call splitLine the sum of the resulting lines length is equal to the original lines length`() {
        val originalLine = Line(Point(0f, 0f), Point(10f, 10f))
        val acceptableError = 0.001f

        val (newLine1, newLine2) = originalLine.splitLine()

        val originalLine1 = Line(Point(10f, 10f), Point(0f, 0f))

        val (newLine3, newLine4) = originalLine1.splitLine()

        assertEquals(originalLine.length, newLine1.length + newLine2.length, acceptableError)
        assertEquals(originalLine1.length, newLine3.length + newLine4.length, acceptableError)
    }

    @Test
    fun `If we create a line of known length the length property returns the correct value`() {
        val line1 = Line(Point(0f, 0f), Point(10f, 0f))
        val expectedLength1 = 10f

        val line2 = Line(Point(10f, 10f), Point(0f, 0f))
        val expectedLength2 = 14.142f //sqrt(10^2 + 10^2)
        val acceptableError = 0.001f

        assertEquals(expectedLength1, line1.length, acceptableError)
        assertEquals(expectedLength2, line2.length, acceptableError)
    }
}