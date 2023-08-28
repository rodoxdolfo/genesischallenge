package genesischallenge.usecase

import genesischallenge.data.repository.WalletRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WalletReader : KoinComponent {
    private val walletRepository by inject<WalletRepository>()
    fun readWallet(filePath: String) = walletRepository.readWalletFile(filePath)
}
