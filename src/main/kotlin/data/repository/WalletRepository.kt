package genesischallenge.data.repository

import genesischallenge.domain.Asset
import java.io.FileNotFoundException

interface WalletRepository {
    fun readWalletFile(filePath: String): List<Asset>
}

class WalletRepositoryImpl : WalletRepository {
    override fun readWalletFile(filePath: String): List<Asset> {

        val lines = object {}.javaClass.getResourceAsStream(filePath)?.bufferedReader()?.use { reader ->
            reader.readLines()
        } ?: throw FileNotFoundException("Wallet file not found: $filePath")

        //dropping the first line to not add the header of the CSV
        return lines.drop(1).map { line ->
            val fields: List<String> = line.split(",")
            Asset(
                symbol = fields[0],
                quantity = fields[1].toDouble(),
                priceUsd = fields[2].toDouble()
            )
        }

    }
}
