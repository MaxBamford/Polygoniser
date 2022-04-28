import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class ShapeTest {

    @Test
    fun `If we create a shape of known area and then split the shape the resultant shapes have the same total area as the original shape`() {
        val originalShape = Shape.Util.square(100f, 100f)
        val (newShape1, newShape2) = originalShape.splitShape()

        assertEquals(originalShape.area, newShape1.area + newShape2.area)
    }

    @Test
    fun `If we create a new shape with a known area the area property returns the correct value`() {
        val acceptableError = 0.001f

        val shape1 = Shape.Util.square(50f,50f)
        val expectedArea1: Float = 50f * 50f

        val pointA = Point(0f, 0f)
        val pointB = Point(10f, 10f)
        val pointC = Point(0f, 10f)

        val shape2 = Shape.Util.poly(listOf(pointA, pointB, pointC))//(right angle tri)
        val expectedArea2: Float = 10f * 10f * 0.5f // half (base x height)

        assertEquals(expectedArea1, shape1.area, acceptableError)
        assertEquals(expectedArea2, shape2.area, acceptableError)
    }

    @Test
    fun `If we create a complex shape with known area the area property returns the correct value`() {
        val pointA = Point(5f, 0f)
        val pointB = Point(10f, 10f)
        val pointC = Point(10f, 20f)
        val pointD = Point(0f, 20f)
        val pointE = Point(0f, 10f)

        val complexShape = Shape.Util.poly(listOf(pointA, pointB, pointC, pointD, pointE))
        val expectedValue = 150f

        assertEquals(expectedValue, complexShape.area)
    }

    @Test
    fun `If we create a shape the getCoordinatePath method returns the correct value`() {
        val pointA = Point(0f, 0f)
        val pointB = Point(10f, 10f)
        val pointC = Point(0f, 10f)

        val shape = Shape.Util.poly(listOf(pointA, pointB, pointC))//(right angle tri)
        val expectedOutput = listOf(pointA, pointB, pointC)

        assertEquals(expectedOutput, shape.coordinatePath)
    }

    @Test
    fun `If we create a valid shape and an invalid shape the isValidShape function returns the correct value for both`() {
        val validShape = Shape.Util.square(50f,50f)
        val invalidShape = Shape.Util.poly(listOf(Point(0f, 0f), Point(10f, 10f)))

        assertTrue(validShape.isValidShape())
        assertFalse(invalidShape.isValidShape())
    }


}