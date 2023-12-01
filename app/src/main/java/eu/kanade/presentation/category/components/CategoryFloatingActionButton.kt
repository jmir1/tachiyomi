package eu.kanade.presentation.category.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.ExtendedFloatingActionButton
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.util.isScrolledToEnd
import tachiyomi.presentation.core.util.isScrollingUp

@Composable
fun CategoryFloatingActionButton(
    lazyListState: LazyListState,
    onCreate: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(MR.strings.action_add)) },
        icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = "") },
        onClick = onCreate,
        expanded = lazyListState.isScrollingUp() || lazyListState.isScrolledToEnd(),
    )
}
