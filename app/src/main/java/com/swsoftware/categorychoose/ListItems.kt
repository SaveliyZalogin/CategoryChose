package com.swsoftware.categorychoose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swsoftware.categorychoose.ui.theme.Orange
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CategoryItem(text: String, onChosenStateChanged: (Boolean) -> Unit) {
    // dragging states
    var currentPosition by remember { mutableStateOf(0) }
    var dragging by remember { mutableStateOf(false) }
    var chosen by remember { mutableStateOf(false) }
    var swiped by remember { mutableStateOf(false) }

    // animations
    val returnAfterDragging = remember { Animatable(0f) }
    val borderRadius by animateDpAsState(if (dragging) 10.dp else 0.dp, animationSpec = tween(500))
    var backgroundCircleScale by remember { mutableStateOf(35.dp.value) }

    // other
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // wrapper box
    Box(modifier = Modifier
        .fillMaxWidth()
        .clipToBounds()) {
        // background box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp, 0.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            // background action wrapper
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // icon wrapper
                Box(contentAlignment = Alignment.Center) {
                    // background filling circle
                    Canvas(modifier = Modifier.size(25.dp)) {
                        drawCircle(Orange, radius = if (swiped) backgroundCircleScale else 0F)
                    }
                    // background action icon
                    Image(modifier = Modifier.size(30.dp), imageVector = if (chosen) Icons.Filled.Close else Icons.Filled.Done,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary))
                }
                // background action description
                Text(text = if (chosen) "Отменить" else "Выбрать",
                    style = TextStyle(
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 12.sp
                    )
                )
            }
        }
        // foreground wrapper with dragging actions
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        // set dragging state and offset position
                        onDragStart = {
                            dragging = true
                            currentPosition = 0
                        },
                        onHorizontalDrag = { change, amount ->
                            // check if user swipes forward only left direction or backward until zero offset
                            if ((change.position.x < change.previousPosition.x) || ((currentPosition + amount) <= 0)) {
                                // change offset position
                                currentPosition += amount.roundToInt()
                                // check if user swiped enough to change chosen state
                                if (currentPosition <= -130.dp.toPx() && !swiped) {
                                    // some haptic feedback
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    // change swiped state and animate background circle
                                    swiped = true
                                    coroutineScope.launch {
                                        animate(
                                            35.dp.value,
                                            1000.dp.value,
                                            animationSpec = tween(320)
                                        ) { value, _ ->
                                            backgroundCircleScale = value
                                        }
                                    }
                                }
                                // prepare return animation
                                coroutineScope.launch {
                                    returnAfterDragging.snapTo(currentPosition.toFloat())
                                }
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                dragging = false
                                // start return animation
                                returnAfterDragging.animateTo(0F)
                                // change chosen state if user swiped enough
                                if (swiped) {
                                    swiped = false
                                    chosen = !chosen
                                    onChosenStateChanged(chosen)
                                }
                            }
                        },
                        onDragCancel = {},
                    )
                }
                .offset { IntOffset(returnAfterDragging.value.roundToInt(), 0) }
                .clip(RoundedCornerShape(0.dp, borderRadius, borderRadius, 0.dp)),
            color = MaterialTheme.colors.background,
        ) {
            // information row
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // category title
                Text(
                    text = text,
                    style = TextStyle(
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 24.sp
                    )
                )
                // icon depends on chosen state
                Crossfade(targetState = chosen) { isChosen ->
                    if (!isChosen) {
                        Icon(modifier = Modifier.size(25.dp),
                            painter = painterResource(id = R.drawable.not_interested),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary)
                    } else {
                        Icon(modifier = Modifier.size(25.dp),
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    }
}