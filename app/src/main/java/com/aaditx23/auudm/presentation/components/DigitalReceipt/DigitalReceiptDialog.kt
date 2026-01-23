package com.aaditx23.auudm.presentation.components.DigitalReceipt

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.aaditx23.auudm.R
import com.aaditx23.auudm.domain.model.Receipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun DigitalReceiptDialog(
    receipt: Receipt,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Load string resources
    val saveText = stringResource(R.string.save)
    val shareText = stringResource(R.string.share)
    val closeText = stringResource(R.string.close)
    val generatingText = stringResource(R.string.generating_receipt)
    val savedText = stringResource(R.string.receipt_saved)
    val saveFailedText = stringResource(R.string.receipt_save_failed)
    val shareFailedText = stringResource(R.string.receipt_share_failed)
    val shareTitle = stringResource(R.string.share_receipt_title)


    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var startCapture by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    // Wait for first frame before starting capture
    LaunchedEffect(Unit) {
        withFrameNanos { }
        startCapture = true
    }

    /**
     * Saves the receipt bitmap to device storage
     */
    suspend fun saveImage(bmp: Bitmap) {
        isSaving = true
        try {
            withContext(Dispatchers.IO) {
                val filename = "receipt_${receipt.id}.png"
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }
                }
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, savedText, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, String.format(saveFailedText, e.message), Toast.LENGTH_LONG).show()
            }
        } finally {
            isSaving = false
        }
    }

    /**
     * Shares the receipt bitmap via Android share sheet
     */
    suspend fun shareImage(bmp: Bitmap) {
        isSharing = true
        try {
            val file = withContext(Dispatchers.IO) {
                val filename = "receipt_${receipt.id}.png"
                val tempFile = File(context.cacheDir, filename)
                tempFile.outputStream().use {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                tempFile
            }

            withContext(Dispatchers.Main) {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, shareTitle))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, String.format(shareFailedText, e.message), Toast.LENGTH_LONG).show()
            }
        } finally {
            isSharing = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        bitmap?.let { bmp ->
                            coroutineScope.launch {
                                saveImage(bmp)
                            }
                        }
                    },
                    enabled = bitmap != null && !isSaving && !isSharing
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(saveText)
                    }
                }

                Button(
                    onClick = {
                        bitmap?.let { bmp ->
                            coroutineScope.launch {
                                shareImage(bmp)
                            }
                        }
                    },
                    enabled = bitmap != null && !isSaving && !isSharing
                ) {
                    if (isSharing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(shareText)
                    }
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving && !isSharing
            ) {
                Text(closeText)
            }
        },
        title = null,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bitmap == null) {
                    Text(generatingText)
                } else {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (startCapture && bitmap == null) {
                    ReceiptBitmapCapture(receipt) {
                        bitmap = it
                    }
                }
            }
        }
    )
}
