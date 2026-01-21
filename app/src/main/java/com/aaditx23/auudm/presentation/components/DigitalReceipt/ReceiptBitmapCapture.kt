package com.aaditx23.auudm.presentation.components.DigitalReceipt

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.domain.model.Receipt

@Composable
fun ReceiptBitmapCapture(
    receipt: Receipt,
    onBitmapReady: (Bitmap) -> Unit
) {
    // BACKGROUND IMAGE SIZE (SOURCE OF TRUTH)
    val bgWidthPx = 5100
    val bgHeightPx = 3722

    var captured by remember { mutableStateOf(false) }

    // UI size is only for preview
    val uiWidthDp = 510.dp
    val uiHeightDp = uiWidthDp * bgHeightPx / bgWidthPx
    println(uiHeightDp)

    Box(
        modifier = Modifier
            .width(uiWidthDp)
            .wrapContentHeight()
            .drawWithCache {

                onDrawWithContent {

                    if (!captured && size.width > 0f && size.height > 0f) {
                        captured = true

                        val bitmap = ImageBitmap(
                            width = bgWidthPx,
                            height = bgHeightPx
                        )

                        val originalCanvas = drawContext.canvas
                        drawContext.canvas = Canvas(bitmap)

                        // scale UI → image pixels
                        val scaleX = bgWidthPx / size.width
                        val scaleY = bgHeightPx / size.height
                        drawContext.canvas.scale(scaleX, scaleY)

                        drawContent()

                        drawContext.canvas = originalCanvas

                        onBitmapReady(bitmap.asAndroidBitmap())
                    }

                    // draw normally on screen
//                    drawContent()
                }
            }
    ) {
        DigitalReceipt(
            receipt = receipt,
//            modifier = Modifier.matchParentSize()
        )
    }
}
