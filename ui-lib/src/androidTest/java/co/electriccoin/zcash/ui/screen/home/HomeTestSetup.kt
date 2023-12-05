package co.electriccoin.zcash.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import co.electriccoin.zcash.ui.design.theme.ZcashTheme
import co.electriccoin.zcash.ui.screen.home.model.WalletSnapshot
import co.electriccoin.zcash.ui.screen.home.view.Home
import java.util.concurrent.atomic.AtomicInteger

class HomeTestSetup(
    private val composeTestRule: ComposeContentTestRule,
    private val walletSnapshot: WalletSnapshot,
    private val isShowFiatConversion: Boolean
) {
    private val onSettingsCount = AtomicInteger(0)
    private val onReceiveCount = AtomicInteger(0)
    private val onSendCount = AtomicInteger(0)
    private val onHistoryCount = AtomicInteger(0)

    fun getOnSettingsCount(): Int {
        composeTestRule.waitForIdle()
        return onSettingsCount.get()
    }

    fun getOnReceiveCount(): Int {
        composeTestRule.waitForIdle()
        return onReceiveCount.get()
    }

    fun getOnSendCount(): Int {
        composeTestRule.waitForIdle()
        return onSendCount.get()
    }

    fun getOnHistoryCount(): Int {
        composeTestRule.waitForIdle()
        return onHistoryCount.get()
    }

    fun getWalletSnapshot(): WalletSnapshot {
        composeTestRule.waitForIdle()
        return walletSnapshot
    }

    @Composable
    @Suppress("TestFunctionName")
    fun DefaultContent() {
        Home(
            walletSnapshot,
            isUpdateAvailable = false,
            isKeepScreenOnDuringSync = false,
            isFiatConversionEnabled = isShowFiatConversion,
            goSettings = {
                onSettingsCount.incrementAndGet()
            },
            goReceive = {
                onReceiveCount.incrementAndGet()
            },
            goSend = {
                onSendCount.incrementAndGet()
            },
            goHistory = {
                onHistoryCount.incrementAndGet()
            },
        )
    }

    fun setDefaultContent() {
        composeTestRule.setContent {
            ZcashTheme {
                DefaultContent()
            }
        }
    }
}
