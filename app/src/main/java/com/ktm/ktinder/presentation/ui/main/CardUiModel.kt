package com.ktm.ktinder.presentation.ui.main

data class CardUiModel(
    val name: String,
    val age: Int,
    val desc: String,
    val imageUrl: String,
    val bgColorHexCode: String
) {

    override fun toString(): String {
        return "$name, $age"
    }
}