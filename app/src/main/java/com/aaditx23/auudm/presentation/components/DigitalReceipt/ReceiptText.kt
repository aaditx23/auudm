package com.aaditx23.auudm.presentation.components.DigitalReceipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReceiptText(
    label: String,
    text: String,
    scale: Float,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 12.sp * scale,
            fontWeight = FontWeight.W700,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(2.dp * scale))
        Text(
            text = text,
            fontSize = 10.sp * scale,
            color = Color.Black
        )
    }
}