package eu.kanade.tachiyomi.ui.category.manga

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.category.MangaCategoryScreen
import eu.kanade.presentation.category.components.CategoryCreateDialog
import eu.kanade.presentation.category.components.CategoryDeleteDialog
import eu.kanade.presentation.category.components.CategoryRenameDialog
import eu.kanade.presentation.components.LoadingScreen
import eu.kanade.presentation.components.TabContent
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Screen.mangaCategoryTab(): TabContent {
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow
    val screenModel = rememberScreenModel { MangaCategoryScreenModel() }

    val state by screenModel.state.collectAsState()

    return TabContent(
        titleRes = R.string.label_manga,
        searchEnabled = false,
        content = { contentPadding, _ ->

            if (state is MangaCategoryScreenState.Loading) {
                LoadingScreen()
            } else {
                val successState = state as MangaCategoryScreenState.Success

                MangaCategoryScreen(
                    state = successState,
                    contentPadding = contentPadding,
                    onClickCreate = { screenModel.showDialog(MangaCategoryDialog.Create) },
                    onClickRename = { screenModel.showDialog(MangaCategoryDialog.Rename(it)) },
                    onClickDelete = { screenModel.showDialog(MangaCategoryDialog.Delete(it)) },
                    onClickMoveUp = screenModel::moveUp,
                    onClickMoveDown = screenModel::moveDown,
                )

                when (val dialog = successState.dialog) {
                    null -> {}
                    MangaCategoryDialog.Create -> {
                        CategoryCreateDialog(
                            onDismissRequest = screenModel::dismissDialog,
                            onCreate = { screenModel.createCategory(it) },
                        )
                    }
                    is MangaCategoryDialog.Rename -> {
                        CategoryRenameDialog(
                            onDismissRequest = screenModel::dismissDialog,
                            onRename = { screenModel.renameCategory(dialog.category, it) },
                            category = dialog.category,
                        )
                    }
                    is MangaCategoryDialog.Delete -> {
                        CategoryDeleteDialog(
                            onDismissRequest = screenModel::dismissDialog,
                            onDelete = { screenModel.deleteCategory(dialog.category.id) },
                            category = dialog.category,
                        )
                    }
                }

                LaunchedEffect(Unit) {
                    screenModel.events.collectLatest { event ->
                        if (event is MangaCategoryEvent.LocalizedMessage) {
                            context.toast(event.stringRes)
                        }
                    }
                }
            }
        },
        navigateUp = navigator::pop,
    )
}
