package com.qianyanhuyu.app_large.constants

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.qianyanhuyu.app_large.util.cdp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/18 18:59
 * @Description : 一些通用参数
 */
object AppConfig {
    const val APP_DESIGN_WIDTH = 1920


    /**
     * 边框渐变
     */
    val brush247 = Brush.horizontalGradient(
        listOf(
            Color(121, 247, 255),
            Color( 38, 66,89),
            Color(248, 126,198).copy(
                alpha = 0.2f
            ),
        )
    )

    val brush121 = Brush.linearGradient(
            listOf(
                Color(121, 247, 255),
                Color(38, 66, 89),
                Color(239, 0, 170).copy(
                    alpha = 0.45f
                ),
            )
        )

    val brush121_192 = Brush.linearGradient(
        colors = listOf(
            Color(121, 247, 255),
            Color( 38, 66,89),
            Color(192, 159,243).copy(
                alpha = 0.68f
            ),
        )
    )

    val brushBlack = Brush.horizontalGradient(
        listOf(
            Color(58, 62, 69, 0),
            Color(27, 29, 33).copy(
                alpha = 0.9f
            )
        )
    )

    val topTripsColor = Brush.horizontalGradient(
        listOf(
            Color(27, 78, 160),
            Color(49, 122, 247).copy(
                alpha = 0.47f
            )
        )
    )

    val blackToBlack = Brush.verticalGradient(
        listOf(
            Color(36, 39, 49),
            Color(36, 39, 49),
        )
    )

    val whiteToBlack = Brush.verticalGradient(
        listOf(
            Color(0, 0, 0).copy(
                alpha = 0f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.3f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.65f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.97f
            ),
            Color(0, 0, 0),
        )
    )

    val blackToBlack5f = Brush.verticalGradient(
        listOf(
            Color(0, 0, 0).copy(
                alpha = 0f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.6f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.6f
            )
        )
    )

    val whiteToBlackHorizontal = Brush.horizontalGradient(
        listOf(
            Color(0, 0, 0).copy(
                alpha = 0f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.3f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.65f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.97f
            ),
            Color(0, 0, 0),
        )
    )

    val whiteToGreenHorizontal = Brush.horizontalGradient(
        listOf(
            Color(0, 0, 0).copy(
                alpha = 0f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.3f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.65f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.71f
            ),
        )
    )

    val whiteToBlackVertical7f = Brush.verticalGradient(
        listOf(
            Color(0, 0, 0).copy(
                alpha = 0f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.3f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.65f
            ),
            Color(0, 0, 0).copy(
                alpha = 0.71f
            ),
        )
    )

    val ipPutInBg = Brush.verticalGradient(
        listOf(
            Color(35, 47, 186).copy(
                alpha = 0f
            ),
            Color(14, 28, 184).copy(
                alpha = 1f
            )
        )
    )

    val friendsTextColor = Brush.horizontalGradient(
        listOf(
            Color(158, 255, 255).copy(
                alpha = 1f
            ),
            Color(141, 128, 255).copy(
                alpha = 1f
            )
        )
    )

    val ipPutInCircleBorder = Brush.verticalGradient(
        listOf(
            Color(42, 130, 228).copy(
                alpha = 1f
            ),
            Color(54, 87, 151).copy(
                alpha = 1f
            )
        )
    )

    val ipPutInBorder = Brush.verticalGradient(
        listOf(
            Color(7, 72, 160).copy(
                alpha = 1f
            ),
            Color(35, 186, 255).copy(
                alpha = 1f
            )
        )
    )

    val ipPutInCenterLine = Brush.verticalGradient(
        listOf(
            Color(42, 98, 209).copy(
                alpha = 0f
            ),
            Color(41, 98, 209).copy(
                alpha = 0.7f
            ),
            Color(40, 98, 209).copy(
                alpha = 1f
            ),
            Color(42, 98, 209).copy(
                alpha = 0.7f
            ),
            Color(42, 98, 209).copy(
                alpha = 0f
            )
        )
    )

    val mediaViewBackground = Brush.verticalGradient(
        listOf(
            Color(16, 27, 150).copy(
                alpha = 0f
            ),
            Color(78, 118, 230).copy(
                alpha = 0.65f
            ),
            Color(0, 0, 0).copy(
                alpha = 0f
            ),
        )
    )

    val mediaViewBorder = Brush.verticalGradient(
        listOf(
            Color(16, 28, 160).copy(
                alpha = 1f
            ),
            Color(35, 186, 255).copy(
                alpha = 0f
            )
        )
    )

    val originToBlueHorizontal = Brush.horizontalGradient(
        listOf(
            Color(247, 156, 37).copy(
                alpha = 0.88f
            ),
            Color(150, 217, 26),
            Color(75, 186, 250),
        )
    )

    // 36 开头的黑色
    val Black36 = Color(36, 39, 49, 1)

    val CustomOrigin = Color(255, 87, 51)
    val CustomYellowish = Color(205, 113, 65)
    val CustomYellow = Color(255, 195, 0)
    val CustomPurple = Color(108, 93, 211)
    val CustomLavender = Color(154, 137, 189)
    val CustomGreen = Color(67, 207, 124)
    val CustomGreen9 = Color(68, 227, 64)
    val CustomGreen165 = Color(165, 214, 63)
    val CustomBlue = Color(32, 169, 232)
    val CustomRed = Color(229, 9, 20)
    val CustomBlue9 = Color(56, 69, 171)


    // 大圆数量
    const val CIRCLE_COUNT = 6
    // 每一圈显示的图片数量
    const val CIRCLE_NUMBER_IMAGE = 5

    // 店友圈 -> 设置头像圆的大小,重复数据增加概率
    val smallCircleRadius = listOf(
        80.cdp,
        50.cdp,
        30.cdp,
        80.cdp,
    )
}