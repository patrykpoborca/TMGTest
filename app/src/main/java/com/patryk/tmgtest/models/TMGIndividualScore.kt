package com.patryk.tmgtest.models

data class TMGIndividualScore(val name: String, val score: Int) {
    fun isValid(): Boolean = name.isNotBlank()
}