import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import java.util.Collections
import kotlin.math.abs
import kotlin.random.Random

class Shape(val lines: List<Line>, var colour: Color) {
    private var _area: Float? = null
    private var _coordinatePath: List<Point>? = null
    private var _path: Path? = null
    private var _maxLength: Float? = null
    private var _variance: Float? = null
    var isColourCalculated = false

    val variance: Float
        get() {
            if (_variance == null) {
                val (offset, dims) = calcRegion()
                _variance = ImagePalette.getColourVariance(offset, dims)
            }
            return _variance!!
        }

    val area: Float
        get() {
            if (_area == null) {
                _area = calcArea()
            }
            return _area!!
        }

    val coordinatePath: List<Point>
        get() {
            if (_coordinatePath == null) {
                _coordinatePath = calcCoordinatePath()
            }
            return _coordinatePath!!
        }

    val properties: Pair<Path, Color>
        get() {
            if (_path == null) {
                _path = calcPath()
            }
            return Pair(_path!!, colour)
        }

    val length: Float
        get() {
            if (_maxLength == null) {
                _maxLength = calcMaxLength()
            }
            return _maxLength!!
        }

    //returns a value which describe a rectangular region around the shape
    fun calcRegion(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var minX = lines.first().a.x
        var minY = lines.first().a.y

        var maxX = lines.first().a.x
        var maxY = lines.first().a.y

        for (line in lines) {
            if (line.a.x < minX) minX = line.a.x
            else if (line.a.x > maxX) maxX = line.a.x
            if (line.a.y < minY) minY = line.a.y
            else if (line.a.y > maxY) maxY = line.a.y
        }

        return Pair(Pair(minX.toInt(), minY.toInt()), Pair((maxX - minX).toInt(), (maxY - minY).toInt()))
    }

    //Splits the given shape in two, returning the resulting shapes
    fun splitShape(favourLength: Boolean): Pair<Shape, Shape> {
        //generate two distinct random indices
        val randomIndex1 = if (favourLength) lines.indexOfFirst { it.length == lines.maxOf { it.length } } else Random.nextInt(lines.size)
        var randomIndex2 = Random.nextInt(lines.size)

        while (randomIndex1 == randomIndex2) {
            randomIndex2 = Random.nextInt(lines.size)
        }

        //create two new points which lie on the randomly selected lines
        val newPoint1 = lines[randomIndex1].getRandomPoint()
        val newPoint2 = lines[randomIndex2].getRandomPoint()

        //find which points come just before the new points and insert them into a list just after each
        val insertAfter1 = lines[randomIndex1].a
        val insertAfter2 = lines[randomIndex2].a

        val newShapes = coordinatePath.toMutableList()

        newShapes.add(newShapes.indexOf(insertAfter1) + 1, newPoint1)
        newShapes.add(newShapes.indexOf(insertAfter2) + 1, newPoint2)

        val newShape1: MutableList<Point> = mutableListOf()
        val newShape2: MutableList<Point> = mutableListOf()

        var pointEncountered = false

        //go through the new points and assign them to one shape or another (or both)
        for (point in newShapes) {
            if (point == newPoint1 || point == newPoint2) {
                newShape1.add(point)
                newShape2.add(point)
                pointEncountered = !pointEncountered
            } else if (pointEncountered) {
                newShape2.add(point)
            } else {
                newShape1.add(point)
            }
        }

        return Pair(Util.poly(newShape1), Util.poly(newShape2))
    }

    //checks whether the shape is valid i.e. a cyclic & non-branching path with at least three sides
    fun isValidShape(): Boolean {
        if (lines.size < 3) return false
        var previous = lines.last()
        return lines.all { current -> (current.a == previous.b).also { previous = current } }
    }

    private fun calcMaxLength(): Float =
        lines.maxOf { it.length }

    private fun calcArea(): Float {

        val vertices = coordinatePath.reversed()
        var sum1 = 0.0f
        var sum2 = 0.0f

        for (i in 0 until vertices.size - 1) {
            sum1 += vertices[i].x * vertices[i + 1].y
            sum2 += vertices[i].y * vertices[i + 1].x
        }

        sum1 += vertices.last().x * vertices.first().y
        sum2 += vertices.first().x * vertices.last().y

        return abs(sum1 - sum2) / 2
    }

    //returns the shape as a list of points (no duplicate coordinates)
    private fun calcCoordinatePath(): List<Point> =
        lines.map { it.a }

    private fun calcPath(): Path {
        val path = Path()
        for (point in coordinatePath) {
            path.lineTo(point.x, point.y)
        }
        path.lineTo(coordinatePath.first().x, coordinatePath.first().y)

        return path
    }

    object Util {

        //takes the dimensions of a square and returns a shape object
        fun square(width: Float, height: Float): Shape {
            val pointA = Point(0.0f, 0.0f)
            val pointB = Point(width, 0.0f)
            val pointC = Point(width, height)
            val pointD = Point(0.0f, height)

            val lines = listOf(Line(pointA, pointB), Line(pointB, pointC), Line(pointC, pointD), Line(pointD, pointA))

            return Shape(lines, getRandomColour())
        }

        //takes a list of points and returns a polygon by connecting all points until point[N] - point[0]
        fun poly(points: List<Point>): Shape {
            var previous: Point = points.last()

            return Shape(
                points.map { point ->
                    Line(previous, point).also { previous = point }
                }, getRandomColour()
            ).also { Collections.rotate(it.lines, -1) }


        }

        private fun getRandomColour(): Color =
            when (Random.nextInt(9)) {
                0 -> Color.DarkGray
                1 -> Color.Blue
                2 -> Color.Red
                3 -> Color.Cyan
                4 -> Color.Black
                5 -> Color.Gray
                6 -> Color.Green
                7 -> Color.Magenta
                8 -> Color.Yellow
                else -> Color.Unspecified
            }
    }

}