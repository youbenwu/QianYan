package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qianyanhuyu.app_large.util.cdp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 17:42
 * @Description : 网格
 */
@Composable
fun SimpleLazyGrid(
    lists: List<String>,
    modifier: Modifier,
    item: @Composable (
        src: String
    ) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(30.cdp),
        verticalArrangement = Arrangement.spacedBy(30.cdp),
        userScrollEnabled = false,
        modifier = modifier,
        content = {
            items(lists.size) {
                item(lists[it])
            }

        }
    )
}