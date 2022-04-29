import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

var shapeHandler = ShapeHandler(PolygoniserConfig.STARTING_SHAPE).also {it.calcImageMeanColours()}

@Composable
@Preview
fun polyApp() {

    Column(modifier = Modifier.padding(all = 8.dp)) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            val fieldValue = remember { mutableStateOf(TextFieldValue()) }
            val fieldContent: Int = if (fieldValue.value.text == "") 0 else fieldValue.value.text.toInt()

            TextField(value = fieldValue.value, onValueChange = { fieldValue.value = it })
            splitWithAreaAndColourButton(fieldContent)
            splitWithAreaButton(fieldContent)
            resetShapeButton()
            favourLengthCheckBox()
        }
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxSize()) {
            shapeHandler.paths.forEach {
                drawPath(
                    path = it.first, color = it.second
                )
            }

        }
    }

}

@Composable
fun calcMeanImageColsButton() {
    Button(
        onClick = { shapeHandler.calcImageMeanColours() }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) { Text("calcImageCols") }
}

@Composable
fun resetShapeButton() {
    Button(
        onClick = {
            shapeHandler = ShapeHandler(PolygoniserConfig.STARTING_SHAPE)
            shapeHandler.calcImageMeanColours()
        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("Reset")
    }

}

@Composable
fun favourLengthCheckBox() {
    val checked = remember { mutableStateOf(shapeHandler.favourLength) }
    Checkbox(checked = checked.value, enabled = true, onCheckedChange = {
        checked.value = it
        shapeHandler.favourLength = it
    })


}

@Composable
fun splitWithAreaButton(repetitions: Int) {

    Button(
        onClick = {
            repeat(repetitions) {
                shapeHandler.splitRandomShapeAreaBias()
            }
            shapeHandler.calcImageMeanColours()

        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("Split Area Bias")
    }
}

@Composable
fun splitWithAreaAndColourButton(repetitions: Int) {

    Button(
        onClick = {
            println(measureTimeMillis {
                repeat(repetitions) {
                    shapeHandler.splitShapeAreaAndColourBias()
                }
            })
        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("Split Area/Col Bias")
    }
}

@Deprecated("Variance turns out to be a bad metric for splitting shapes as the algorithm gets stuck in local maxima of massive variance (i.e on a tiny white/black border)")
@Composable
fun splitWithVarianceButton(repetitions: Int) {

    Button(
        onClick = {
            repeat(repetitions) {
                shapeHandler.splitRandomShapeVarianceBias()
            }
            shapeHandler.calcImageMeanColours()
        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("Split Variance Bias")
    }
}

@Composable
fun splitWithLineLengthButton(repetitions: Int) {

    Button(
        onClick = {
            repeat(repetitions) {
                shapeHandler.splitRandomShapeMaxLengthBias()
            }
            shapeHandler.calcImageMeanColours()
        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("Split Line Length Bias")
    }
}

@Composable
fun splitButton(repetitions: Int) {
    Button(
        onClick = {
            repeat(repetitions) {
                shapeHandler.splitRandomShape()
            }
            shapeHandler.calcImageMeanColours()
        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("Split No Bias")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        polyApp()
    }
}


