import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object ImagePalette {
    private val image: BufferedImage = ImageIO.read(File(PolygoniserConfig.IMAGE_FILEPATH))
    private val pixelMean: Float = image.getRGB(0, 0, image.width, image.height, null, 0, image.width).sumOf {
        val col = Color(it)
        (col.red + col.blue + col.green).toDouble() / (image.width * image.height)
    }.toFloat()

    //gets the average color of the given region of the input image
    fun getAverageColour(offset: Pair<Int, Int>, dimensions: Pair<Int, Int>): Color {
        val colArray = image.getRGB(offset.first, offset.second, dimensions.first, dimensions.second, null, 0, dimensions.first)

        var avgRed: Float = 0f
        var avgGreen: Float = 0f
        var avgBlue: Float = 0f

        for (colour in colArray) {
            val col: Color = Color(colour)
            avgRed += col.red
            avgGreen += col.green
            avgBlue += col.blue
        }

        avgRed /= colArray.size
        avgGreen /= colArray.size
        avgBlue /= colArray.size

        return Color(avgRed.toInt(), avgGreen.toInt(), avgBlue.toInt())
    }

    //gets the variance of colour in a given area - to be used as a heuristic when selecting shapes to be split
    //We want areas with less detail to also take less polygons
    fun getColourVariance(offset: Pair<Int, Int>, dimensions: Pair<Int, Int>): Float {
        val colArray = image.getRGB(offset.first, offset.second, dimensions.first, dimensions.second, null, 0, dimensions.first)

        var variance: Float = 0f

        for (colour in colArray){
            val col: Color = Color(colour)

            variance += col.red
            variance += col.blue
            variance += col.green
        }

        return (variance * variance) / colArray.size
    }
}