package genesischallenge.data.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun configureHttpClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(
            Json {
                //configuring the HttpClient to ignore json key not in use by the logic of the code
                ignoreUnknownKeys = true
            }
        )
    }
}
