package com.henni.handwriting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.henni.handwriting.ColumnChart
import com.henni.handwriting.LineChart
import com.henni.handwriting.PieChart
import com.henni.handwriting.RowChart
import com.henni.handwriting.models.Bars
import com.henni.handwriting.models.HorizontalIndicatorProperties
import com.henni.handwriting.models.IndicatorCount
import com.henni.handwriting.models.Line
import com.henni.handwriting.models.Pie

@Composable
fun PhoneSample() {
    LazyColumn(
        modifier = Modifier
            .statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Row {
                    PieSample()
                }
                Row {
                    PieSample2()
                }
                Row {
                    PieSample3()
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Row {
                    ColumnSample()
                }
                Row {
                    ColumnSample2()
                }
                Row {
                    ColumnSample3()
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Row {
                    RowSample()
                }
                Row {
                    RowSample2()
                }
                Row {
                    RowSample3()
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Row {
                    LineSample()
                }
                Row {
                    LineSample2()
                }
                Row {
                    LineSample4()
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Row {
                    LineSample8()
                }
                Row {
                    LineSample7()
                }
                Row {
                    LineSample6()
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Row {
                    LineSample5()
                }
                Row {
                    LineSample3()
                }
                Row {
                    LineSample9()
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}