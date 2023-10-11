package com.qianyanhuyu.app_large.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.util.TypedValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.qianyanhuyu.app_large.constants.AppConfig.APP_DESIGN_WIDTH
import java.lang.Math.PI
import java.lang.Math.cos
import java.lang.Math.sin
import kotlin.math.pow


/**
 * compose屏幕适配单位
 */
val Number.cdp
    get() = Dp(
        toFloat() *
                Resources.getSystem().displayMetrics.widthPixels
                / APP_DESIGN_WIDTH
                / Resources.getSystem().displayMetrics.density
    )

val Dp.toPx
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics)

/**
 * compose屏幕适配单位（字体专用）
 */
val Number.csp
    get() = (toFloat() *
            Resources.getSystem().displayMetrics.widthPixels
            / APP_DESIGN_WIDTH
            / Resources.getSystem().displayMetrics.scaledDensity).sp


/**
 * 将数字转换成compose中的DP
 */
val Number.transformDp
    get() = Dp(toFloat() / Resources.getSystem().displayMetrics.density)

/**
 * 根据圆心计算出坐标
 */
fun inCircleOffset(center: Offset, radius: Float, angle: Int): Offset {
    return Offset((center.x + radius * kotlin.math.cos(angle * PI / 180)).toFloat(), (center.y + radius * kotlin.math.sin(
        angle * PI / 180
    )).toFloat())
}

/**
 * 根据url获取网络图片的bitmap
 */
suspend fun Context.getImageBitmapByUrl(url: String, size: Int = 100): Bitmap? {
    val request = ImageRequest.Builder(this)
        .size(size)
        .data(url)
        .allowHardware(false)
        .build()
    val result = (imageLoader.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}

fun Number.units(): List<Int> {
    try {
        val units = mutableListOf<Int>()

        if(this.toInt() <= 0 ) {
            units.add(0)
        } else {
            for (i in this.toString().length downTo 0 ) {
                if(i != this.toString().length) {
                    units.add(this.toInt() / 10.toDouble().pow(i).toInt() % 10)
                }
            }
        }

        return units
    } catch (e: Exception) {
        Log.d("Number.units exception: ", "data: $this -- error: ${e.message.toString()}")
        return listOf(0)
    }
}


