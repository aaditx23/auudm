package com.aaditx23.auudm.presentation.components.DigitalReceipt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.domain.model.Receipt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DigitalReceipt(
    receipt: Receipt,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
    ) {



        val width = maxWidth

        // Scale factor based on your design image width
        // Suppose original image was designed at 1080px width
        val scale = width / 320.dp   // adjust if needed

        Box {

            // Background Image
            Image(
                painter = painterResource(id = R.drawable.receipt_bg),
                contentDescription = "Receipt",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            /* =======================
               HEADER
            ======================== */

            Text(
                text = "আমির উল উলুম দ্বীনিয়া মাদ্রাসা",
                fontSize = (18.sp * scale),
                fontWeight = FontWeight.W900,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 16.dp * scale),
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 42.dp * scale)
            ) {
                Text(
                    text = "অনুদান রসিদ",
                    fontSize = (12.sp * scale),
                    fontWeight = FontWeight.W600,
                    color = Color.Black
                )
                Text(
                    text = " (দাতার অংশ)",
                    fontSize = (12.sp * scale),
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            }

            /* =======================
               TABLE
            ======================== */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 70.dp * scale)
                    .padding(horizontal = 32.dp * scale)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReceiptText(
                        label = "রসিদ নং:",
                        text = receipt.id.toString(),
                        scale = scale
                    )
                    Spacer(Modifier.weight(1f))
                    ReceiptText(
                        label = "তারিখ:",
                        text = formatDate(receipt.date),
                        scale = scale
                    )
                }
                Spacer(Modifier.height(0.dp))
                ReceiptText(
                    label = "দাতার নাম:",
                    text = receipt.donorName,
                    scale = scale
                )
                Spacer(Modifier.height(0.dp))
                ReceiptText(
                    label = "ঠিকানা:",
                    text = receipt.address,
                    scale = scale
                )
                Spacer(Modifier.height(0.dp))
                ReceiptText(
                    label = "মাস:",
                    text = getMonthString(receipt.month),
                    scale = scale
                )
                Spacer(Modifier.height(0.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReceiptText(
                        label = "মাধ্যম:",
                        text = getMediumString(receipt.medium),
                        scale = scale
                    )
                    Spacer(Modifier.weight(1f))
                    ReceiptText(
                        label = "টাকা:",
                        text = "${receipt.amount}",
                        scale = scale
                    )
                }
                Spacer(Modifier.height(0.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReceiptText(
                        label = "গ্রহণকারীর স্বাক্ষর:",
                        text = receipt.recipientName,
                        scale = scale
                    )
                    Spacer(Modifier.weight(1f))
                    ReceiptText(
                        label = "পদ:",
                        text = receipt.recipientDesignation,
                        scale = scale
                    )
                }
            }

            /* =======================
               FOOTER TEXT
            ======================== */

            Text(
                text = "আল্লাহ তায়ালা আপনার দান সদকায়ে জারিয়া হিসেবে কবুল করুন — আমিন",
                fontSize = (6.sp),
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                color = Color.Black
//                    .offset(y = (-16).dp * scale),

            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return outputFormat.format(Date(timestamp))
}

private fun getMediumString(medium: Int): String {
    return when (medium) {
        1 -> "বিকাশ"
        2 -> "ব্যাংক"
        3 -> "নগদ"
        else -> ""
    }
}

private fun getMonthString(month: Int): String {
    val months = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )
    return months.getOrNull(month - 1) ?: ""
}
