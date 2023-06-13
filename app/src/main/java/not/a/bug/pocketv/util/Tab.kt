package not.a.bug.notificationcenter.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.compose.ui.zIndex
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.TabRowDefaults


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PremiumPillIndicator(
    currentTabPosition: DpRect,
    modifier: Modifier = Modifier,
    anyTabFocused: Boolean,
    activeColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
    inactiveColor: Color = Color(0xFF484362).copy(alpha = 0.4f)
) {
    val width by animateDpAsState(targetValue = currentTabPosition.width)
    val height = currentTabPosition.height
    val leftOffset by animateDpAsState(targetValue = currentTabPosition.left)
    val topOffset = currentTabPosition.top

    val pillColor by
    animateColorAsState(targetValue = if (anyTabFocused) activeColor else inactiveColor)

    Box(
        modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = leftOffset, y = topOffset)
            .width(width)
            .height(height)
            .background(color = pillColor, shape = RoundedCornerShape(50.dp))
            .zIndex(-1f)
    )
}