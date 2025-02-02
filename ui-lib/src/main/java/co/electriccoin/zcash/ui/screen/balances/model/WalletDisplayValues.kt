package co.electriccoin.zcash.ui.screen.balances.model

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import cash.z.ecc.android.sdk.Synchronizer
import cash.z.ecc.android.sdk.model.FiatCurrencyConversionRateState
import cash.z.ecc.android.sdk.model.MonetarySeparators
import cash.z.ecc.android.sdk.model.PercentDecimal
import cash.z.ecc.android.sdk.model.toFiatCurrencyState
import cash.z.ecc.android.sdk.model.toZecString
import co.electriccoin.zcash.ui.R
import co.electriccoin.zcash.ui.common.extension.toKotlinLocale
import co.electriccoin.zcash.ui.common.model.WalletSnapshot
import co.electriccoin.zcash.ui.common.model.spendableBalance
import co.electriccoin.zcash.ui.common.model.totalBalance

data class WalletDisplayValues(
    val progress: PercentDecimal,
    val zecAmountText: String,
    val statusText: String,
    val fiatCurrencyAmountState: FiatCurrencyConversionRateState,
    val fiatCurrencyAmountText: String
) {
    companion object {
        @Suppress("MagicNumber", "LongMethod")
        internal fun getNextValues(
            context: Context,
            walletSnapshot: WalletSnapshot,
            isUpdateAvailable: Boolean = false,
            isDetailedStatus: Boolean = false,
        ): WalletDisplayValues {
            var progress = PercentDecimal.ZERO_PERCENT
            val zecAmountText = walletSnapshot.totalBalance().toZecString()
            var statusText = ""
            // TODO [#578]: Provide Zatoshi -> USD fiat currency formatting
            // TODO [#578]: https://github.com/Electric-Coin-Company/zcash-android-wallet-sdk/issues/578
            // We'll ideally provide a "fresh" currencyConversion object here
            val fiatCurrencyAmountState =
                walletSnapshot.spendableBalance().toFiatCurrencyState(
                    null,
                    Locale.current.toKotlinLocale(),
                    MonetarySeparators.current(java.util.Locale.getDefault())
                )
            var fiatCurrencyAmountText = getFiatCurrencyRateValue(context, fiatCurrencyAmountState)

            when (walletSnapshot.status) {
                Synchronizer.Status.SYNCING -> {
                    progress = walletSnapshot.progress
                    // We add "so far" to the amount
                    if (fiatCurrencyAmountState != FiatCurrencyConversionRateState.Unavailable) {
                        fiatCurrencyAmountText =
                            context.getString(
                                R.string.balances_status_syncing_amount_suffix,
                                fiatCurrencyAmountText
                            )
                    }
                    statusText = context.getString(R.string.balances_status_syncing)
                }
                Synchronizer.Status.SYNCED -> {
                    statusText =
                        if (isUpdateAvailable) {
                            context.getString(
                                R.string.balances_status_update,
                                context.getString(R.string.app_name)
                            )
                        } else {
                            context.getString(R.string.balances_status_synced)
                        }
                }
                Synchronizer.Status.DISCONNECTED -> {
                    if (isDetailedStatus) {
                        statusText =
                            context.getString(
                                R.string.balances_status_error_detailed,
                                context.getString(R.string.balances_status_error_detailed_connection)
                            )
                    } else {
                        statusText =
                            context.getString(
                                R.string.balances_status_error_simple,
                                context.getString(R.string.app_name)
                            )
                    }
                }
                Synchronizer.Status.STOPPED -> {
                    if (isDetailedStatus) {
                        statusText = context.getString(R.string.balances_status_detailed_stopped)
                    } else {
                        statusText = context.getString(R.string.balances_status_syncing)
                    }
                }
            }

            // More detailed error message
            walletSnapshot.synchronizerError?.let {
                if (isDetailedStatus) {
                    statusText =
                        context.getString(
                            R.string.balances_status_error_detailed,
                            walletSnapshot.synchronizerError.getCauseMessage()
                                ?: context.getString(R.string.balances_status_error_detailed_unknown)
                        )
                } else {
                    statusText =
                        context.getString(
                            R.string.balances_status_error_simple,
                            context.getString(R.string.app_name)
                        )
                }
            }

            return WalletDisplayValues(
                progress = progress,
                zecAmountText = zecAmountText,
                statusText = statusText,
                fiatCurrencyAmountState = fiatCurrencyAmountState,
                fiatCurrencyAmountText = fiatCurrencyAmountText
            )
        }
    }
}

private fun getFiatCurrencyRateValue(
    context: Context,
    fiatCurrencyAmountState: FiatCurrencyConversionRateState
): String {
    return fiatCurrencyAmountState.let { state ->
        when (state) {
            is FiatCurrencyConversionRateState.Current -> state.formattedFiatValue
            is FiatCurrencyConversionRateState.Stale -> state.formattedFiatValue
            is FiatCurrencyConversionRateState.Unavailable -> {
                context.getString(R.string.fiat_currency_conversion_rate_unavailable)
            }
        }
    }
}
