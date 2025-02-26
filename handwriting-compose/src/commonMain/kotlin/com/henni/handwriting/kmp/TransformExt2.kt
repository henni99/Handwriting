package com.henni.handwriting.kmp

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.tool.isLassoUsed

/**
 * Customized transform gesture detector.
 *
 * A caller of this function can choose if the pointer events will be consumed.
 * And the caller can implement [onGestureStart] and [onGestureEnd] event.
 *
 * @param canConsumeGesture Lambda that asks the caller whether the gesture can be consumed.
 * @param onGesture This lambda is called when [canConsumeGesture] returns true.
 * @param onGestureStart This lambda is called when a gesture starts.
 * @param onGestureEnd This lambda is called when a gesture ends.
 * @param onTap will be called when single tap is detected.
 * @param onDoubleTap will be called when double tap is detected.
 * @param enableOneFingerZoom If true, enable one finger zoom gesture, double tap followed by
 * vertical scrolling.
 */
suspend fun PointerInputScope.detectTransformGestures(
    toolMode: ToolMode = ToolMode.PenMode,
    cancelIfZoomCanceled: Boolean = true,
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, timeMillis: Long, change: PointerInputChange, dragAmount: Offset, isMultiTouch: Boolean) -> Unit,
    onGestureStart: (Offset) -> Unit = {},
    onGestureEnd: (isMultiTouch: Boolean) -> Unit = {},
) = awaitEachGesture {

    val firstDown = awaitFirstDown(requireUnconsumed = false)
    firstDown.consume()

    onGestureStart(firstDown.position)
    var firstUp: PointerInputChange = firstDown
    var hasMoved = false
    var isMultiTouch = false
    var isLongPressed = false
    var isCanceled = false
    forEachPointerEventUntilReleased(
        toolMode = toolMode,
        onCancel = { isCanceled = true },
    ) { event, isTouchSlopPast ->
        if (isTouchSlopPast) {
            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()
            if (zoomChange != 1f || panChange != Offset.Zero) {
                val centroid = event.calculateCentroid(useCurrent = true)
                val timeMillis = event.changes[0].uptimeMillis
                onGesture(
                    centroid,
                    panChange,
                    zoomChange,
                    timeMillis,
                    event.changes[0],
                    event.changes[0].position,
                    isMultiTouch
                )
                event.consumePositionChanges()
            }
            hasMoved = true
        }
        if (event.changes.size > 1) {
            isMultiTouch = true
        }
        firstUp = event.changes[0]
        val cancelGesture = cancelIfZoomCanceled && isMultiTouch && event.changes.size == 1
        !cancelGesture
    }

    if (firstUp.uptimeMillis - firstDown.uptimeMillis > viewConfiguration.longPressTimeoutMillis) {
        isLongPressed = true
    }

    onGestureEnd(isMultiTouch)
}

/**
 * Invoke action for each PointerEvent until all pointers are released.
 *
 * @param onCancel Callback function that will be called if PointerEvents is consumed by other composable.
 * @param action Callback function that will be called every PointerEvents occur.
 */
private suspend fun AwaitPointerEventScope.forEachPointerEventUntilReleased(
    toolMode: ToolMode,
    onCancel: () -> Unit,
    action: (event: PointerEvent, isTouchSlopPast: Boolean) -> Boolean,
) {
    val touchSlop = TouchSlop(viewConfiguration.touchSlop)
    do {
        val mainEvent = awaitPointerEvent(pass = PointerEventPass.Main)
        if (mainEvent.changes.fastAny { it.isConsumed }) {
            onCancel()
            break
        }

        val isTouchSlopPast = touchSlop.isPast(mainEvent)
        val canContinue = action(mainEvent, isTouchSlopPast)
        if (!canContinue) {
            break
        }
        if (isTouchSlopPast) {
            continue
        }
        if (isLassoUsed(toolMode)) {
            val finalEvent = awaitPointerEvent(pass = PointerEventPass.Final)
            if (finalEvent.changes.fastAny { it.isConsumed }) {
                onCancel()
                break
            }
        }
    } while (mainEvent.changes.fastAny { it.pressed })
}

/**
 * Consume event if the position is changed.
 */
private fun PointerEvent.consumePositionChanges() {
    changes.fastForEach {
        if (it.positionChanged()) {
            it.consume()
        }
    }
}

/**
 * Touch slop detector.
 *
 * This class holds accumulated zoom and pan value to see if touch slop is past.
 *
 * @param threshold Threshold of movement of gesture after touch down. If the movement exceeds this
 * value, it is judged to be a swipe or zoom gesture.
 */
private class TouchSlop(private val threshold: Float) {
    private var pan = Offset.Zero
    private var past = false

    /**
     * Judge the touch slop is past.
     *
     * @param event Event that occurs this time.
     * @return True if the accumulated zoom or pan exceeds the threshold.
     */
    fun isPast(event: PointerEvent): Boolean {
        if (past) {
            return true
        }

        if (event.changes.size > 1) {
            // If there are two or more fingers, we determine the touch slop is past immediately.
            past = true
        } else {
            pan += event.calculatePan()
            past = pan.getDistance() > threshold
        }

        return past
    }
}

