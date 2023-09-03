package genesischallenge.controller

import genesischallenge.domain.WalletPerformance
import genesischallenge.usecase.UpdateAssetPrice
import genesischallenge.usecase.WalletReader
import genesischallenge.usecase.calculatePerformance

class PerformanceController {
    suspend fun getWalletPerformance(filePath: String): WalletPerformance {
        //Getting assets list from the wallet file
        val assets = WalletReader().readWallet(filePath)

        //Getting updates prices for the assets
        val updatedAssets = UpdateAssetPrice().getUpdatedAssetPrice(assets)

        //Calculating asset performance
        return calculatePerformance(updatedAssets)
    }
}
