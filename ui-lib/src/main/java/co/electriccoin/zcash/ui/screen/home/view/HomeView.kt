package co.electriccoin.zcash.ui.screen.home.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cash.z.ecc.android.sdk.Synchronizer
import co.electriccoin.zcash.spackle.Twig
import co.electriccoin.zcash.ui.R
import co.electriccoin.zcash.ui.common.compose.DisableScreenTimeout
import co.electriccoin.zcash.ui.common.model.WalletSnapshot
import co.electriccoin.zcash.ui.design.component.AppAlertDialog
import co.electriccoin.zcash.ui.design.component.GradientSurface
import co.electriccoin.zcash.ui.design.component.NavigationTabText
import co.electriccoin.zcash.ui.design.theme.ZcashTheme
import co.electriccoin.zcash.ui.fixture.WalletSnapshotFixture
import co.electriccoin.zcash.ui.screen.home.ForcePage
import co.electriccoin.zcash.ui.screen.home.HomeScreenIndex
import co.electriccoin.zcash.ui.screen.home.model.TabItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Preview("Home")
@Composable
private fun ComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            Home(
                forcePage = null,
                isKeepScreenOnWhileSyncing = false,
                isShowingRestoreInitDialog = false,
                onPageChange = {},
                setShowingRestoreInitDialog = {},
                subScreens = persistentListOf(),
                walletSnapshot = WalletSnapshotFixture.new(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Suppress("LongParameterList")
@Composable
fun Home(
    forcePage: ForcePage?,
    isKeepScreenOnWhileSyncing: Boolean?,
    isShowingRestoreInitDialog: Boolean,
    onPageChange: (HomeScreenIndex) -> Unit,
    setShowingRestoreInitDialog: () -> Unit,
    subScreens: ImmutableList<TabItem>,
    walletSnapshot: WalletSnapshot?,
) {
    val pagerState =
        rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { subScreens.size }
        )

    // Using [rememberUpdatedState] to ensure that always the latest lambda is captured
    // And to avoid Detekt warning: Lambda parameters in a @Composable that are referenced directly inside of
    // restarting effects can cause issues or unpredictable behavior.
    val currentOnPageChange = rememberUpdatedState(newValue = onPageChange)

    // Listening for the current page change
    LaunchedEffect(pagerState, currentOnPageChange) {
        snapshotFlow {
            pagerState.currentPage
        }.distinctUntilChanged()
            .collect { page ->
                Twig.info { "Current pager page: $page" }
                currentOnPageChange.value(HomeScreenIndex.fromIndex(page))
            }
    }

    // Force page change e.g. when system back navigation event detected
    forcePage?.let {
        LaunchedEffect(forcePage) {
            pagerState.animateScrollToPage(forcePage.currentPage.ordinal)
        }
    }

    HomeContent(
        pagerState = pagerState,
        subScreens = subScreens,
    )

    if (isShowingRestoreInitDialog) {
        HomeRestoringInitialDialog(setShowingRestoreInitDialog)
    }

    if (isKeepScreenOnWhileSyncing == true &&
        walletSnapshot?.status == Synchronizer.Status.SYNCING
    ) {
        DisableScreenTimeout()
    }
}

@Composable
@Suppress("LongMethod")
@OptIn(ExperimentalFoundationApi::class)
fun HomeContent(
    pagerState: PagerState,
    subScreens: ImmutableList<TabItem>
) {
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout {
        val (pager, tabRow) = createRefs()

        HorizontalPager(
            state = pagerState,
            pageSpacing = 0.dp,
            pageSize = PageSize.Fill,
            pageNestedScrollConnection =
                PagerDefaults.pageNestedScrollConnection(
                    state = pagerState,
                    orientation = Orientation.Horizontal
                ),
            pageContent = { index ->
                subScreens[index].screenContent()
            },
            key = { index ->
                subScreens[index].title
            },
            beyondBoundsPageCount = 1,
            modifier =
                Modifier.constrainAs(pager) {
                    top.linkTo(parent.top)
                    bottom.linkTo(tabRow.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        Column(
            modifier =
                Modifier.constrainAs(tabRow) {
                    top.linkTo(pager.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
        ) {
            HorizontalDivider(
                thickness = DividerDefaults.Thickness,
                color = ZcashTheme.colors.dividerColor
            )
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                // Don't use the predefined divider, as its fixed position is below the tabs bar
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier =
                            Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .padding(horizontal = ZcashTheme.dimens.spacingDefault),
                        color = ZcashTheme.colors.complementaryColor
                    )
                },
                modifier =
                    Modifier
                        .navigationBarsPadding()
                        .padding(
                            horizontal = ZcashTheme.dimens.spacingDefault,
                            vertical = ZcashTheme.dimens.spacingSmall
                        )
            ) {
                subScreens.forEachIndexed { index, item ->
                    val selected = index == pagerState.currentPage

                    NavigationTabText(
                        text = item.title,
                        selected = selected,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        modifier =
                            Modifier
                                .padding(
                                    horizontal = ZcashTheme.dimens.spacingXtiny,
                                    vertical = ZcashTheme.dimens.spacingDefault
                                )
                                .testTag(item.testTag)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeRestoringInitialDialog(setShowingRestoreInitDialog: () -> Unit) {
    AppAlertDialog(
        title = stringResource(id = R.string.restoring_initial_dialog_title),
        text = stringResource(id = R.string.restoring_initial_dialog_description),
        confirmButtonText = stringResource(id = R.string.restoring_initial_dialog_positive_button),
        onConfirmButtonClick = setShowingRestoreInitDialog
    )
}
