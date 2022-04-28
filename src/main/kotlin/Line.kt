import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class Line(val a: Point, val b: Point) {

    private var _length: Float? = null
    val length: Float
        get() {
            if (_length == null){
                _length = calcLength()
            }
            return _length ?: throw AssertionError("Line length set to null by another thread")
        }

    //Splits the line randomly and returns the resulting lines
    fun splitLine(): Pair<Line, Line> {
        val newPoint = getRandomPoint()
        return Pair(Line(a, newPoint), Line(newPoint, b))
    }

    fun getRandomPoint(): Point {
        val lineScalingValue = Random.nextDouble(PolygoniserConfig.LINE_BOUNDARY_RATIO, 1 - PolygoniserConfig.LINE_BOUNDARY_RATIO)

        val deltaX = (a.x - b.x) * lineScalingValue
        val deltaY = (a.y - b.y) * lineScalingValue

        return Point((b.x + deltaX).toFloat(), (b.y + deltaY).toFloat())
    }

    // d = sqrt( (x_2-x_1)^2 + (y_2-y_1)^2 )
    private fun calcLength(): Float = sqrt((b.x - a.x).pow(2) + (b.y - a.y).pow(2))

}