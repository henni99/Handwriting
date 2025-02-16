package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.kmp.HandwritingController

@Composable
fun ColorPicker(
    controller: HandwritingController,
    modifier: Modifier = Modifier,
    list: List<Color> = defaultColorList(),
) {

    Text(
        modifier = Modifier
            .padding(start = 8.dp),
        text = "Color",
        fontSize = 18.sp
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {

        items(list) { color ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { controller.setPenColor(color) }
            )
        }
    }
}

fun defaultColorList(): List<Color> {
    return listOf(
        Color(0xFFf8130d),
        Color(0xFFb8070d),
        Color(0xFF7a000b),
        Color(0xFFff7900),
        Color(0xFFfff8b3),
        Color(0xFFfcf721),
        Color(0xFFf8df09),
        Color(0xFF8a3a00),
        Color(0xFFc0dc18),
        Color(0xFF88dd20),
        Color(0xFF07ddc3),
        Color(0xFF01a0a3),
        Color(0xFF59cbf0),
        Color(0xFF005FFF),
        Color(0xFF022b6d),
        Color(0xFFfa64e1),
        Color(0xFFfc50a6),
        Color(0xFFd7036a),
        Color(0xFFdb94fe),
        Color(0xFFb035f8),
        Color(0xFF7b2bec),
        Color(0xFFb0aaae),
        Color(0xFF768484),
        Color(0xFF333333),
        Color(0xFF0a0c0b),
    )
}