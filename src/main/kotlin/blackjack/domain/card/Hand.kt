package blackjack.domain.card

class Hand(private val cardList: MutableList<Card> = mutableListOf()) {

    fun addCardToHand(card: Card): Boolean {
        if (canAdd())
            return cardList.add(card)
        return false
    }

    fun canAdd(): Boolean = minValueOfHand() < VALUE_OF_WIN && !isBlackJackHand()

    fun getCardListInHand() = cardList.toList()

    fun getMakeableScores(): List<Int> {
        if (isBlackJackHand())
            return listOf(VALUE_OF_WIN)
        val numberOfAces = getNumberOfAceCards()
        val valueWithoutAces = getCardsWithoutAce().sumOf { it.cardValue }
        val ableValues = (numberOfAces downTo 0)
            .map { ACE_VALUE * it + ACE_VALUE_ALTERNATIVE * (numberOfAces - it) }
            .map { it + valueWithoutAces }
            .toHashSet()
        return ableValues.toList().filter { it <= VALUE_OF_WIN }
    }

    fun getMaxValue(): Int {
        val numberOfAces = getNumberOfAceCards()
        val valueWithoutAces = getCardsWithoutAce().sumOf { it.cardValue }
        return valueWithoutAces + ACE_VALUE_ALTERNATIVE * numberOfAces
    }

    fun isBusted() = (getMakeableScores().minOrNull() ?: Int.MAX_VALUE) > VALUE_OF_WIN

    private fun minValueOfHand(): Int = cardList.sumOf { it.cardValue }

    fun isBlackJackHand(): Boolean {
        val numberOfAces = getNumberOfAceCards()
        val sumOfCardWithoutAce = getCardsWithoutAce().sumOf { it.cardValue }

        val leftValueForAces = VALUE_OF_WIN - sumOfCardWithoutAce
        val actualNeedAces = leftValueForAces % ACE_VALUE_ALTERNATIVE + leftValueForAces / ACE_VALUE_ALTERNATIVE

        return actualNeedAces == numberOfAces
    }

    private fun getCardsWithoutAce() = cardList.filter { it.cardValue != ACE_VALUE }

    private fun getNumberOfAceCards() = cardList.filter { it.cardValue == ACE_VALUE }.size

    companion object {
        const val VALUE_OF_WIN = 21
        private const val ACE_VALUE = 1
        private const val ACE_VALUE_ALTERNATIVE = 11
    }
}
