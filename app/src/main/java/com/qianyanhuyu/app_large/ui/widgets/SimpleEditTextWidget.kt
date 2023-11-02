package com.qianyanhuyu.app_large.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.qianyanhuyu.app_large.ui.theme.EditTextBottomLineColor
import com.qianyanhuyu.app_large.ui.theme.IconColor
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/18 17:02
 * @Description : 通用输入框 基于BasicTextField 处理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleEditTextWidget(
    value: String,
    @StringRes valueLabel: Int? = null,
    isReadOnly: Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        color = Color.White
    ),
    isShowTrailingIcon: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = Color.White,
        containerColor = Color.Transparent,
        focusedIndicatorColor = EditTextBottomLineColor,
        unfocusedIndicatorColor = EditTextBottomLineColor.copy(
            alpha = 0.6f
        ),
        unfocusedLeadingIconColor = IconColor,
        focusedLeadingIconColor = IconColor,
        unfocusedTrailingIconColor = Color.White,
        focusedTrailingIconColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White
    ),
    paddingValues: PaddingValues = PaddingValues(4.cdp)
) {
    BasicTextField(
        value = value,
        modifier = modifier
            .background(Color.Transparent, shape)
            .indicatorLine(enabled, isError, interactionSource, colors)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(/*colors.cursorColor(isError).value*/EditTextBottomLineColor),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                colors = colors,
                interactionSource = interactionSource,
                contentPadding = paddingValues,
                label = valueLabel?.let {
                    {
                        Text(
                            text = stringResource(id = it),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.csp,
                            modifier = Modifier
                                .padding(vertical = 8.cdp)
                        )
                    }
                },
                trailingIcon = if (value.isNotEmpty() && !isReadOnly && isShowTrailingIcon ) {
                    {
                        IconButton(
                            onClick = onClick
                        ) {
                            Icon(
                                Icons.Outlined.Clear,
                                contentDescription = "清除"
                            )
                        }
                    }
                } else null,
                leadingIcon = leadingIcon?.let {
                    {
                        CommonIcon(
                            leadingIcon,
                            tint = IconColor,
                            modifier = Modifier
                                .size(50.cdp)
                        )
                    }
                },
                shape = Shapes.small
            )
        }
    )
}