package com.aaditx23.auudm.presentation.components.DigitalReceipt

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.aaditx23.auudm.domain.model.Receipt

@Composable
fun ReceiptBitmapCapture(
    receipt: Receipt,
    onBitmapReady: (Bitmap) -> Unit
) {
    var captured by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                onDrawWithContent {

                    if (!captured && size.width > 0 && size.height > 0) {
                        captured = true

                        val imageBitmap = ImageBitmap(
                            width = size.width.toInt(),
                            height = size.height.toInt()
                        )

                        val originalCanvas = drawContext.canvas
                        drawContext.canvas = Canvas(imageBitmap)

                        // draw receipt into bitmap
                        drawContent()

                        drawContext.canvas = originalCanvas
                        onBitmapReady(imageBitmap.asAndroidBitmap())
                    }

                    // normal draw
                    drawContent()
                }
            }
    ) {
        DigitalReceipt(receipt)
    }
}
