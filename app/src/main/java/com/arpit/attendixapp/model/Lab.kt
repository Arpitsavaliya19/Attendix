package com.arpit.attendixapp.model

data class Lab(
    val id: Int = 0,
    val name: String,
    val attended: Int = 0,
    val total: Int = 0,
    val colorHex: String = "#03A9F4"
) {
    val percentage: Float
        get() = if (total > 0) (attended.toFloat() / total.toFloat()) * 100f else 0f
}
