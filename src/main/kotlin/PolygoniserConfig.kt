import java.io.File
import javax.imageio.ImageIO

object PolygoniserConfig {
    const val IMAGE_FILEPATH = "/home/mxb/IdeaProjects/Polygoniser/src/main/resources/DEX.jpg"
    val STARTING_SHAPE: Shape
    const val LINE_BOUNDARY_RATIO = 0.4 //The acceptable proportion of a line away from the ends to perform a split
    const val AREA_BIAS_CONSTANT = 5 //when the next shape is chosen to be split using the area bias this value is how many of the top shapes are considered for splitting
    const val LONGEST_LINE_BIAS_CONSTANT = 5 //How many shapes are considered when using line bias
    const val VARIANCE_BIAS_CONSTANT = 5
    const val MAXIMUM_SPLIT_ATTEMPTS = 5 //How many times a shape is split (and then the split is discounted because the resultant shapes colours are within the bound) before the shape is no longer considered for spliting
    const val MINIMUM_COLOUR_DISTANCE: Float = 5f //MAXIMUM VALUE = 441.67f
    init {
        val image = ImageIO.read(File(IMAGE_FILEPATH))
        STARTING_SHAPE = Shape.Util.square(image.width.toFloat(),image.height.toFloat())
    }

}