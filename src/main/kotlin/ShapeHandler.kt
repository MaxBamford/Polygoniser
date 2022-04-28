import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.random.Random

class ShapeHandler(original: Shape) {
    private val _shapes: MutableList<Shape> = mutableListOf()
    val paths: List<Pair<Path, Color>> get() = calcPaths()
    var favourLength = true

    init {
        _shapes.add(original)
    }


    private fun calcPaths(): List<Pair<Path, Color>> =
        _shapes.map { it.properties }

    fun splitRandomShape() {
        val chosenShape = _shapes.removeAt(Random.nextInt(0, _shapes.size))
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        _shapes.add(newShape1)
        _shapes.add(newShape2)
    }

    fun splitRandomShapeMaxLengthBias() {
        _shapes.sortByDescending { it.length  }
        val upperBound = if (_shapes.size < PolygoniserConfig.LONGEST_LINE_BIAS_CONSTANT) _shapes.size else PolygoniserConfig.LONGEST_LINE_BIAS_CONSTANT
        val chosenShape = _shapes.removeAt(Random.nextInt(0, upperBound))
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        _shapes.add(newShape1)
        _shapes.add(newShape2)
    }

    fun splitRandomShapeAreaBias() {
        _shapes.sortByDescending { it.area }
        val upperBound = if (_shapes.size < PolygoniserConfig.AREA_BIAS_CONSTANT) _shapes.size else PolygoniserConfig.AREA_BIAS_CONSTANT
        val chosenShape = _shapes.removeAt(Random.nextInt(0, upperBound))
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        _shapes.add(newShape1)
        _shapes.add(newShape2)
    }

    fun splitRandomShapeVarianceBias() {
       _shapes.sortByDescending { it.variance }
        val upperBound = if (_shapes.size < PolygoniserConfig.VARIANCE_BIAS_CONSTANT) _shapes.size else PolygoniserConfig.VARIANCE_BIAS_CONSTANT
        val chosenShape = _shapes.removeAt(Random.nextInt(0, upperBound))
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        _shapes.add(newShape1)
        _shapes.add(newShape2)
    }

    //calculates the average colour of the region each shape lies on in the input image and sets each shape to this colour
    fun calcImageMeanColours() {
        for (shape in _shapes) {
            if (!shape.isColourCalculated) {
                val (offset, dimensions) = shape.calcRegion()
                val newColour = ImagePalette.getAverageColour(offset, dimensions)
                shape.colour = Color(red = newColour.red, green = newColour.green, blue = newColour.blue, alpha = 255)
                shape.isColourCalculated = true
            }
        }
    }
}