package ru.playzone.utils

fun String.isValidEmail(): Boolean {
    return this.contains("@")
}