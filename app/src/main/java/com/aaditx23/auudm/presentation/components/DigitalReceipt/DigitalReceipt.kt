package com.aaditx23.auudm.presentation.components.DigitalReceipt

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.presentation.util.Constants
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import java.text.NumberFormat


@SuppressLint("UnusedBoxWithConstraintsScope", "LocalContextConfigurationRead")
@Composable
fun DigitalReceipt(
    receipt: Receipt,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsDataStore: SettingsDataStore = koinInject()
    val language by settingsDataStore.languageFlow.collectAsState()

    val localizedResources = remember(language) {
        val locale = Locale.Builder().setLanguage(language).build()
        val configuration = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        context.createConfigurationContext(configuration).resources
    }

    // Load string resources
    val headerText = localizedResources.getString(R.string.receipt_header)
    val titleText = localizedResources.getString(R.string.receipt_title)
    val subtitleText = localizedResources.getString(R.string.receipt_subtitle)
    val footerText = localizedResources.getString(R.string.receipt_footer)

    val address_details = localizedResources.getString(R.string.address_details)
    val dateLabel = localizedResources.getString(R.string.date_short)
    val donorNameLabel = localizedResources.getString(R.string.donor_name_label)
    val addressLabel = localizedResources.getString(R.string.address_label)
    val monthLabel = localizedResources.getString(R.string.month_short)
    val mediumLabel = localizedResources.getString(R.string.medium_short)
    val amountLabel = localizedResources.getString(R.string.amount_short)
    val recipientSignatureLabel = localizedResources.getString(R.string.recipient_signature)
    val designationLabel = localizedResources.getString(R.string.designation_label)
    val yearLabel = localizedResources.getString(R.string.year)

    // Load month and medium resources
    val months = Constants.MONTH_IDS.map { localizedResources.getString(it) }
    val mediums = Constants.MEDIUM_IDS.map { localizedResources.getString(it) }
    val recipients = Constants.RECIPIENT_IDS.map { localizedResources.getString(it) }
    val designations = Constants.RECIPIENT_DESIGNATION_IDS.map { localizedResources.getString(it) }

    // Date formatter
    val locale = remember(language) { Locale.Builder().setLanguage(language).build() }
    val integerFormat = remember { NumberFormat.getIntegerInstance(locale) }
    integerFormat.isGroupingUsed = false

    val dateFormat = remember(locale) { SimpleDateFormat("dd/MM/yyyy", locale) }
    val formattedDate = remember(receipt.date, locale) { dateFormat.format(Date(receipt.date)) }
    val monthName = receipt.month.joinToString(", ") { months.getOrNull(it - 1) ?: "" }
    val mediumName = mediums.getOrNull(receipt.medium - 1) ?: ""
    val recipientName = recipients.getOrNull(receipt.recipientIndex) ?: ""
    val recipientDesignation = designations.getOrNull(receipt.recipientIndex) ?: ""
    val year = integerFormat.format(receipt.year)
    // Format amount according to locale
    val formattedAmount = remember(receipt.amount, locale) {
        val numberFormat = java.text.NumberFormat.getNumberInstance(locale)
        numberFormat.format(receipt.amount)
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val width = maxWidth
        val scale = width / 320.dp

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

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = headerText,
                    fontSize = (18.sp * scale),
                    fontWeight = FontWeight.W900,
                    modifier = Modifier
                        .offset(y=10.dp * scale),
                    color = Color.Black
                )
                Text(
                    text = address_details,
                    fontSize = (8.sp * scale),
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .offset(y=5.dp * scale),
                    color = Color.Black
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = titleText,
                        fontSize = (12.sp * scale),
                        fontWeight = FontWeight.W600,
                        color = Color.Black
                    )
                    Text(
                        text = " $subtitleText",
                        fontSize = (12.sp * scale),
                        fontStyle = FontStyle.Italic,
                        color = Color.Black
                    )
                }
            }

            /* =======================
               TABLE
            ======================== */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 75.dp * scale)
                    .padding(horizontal = 32.dp * scale),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReceiptText(
                        label = donorNameLabel,
                        text = receipt.donorName,
                        scale = scale,
                        modifier = Modifier.fillMaxWidth(0.55f)

                    )


                    ReceiptText(
                        label = dateLabel,
                        text = formattedDate,
                        scale = scale,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                ReceiptText(
                    label = addressLabel,
                    text = receipt.address,
                    scale = scale
                )

                ReceiptText(
                    label = monthLabel,
                    text = monthName,
                    scale = scale,
                    modifier = Modifier.fillMaxWidth()
                )
                ReceiptText(
                    label = yearLabel,
                    text = year,
                    scale = scale,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReceiptText(
                        label = mediumLabel,
                        text = mediumName,
                        scale = scale,
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    ReceiptText(
                        label = amountLabel,
                        text = formattedAmount,
                        scale = scale,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReceiptText(
                        label = recipientSignatureLabel,
                        text = recipientName,
                        scale = scale,
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    ReceiptText(
                        label = designationLabel,
                        text = recipientDesignation,
                        scale = scale,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            /* =======================
               FOOTER TEXT
            ======================== */

            Text(
                text = footerText,
                fontSize = (5.sp*scale),
                modifier = Modifier.align(Alignment.BottomCenter),
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.W300
            )
        }
    }
}