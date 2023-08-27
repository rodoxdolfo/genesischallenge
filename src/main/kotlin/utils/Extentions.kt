package genesischallenge.utils

import java.util.*

fun Double.roundTo2Decimal(): String {
    return String.format(Locale.ENGLISH,"%.2f", this)
}