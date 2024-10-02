package com.ktm.ktinder.domain.entity

class Contact(
    val name: String,
    val age: Int,
    val desc: String,
    val imageUrl: String,
) {

    override fun toString(): String {
        return "$name, $age"
    }
}