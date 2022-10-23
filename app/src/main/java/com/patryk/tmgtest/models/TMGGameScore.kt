package com.patryk.tmgtest.models

// We use a pre-sort/filter index as a "key" to the item as there is no provided key in the sample data
// perhaps the idea is to add an ID but i chose to just use the sample data model in the spirit of keeping it harder
data class TMGGameScore(val winningScore: TMGIndividualScore, val losingScore: TMGIndividualScore, val index: Int) {
    fun isValid(): Boolean = winningScore.isValid() && losingScore.isValid()
}