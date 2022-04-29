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
        _shapes.sortByDescending { it.length }
        val upperBound =
            if (_shapes.size < PolygoniserConfig.LONGEST_LINE_BIAS_CONSTANT) _shapes.size else PolygoniserConfig.LONGEST_LINE_BIAS_CONSTANT
        val chosenShape = _shapes.removeAt(Random.nextInt(0, upperBound))
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        _shapes.add(newShape1)
        _shapes.add(newShape2)
    }

    fun splitRandomShapeAreaBias() {
        _shapes.sortByDescending { it.area }
        val upperBound =
            if (_shapes.size < PolygoniserConfig.AREA_BIAS_CONSTANT) _shapes.size else PolygoniserConfig.AREA_BIAS_CONSTANT
        val chosenShape = _shapes.removeAt(Random.nextInt(0, upperBound))
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        _shapes.add(newShape1)
        _shapes.add(newShape2)
    }

    //We select a shape to be split based on area - every time we split a shape we check if the colours of the new shapes are different (bounded by MINIMUM_COLOUR_DISTANCE)
    //If the new shapes colours differ enough they are split, otherwise we do not split the shapes and we increment splitAttempts
    //if a shapes splitAttempts exceeds MAXIMUM_SPLIT_ATTEMPTS it is no longer considered for splitting
    fun splitShapeAreaAndColourBias() {
        val tempShapes = _shapes.filter { it.splitAttempts < PolygoniserConfig.MAXIMUM_SPLIT_ATTEMPTS }.sortedByDescending { it.area }
        val upperBound =
            if (tempShapes.size < PolygoniserConfig.AREA_BIAS_CONSTANT) tempShapes.size else PolygoniserConfig.AREA_BIAS_CONSTANT
        val chosenShape = tempShapes[Random.nextInt(0, upperBound)]
        val (newShape1, newShape2) = chosenShape.splitShape(favourLength)
        val (offset1, dimensions1) = newShape1.calcRegion()
        val (offset2, dimensions2) = newShape2.calcRegion()
        val colour1 = ImagePalette.getAverageColour(offset1, dimensions1)
        val colour2 = ImagePalette.getAverageColour(offset2, dimensions2)
        if (ImagePalette.getColourDistance(colour1, colour2) < PolygoniserConfig.MINIMUM_COLOUR_DISTANCE) {
            _shapes[_shapes.indexOf(chosenShape)].addAttempt()
        } else {
            _shapes.remove(chosenShape)
            newShape1.colour = Color(red = colour1.red, green = colour1.green, blue = colour1.blue, alpha = 255)
            newShape1.isColourCalculated = true
            newShape1.setAttempts(chosenShape.splitAttempts)
            newShape2.colour = Color(red = colour2.red, green = colour2.green, blue = colour2.blue, alpha = 255)
            newShape2.isColourCalculated = true
            newShape2.setAttempts(chosenShape.splitAttempts)
            _shapes.add(newShape1)
            _shapes.add(newShape2)
        }

    }

    @Deprecated("Variance turns out to be a bad metric for splitting shapes as the algorithm gets stuck in local maxima of massive variance (i.e on a tiny white/black border)")
    fun splitRandomShapeVarianceBias() {
        _shapes.sortByDescending { it.variance }
        val upperBound =
            if (_shapes.size < PolygoniserConfig.VARIANCE_BIAS_CONSTANT) _shapes.size else PolygoniserConfig.VARIANCE_BIAS_CONSTANT
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