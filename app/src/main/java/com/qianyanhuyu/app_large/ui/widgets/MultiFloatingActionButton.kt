package com.qianyanhuyu.app_large.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.toPx

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/20 18:06
 * @Description : 自定义悬浮按钮
 */
@Composable
fun MultiFloatingActionButton(
    modifier: Modifier = Modifier,
    srcIcon: ImageVector,
    srcIconColor: Color = Color.White,
    fabBackgroundColor: Color = Color.Unspecified,
    selectState: MutableState<Int>,
    items: List<MultiFabItem>,
    onFabItemClicked: (item: MultiFabItem) -> Unit
) {
    //当前菜单默认状态处于：Collapsed
    val currentState = remember { mutableStateOf(MultiFabState.Collapsed) }

    //创建过渡对象，用于管理多个动画值，并且根据状态变化运行这些值
    val transition = updateTransition(targetState = currentState, label = "")

    val centerContentWidth = 203.cdp
    val centerContentHeight = 150.cdp
    val itemImageSize = 139.cdp
    val mediumWidth = 223.cdp   // 283

    //用于+号按钮的旋转动画
    val rotateAnim: Float by transition.animateFloat(
        transitionSpec = {
            if (targetState.value == MultiFabState.Expanded) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        }, label = ""
    ) { state ->
        //根据state来设置最终的角度
        if (state.value == MultiFabState.Collapsed) 0F else 180F
    }

    //透明度动画
    val alphaAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 200)
    }, label = "") { state ->
        if (state.value == MultiFabState.Expanded) 1F else 0F
    }


    //记录每个Item的收缩动画的Transition
    val shrinkListAnim:MutableList<Float> = mutableListOf()
    items.forEachIndexed { index, _ ->
        //循环生成Transition
        val shrinkAnim by transition.animateFloat(
            targetValueByState = { state ->
                when (state.value) {
                    MultiFabState.Collapsed -> 5F
                    //根据位置，递增每个item的位置高度
                    MultiFabState.Expanded ->
                        when(index) {
                            0 -> (mediumWidth * 2).toPx
                            1 -> mediumWidth.toPx
                            2 -> 5F
                            3 -> mediumWidth.toPx
                            4 -> (mediumWidth * 2).toPx
                            else -> 5F
                        }
                }
            },
            label = "",
            transitionSpec = {
                if (targetState.value == MultiFabState.Expanded) {
                    //dampingRatio属性删除等于默认1F，没有回弹效果
                    spring(stiffness = Spring.StiffnessLow,dampingRatio = 0.58F)
                } else {
                    spring(stiffness = Spring.StiffnessMedium)
                }
            }
        )
        //添加到收缩列表中
        shrinkListAnim.add(index,shrinkAnim)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        CommonComposeImage(
            resId = R.drawable.nav_bg,
            modifier = Modifier
                .width(1485.cdp)
                .height(808.cdp)
                .graphicsLayer {
                    this.translationY = 210.cdp.toPx
                }
                .alpha(animateFloatAsState(alphaAnim, label = "").value)
                .align(Alignment.BottomCenter)
        )

        //创建多个Item,Fab按钮
        items.forEachIndexed{index, item ->

            // 设置偏移量
            val modifierItem = Modifier
                .graphicsLayer {
                    this.translationX = if(index < 2) -shrinkListAnim[index] else shrinkListAnim[index]
                    this.translationY = if(index == 1 || index == 3) {
                        -(shrinkListAnim[index] - 60.cdp.toPx)
                    } else if(index == 2) {
                        if(currentState.value == MultiFabState.Collapsed) shrinkListAnim[index] else -(shrinkListAnim[index] + mediumWidth.toPx + 40.cdp.toPx)

                    } else {
                        20.cdp.toPx
                    }
                }
                .alpha(animateFloatAsState(alphaAnim, label = "").value)

            if(selectState.value == index) {
                CommonComposeImage(
                    resId = R.drawable.nav_item_bg,
                    modifier = modifierItem
                        .width(283.cdp)
                        .height(224.cdp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifierItem
                    .clickable {
                        if (currentState.value == MultiFabState.Expanded){
                            //更新状态 => 折叠菜单
                            currentState.value = MultiFabState.Collapsed
                            selectState.value = index
                            onFabItemClicked(item)
                        }
                    }
            ) {
                CommonComposeImage(
                    resId = item.icon,
                    modifier = Modifier
                        .size(itemImageSize)
                        .aspectRatio(1f)
                )

                Text(
                    item.label,
                    color = item.labelTextColor,
                    fontSize = 35.csp,
                    letterSpacing = 2.csp,
                    modifier = Modifier
                        .clip(Shapes.medium)
                        .alpha(animateFloatAsState(alphaAnim, label = "").value)
                        .background(color = item.labelBackgroundColor)
                        .padding(start = 6.cdp, end = 6.cdp, top = 4.cdp, bottom = 4.cdp)
                )
            }
        }
        CommonComposeImage(
            resId = R.drawable.ic_home,
            modifier = Modifier
                .width(centerContentWidth)
                .height(centerContentHeight)
                .clip(CircleShape)
                .graphicsLayer {
                    this.translationY = 20.cdp.toPx
                }
                .clickable {
                    //更新状态执行：收缩动画
                    currentState.value =
                        if (currentState.value == MultiFabState.Collapsed) MultiFabState.Expanded else MultiFabState.Collapsed
                }
                .rotate(rotateAnim)
        )
    }
}

/**
 * FloatingActionButton填充的数据
 */
class MultiFabItem(
    val index: Int,
    @DrawableRes val icon: Int,
    val label: String,
    val srcIconColor: Color = Color.White,
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.Transparent,
    val fabBackgroundColor: Color = Color.Unspecified,
)

/**
 * 定义FloatingActionButton状态
 */
enum class MultiFabState {
    Collapsed,
    Expanded
}