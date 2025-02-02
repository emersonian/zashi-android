@file:Suppress("TooManyFunctions")

package co.electriccoin.zcash.ui.design.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.electriccoin.zcash.ui.design.R
import co.electriccoin.zcash.ui.design.theme.ZcashTheme
import co.electriccoin.zcash.ui.design.theme.internal.SecondaryTypography

@Preview
@Composable
private fun TopAppBarTextComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(titleText = "Screen A", backText = "Back")
        }
    }
}

@Preview
@Composable
private fun TopAppBarTextRestoringComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                titleText = "Screen A",
                backText = "Back",
                restoringLabel = "[RESTORING YOUR WALLET…]"
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarTextRestoringLongComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                titleText = "Screen A",
                backText = "Back",
                restoringLabel = "[RESTORING YOUR WALLET LONG TEXT…]"
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarLogoComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(showTitleLogo = true, backText = "Back")
        }
    }
}

@Preview
@Composable
private fun TopAppBarLogoRestoringComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                showTitleLogo = true,
                backText = "Back",
                restoringLabel = "[RESTORING YOUR WALLET…]"
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarRegularMenuComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                titleText = "Screen B",
                regularActions = {
                    TopBarRegularMenuExample(
                        actionOneCallback = {},
                        actionTwoCallback = {}
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarOneVisibleActionMenuComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                titleText = "Screen C",
                backText = "Back",
                regularActions = {
                    TopBarOneVisibleActionMenuExample(
                        actionCallback = {}
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarHamburgerMenuComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                titleText = "Screen D",
                backText = "Back",
                hamburgerMenuActions = {
                    TopBarHamburgerMenuExample(
                        actionCallback = {}
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarHamburgerPlusActionComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            SmallTopAppBar(
                titleText = "Screen E",
                hamburgerMenuActions = {
                    TopBarHamburgerMenuExample(
                        actionCallback = {}
                    )
                },
                regularActions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Content description text"
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun TopBarHamburgerMenuExample(
    modifier: Modifier = Modifier,
    actionCallback: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        IconButton(
            onClick = {
                expanded = true
                actionCallback()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.hamburger_menu_icon),
                contentDescription = "Content description text"
            )
        }
    }
}

@Composable
private fun TopBarRegularMenuExample(
    actionOneCallback: () -> Unit,
    actionTwoCallback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Content description text"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Action One") },
                onClick = {
                    actionOneCallback()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Action Two") },
                onClick = {
                    actionTwoCallback()
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun TopBarOneVisibleActionMenuExample(
    modifier: Modifier = Modifier,
    actionCallback: () -> Unit
) {
    Reference(
        text = "Action 1",
        onClick = actionCallback,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(all = ZcashTheme.dimens.spacingDefault)
    )
}

@Composable
@Suppress("LongParameterList", "LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
fun SmallTopAppBar(
    modifier: Modifier = Modifier,
    restoringLabel: String? = null,
    titleText: String? = null,
    showTitleLogo: Boolean = false,
    backText: String? = null,
    backContentDescriptionText: String? = null,
    onBack: (() -> Unit)? = null,
    hamburgerMenuActions: (@Composable RowScope.() -> Unit)? = null,
    regularActions: (@Composable RowScope.() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var restoringSpacerHeight: Dp = 0.dp

                if (titleText != null) {
                    Text(
                        text = titleText.uppercase(),
                        style = SecondaryTypography.headlineSmall
                    )
                    restoringSpacerHeight = ZcashTheme.dimens.spacingTiny
                } else if (showTitleLogo) {
                    Icon(
                        painter = painterResource(id = R.drawable.zashi_text_logo),
                        contentDescription = null,
                        modifier = Modifier.height(ZcashTheme.dimens.topAppBarZcashLogoHeight)
                    )
                    restoringSpacerHeight = ZcashTheme.dimens.spacingSmall
                }

                if (restoringLabel != null) {
                    Spacer(modifier = Modifier.height(restoringSpacerHeight))

                    @Suppress("MagicNumber")
                    Text(
                        text = restoringLabel.uppercase(),
                        style = ZcashTheme.extendedTypography.restoringTopAppBarStyle,
                        color = ZcashTheme.colors.restoringTopAppBarColor,
                        modifier = Modifier.fillMaxWidth(0.75f),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            backText?.let {
                Box(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .clip(RoundedCornerShape(ZcashTheme.dimens.regularRippleEffectCorner))
                            .clickable { onBack?.run { onBack() } }
                ) {
                    Row(
                        modifier = Modifier.padding(all = ZcashTheme.dimens.spacingDefault),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = backContentDescriptionText
                        )
                        Spacer(modifier = Modifier.size(size = ZcashTheme.dimens.spacingSmall))
                        Text(text = backText.uppercase())
                    }
                }
            }
        },
        actions = {
            regularActions?.invoke(this)
            hamburgerMenuActions?.invoke(this)
        },
        modifier =
            Modifier
                .testTag(CommonTag.TOP_APP_BAR)
                .then(modifier)
    )
}
