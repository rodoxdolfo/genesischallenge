package genesischallenge

import genesischallenge.Controller.getWalletPerformance
import kotlinx.coroutines.runBlocking
import genesischallenge.utils.*

fun main() = runBlocking {
    try {
        val walletPerformance = getWalletPerformance("/wallet.csv")

        println("total=${walletPerformance.totalValue.roundTo2Decimal()},best_asset=${walletPerformance.bestPerformingAsset.symbol},best_performance=${walletPerformance.bestPerformingAsset.performance.roundTo2Decimal()},worst_asset=${walletPerformance.worstPerformingAsset.symbol},worst_performance=${walletPerformance.worstPerformingAsset.performance.roundTo2Decimal()}")
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    }
}


