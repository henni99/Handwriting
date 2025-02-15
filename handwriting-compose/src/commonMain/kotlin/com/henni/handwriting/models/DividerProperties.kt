package com.henni.handwriting.models

data class DividerProperties(
    val enabled:Boolean = true,
    val xAxisProperties:LineProperties = LineProperties(),
    val yAxisProperties:LineProperties = LineProperties()
)
