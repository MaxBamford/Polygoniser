import org.jetbrains.skiko.toBitmap
import org.jetbrains.skiko.toImage
import java.awt.Color
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import java.io.File
import java.lang.Math.pow
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.sqrt

object ImagePalette {
    private val image: BufferedImage = ImageIO.read(File(PolygoniserConfig.IMAGE_FILEPATH))
    private val greyImage: BufferedImage
    private val greyImageMean: Float
    init {
        val cs = ColorSpace.getInstance(ColorSpace.CS_GRAY)
        val op = ColorConvertOp(cs, null)
        greyImage = op.filter(image, null)

        greyImageMean = greyImage.getRGB(0, 0, greyImage.width, greyImage.height, null, 0, greyImage.width)
            .sumOf { Color(it).red }.toFloat() / (greyImage.width * greyImage.height)
    }


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

        var variance = 0f

        for (colour in colArray){
            val col = Color(colour)

            variance += (col.red - greyImageMean) * (col.red - greyImageMean)
        }

        return variance / colArray.size - 1
    }

    fun getColourDistance(first: Color, second: Color): Float =
        sqrt((sqr(second.red - first.red) + sqr(second.green - first.green) + sqr(second.blue - first.blue)).toFloat())

    private fun sqr(value: Int) = value * value
}