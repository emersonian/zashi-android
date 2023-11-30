package co.electriccoin.zcash.ui.screen.debug.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.electriccoin.zcash.spackle.model.Index
import co.electriccoin.zcash.ui.design.component.Body
import co.electriccoin.zcash.ui.design.component.ChipIndexed
import co.electriccoin.zcash.ui.design.component.GradientSurface
import co.electriccoin.zcash.ui.design.component.Header
import co.electriccoin.zcash.ui.design.component.NavigationButton
import co.electriccoin.zcash.ui.design.component.PrimaryButton
import co.electriccoin.zcash.ui.design.component.SecondaryButton
import co.electriccoin.zcash.ui.design.component.TertiaryButton
import co.electriccoin.zcash.ui.design.theme.ZcashTheme

@Preview("DesignGuide")
@Composable
private fun ComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        DesignGuide()
    }
}

@Composable
// Allowing magic numbers since this is debug-only
@Suppress("MagicNumber")
fun DesignGuide() {
    GradientSurface {
        Column {
            Header(text = "H1")
            Body(text = "body")
            NavigationButton(onClick = { }, text = "Back")
            NavigationButton(onClick = { }, text = "Next")
            PrimaryButton(onClick = { }, text = "Primary button", outerPaddingValues = PaddingValues(24.dp))
            SecondaryButton(onClick = { }, text = "Secondary button", outerPaddingValues = PaddingValues(24.dp))
            TertiaryButton(onClick = { }, text = "Tertiary button", outerPaddingValues = PaddingValues(24.dp))
            ChipIndexed(Index(1), "edict")
        }
    }
}
