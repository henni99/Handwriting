package com.henni.handwriting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastForEach
import com.henni.handwriting.extension.detectTransformGestures
import com.henni.handwriting.extension.findId
import com.henni.handwriting.extension.getBitmap
import com.henni.handwriting.extension.updateTick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A Composable function that represents a handwriting note.
 *
 * This Composable listens for touch events and allows the user to draw on the canvas.
 * It handles multi-touch gestures like pinch-to-zoom and panning, and it renders the drawn paths
 * on a custom Canvas.
 *
 * @param modifier The modifier to be applied to the outer Box.
 * @param controller The HandwritingController that manages the drawing and state of paths.
 * @param contentWidth The width of the content area.
 * @param contentHeight The height of the content area.
 * @param onInvalidateListener A callback invoked when the canvas is invalidated and needs to be redrawn.
 */

@Composable
fun HandWritingNote(
    modifier: Modifier = Modifier,
    controller: HandwritingController,
    contentWidth: Dp,
    contentHeight: Dp,
    onInvalidateListener: () -> Unit = {}
) {
    val invalidateTick: MutableState<Int> = remember { mutableStateOf(0) }

    var canvas: Canvas? by remember { mutableStateOf(null) }

    var canvasSize: IntSize by remember { mutableStateOf(IntSize.Zero) }

    var canvasImageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset.Zero) }

    var isMultiTouched by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.Main) {
            controller.refreshTick.collect {

                canvasImageBitmap = getBitmap(canvasSize).also {
                    canvas = Canvas(it)
                }

                controller.handwritingPaths.fastForEach { path ->
                    if (!controller.selectedHandwritingPaths.findId(path.id)) {
                        canvas?.drawPath(
                            path = path.renderedPath,
                            paint = path.paint
                        )
                    }
                }

                invalidateTick.updateTick()
            }
        }
    }

    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .width(contentWidth)
                .height(contentHeight)
                .onSizeChanged { newSize ->
                    val size =
                        newSize.takeIf { it.width != 0 && it.height != 0 } ?: return@onSizeChanged

                    canvasImageBitmap = getBitmap(size).also {
                        canvas = Canvas(it)
                    }
                }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y

                    canvasSize = IntSize(size.height.toInt(), size.width.toInt())
                }
                .clipToBounds()
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGestureStart = { offset ->

                            controller.currentTouchEvent.onTouchStart(
                                canvas = canvas,
                                offset = offset,
                                paint = controller.currentPaint
                            )

                            invalidateTick.updateTick()
                        },
                        onGesture = { zoomChange: Float, panChange: Offset, change: PointerInputChange, isMultiTouch: Boolean ->
                            isMultiTouched = isMultiTouch

                            if (isMultiTouch) {

                                scale = (scale * zoomChange).coerceIn(1f, 5f)

                                val extraWidth = (scale - 1) * canvasSize.width
                                val extraHeight = (scale - 1) * canvasSize.height

                                val maxX = extraWidth / 2
                                val maxY = extraHeight / 2

                                offset = Offset(
                                    x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                                    y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                                )

                            } else {

                                controller.currentTouchEvent.onTouchMove(
                                    canvas = canvas,
                                    previousOffset = change.previousPosition,
                                    currentOffset = change.position,
                                    paint = controller.currentPaint
                                )

                            }

                            invalidateTick.updateTick()


                        },
                        onGestureEnd = { isMultiTouch ->
                            isMultiTouched = isMultiTouch

                            if (!isMultiTouch) {
                                controller.currentTouchEvent.onTouchEnd(
                                    canvas = canvas,
                                    paint = controller.currentPaint
                                )
                                invalidateTick.updateTick()
                            }
                        },
                        onGestureCancel = {
                            controller.currentTouchEvent.onTouchInitialize()
                            invalidateTick.updateTick()
                        }
                    )
                }
        ) {
            drawIntoCanvas { canvas ->
                drawRect(controller.contentBackground)

                canvasImageBitmap?.let { bitmap ->
                    canvas.drawImage(bitmap, Offset.Zero, Paint())
                }

                controller.currentTouchEvent.onDrawIntoCanvas(
                    canvas = canvas,
                    paint = controller.currentPaint,
                    isMultiTouch = isMultiTouched
                )
            }

            if (invalidateTick.value != 0) {
                onInvalidateListener()
            }
        }
    }
}