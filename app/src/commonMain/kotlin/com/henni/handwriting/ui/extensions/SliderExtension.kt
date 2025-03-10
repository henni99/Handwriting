package com.henni.handwriting.ui.extensions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.ui.SliderRange

@Composable
fun HandWritingSlider(
    title: String = "",
    value: Float,
    sliderRange: SliderRange = SliderRange.ZERO_TO_ONE,
    onValueChangeFinished: (Float) -> Unit = { }
) {
    var sliderPosition by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        sliderPosition = when (sliderRange) {
            SliderRange.ONE_TO_HUNDRED -> value / 100
            SliderRange.ZERO_TO_ONE -> value
        }
    }

    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            maxLines = 1,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            text = title
        )

        VerticalSpacer(4.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(40.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                text = when (sliderRange) {
                    SliderRange.ONE_TO_HUNDRED -> "${(100 * sliderPosition).toInt()}"
                    SliderRange.ZERO_TO_ONE -> "${(100 * sliderPosition).toInt()}%"
                }
            )
            Slider(
                modifier = Modifier
                    .wrapContentWidth(),
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                onValueChangeFinished = {
                    onValueChangeFinished(
                        when (sliderRange) {
                            SliderRange.ONE_TO_HUNDRED -> (sliderPosition * 100)
                            SliderRange.ZERO_TO_ONE -> sliderPosition
                        }
                    )
                }
            )
        }
    }
}