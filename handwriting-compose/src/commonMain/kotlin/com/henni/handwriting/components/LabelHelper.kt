package com.henni.handwriting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henni.handwriting.models.Bars
import kotlin.math.min

@Composable
fun LabelHelper(
    data: List<Pair<String, Brush>>,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 13.sp)
) {
    val numberOfGridCells = min(data.size, 3)
    LazyVerticalGrid(columns = GridCells.Fixed(numberOfGridCells), modifier = Modifier) {
        items(data) { (label, color) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                BasicText(
                    text = label,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * RC means Row & Column
 */
@Composable
fun RCChartLabelHelper(
    data: List<Bars>,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 13.sp)
) {
    val labels = data.flatMap { it.values.map { it.label } }.distinct()
    val colors = labels.map { label ->
        data.flatMap { bars ->
            bars.values.filter { it.label == label }.map { it.color }
        }.firstOrNull() ?: SolidColor(Color.Unspecified)
    }
    LabelHelper(
        data = labels.mapIndexed { index, label -> label.orEmpty() to colors[index] },
        textStyle = textStyle
    )
}
