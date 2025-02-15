package com.henni.handwriting.utils

import com.henni.handwriting.models.Bars

/**
 * RC means Row & Column
 */
fun checkRCMaxValue(maxValue: Double, data: List<Bars>) {
    require(maxValue >= (data.maxOfOrNull { it.values.maxOfOrNull { it.value } ?: 0.0 } ?: 0.0)) {
        "Chart data must be at most $maxValue (Specified Max Value)"
    }
}

fun checkRCMinValue(minValue: Double, data: List<Bars>) {
    require(minValue <= 0) {
        "Min value in column chart must be 0 or lower."
    }
    require(minValue <= (data.minOfOrNull { it.values.minOfOrNull { it.value } ?: 0.0 } ?: 0.0)) {
        "Chart data must be at least $minValue (Specified Min Value)"
    }
}