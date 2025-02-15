package com.henni.handwriting.extensions

import androidx.compose.ui.Modifier

actual fun Modifier.pointerInteropFilter(onTouchEvent: (MotionEvent) -> Boolean): Modifier =
    this.pointerInteropFilterCommon { event -> onTouchEvent.invoke(event) }
