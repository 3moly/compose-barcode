package com.threemoly.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.threemoly.barcode.barcodeImageBitmap
import com.threemoly.barcode.BarcodeType

@Composable
fun ExampleApp() {
    var number by remember {
        mutableStateOf("barcode")
    }
    val imageBitmap = remember(number) {
        barcodeImageBitmap(
            number = number,
            barcodeFormat = BarcodeType.QR_CODE,
            width = 500,
            height = 500
        )
    }
    SampleTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                number,
                onValueChange = { value ->
                    number = value
                },
                modifier = Modifier
            )
            if (imageBitmap != null)
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(250.dp).background(Color.Yellow)
                )
            else {
                Text("no image")
            }
        }
    }
}