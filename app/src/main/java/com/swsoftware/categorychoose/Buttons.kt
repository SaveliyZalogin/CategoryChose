package com.swsoftware.categorychoose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swsoftware.categorychoose.ui.theme.DisabledOrange

@Composable
fun LaterButton(modifier: Modifier, onClick: () -> Unit = {}) {
    Button(modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant
        ),
        elevation = ButtonDefaults.elevation(0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Позже",
            style = TextStyle(
                color = MaterialTheme.colors.onSecondary,
                fontSize = 18.sp
            )
        )
    }
}
@Composable
fun ContinueButton(modifier: Modifier, disabled: Boolean, onClick: () -> Unit = {}) {
    val backgroundColor by animateColorAsState(
        targetValue = if (disabled) DisabledOrange else MaterialTheme.colors.primary,
        animationSpec = tween(200)
    )
    val contentColor by animateColorAsState(
        targetValue = if (disabled) Color.Black else Color.White,
        animationSpec = tween(200)
    )
    Button(modifier = modifier,
        onClick = onClick,
        enabled = !disabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = backgroundColor,
            disabledContentColor = contentColor
        ),
        elevation = ButtonDefaults.elevation(0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Продолжить",
            style = TextStyle(
                fontSize = 18.sp
            )
        )
    }
}