package com.henni.handwriting.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.henni.handwriting.models.AnimationMode
import com.henni.handwriting.models.Bars
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * RC means Row & Column
 */
@Composable
fun ImplementRCAnimation(
    data:List<Bars>,
    animationMode: AnimationMode,
    spec: (Bars.Data)->AnimationSpec<Float>,
    delay:Long,
    before:()->Unit
) {
    LaunchedEffect(data) {
        before()
        delay(delay)
        data.map { it.values }.flatten().filter { it.value != 0.0 }.forEachIndexed { index, data ->
            val animate: suspend () -> Unit = {
                data.animator.animateTo(
                    1f,
                    animationSpec = spec(data)
                )
            }
            when (animationMode) {
                is AnimationMode.OneByOne -> {
                    animate()
                }

                is AnimationMode.Together -> {
                    launch {
                        delay(animationMode.delayBuilder(index))
                        animate()
                    }
                }
            }
        }
    }
}