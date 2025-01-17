package blackjack

import blackjack.domain.BlackjackGame
import blackjack.domain.player.Bet
import blackjack.domain.player.PlayerList
import blackjack.domain.player.PlayerName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlackjackGameTest {
    @Test
    fun `BlackjackGame생성시에 추가한 참가자 목록을 이후에 받아 올 수 있다`() {
        val player1Name = PlayerName("player1")
        val player2Name = PlayerName("player2")
        val game = BlackjackGame(PlayerList.createPlayerList(listOf(player1Name, player2Name), List(2) { Bet() }))
        val predictedNumberOfPlayers = 2
        val playerList = game.getGamerList()
        assertThat(playerList.size).isEqualTo(predictedNumberOfPlayers)
        val nameList = playerList.map { it.getName() }
        assertThat(nameList).contains(player1Name, player2Name)
    }

    @Test
    fun `BlackjackGame 생성시 참가자에게 2장씩 카드를 나누어 주고 갯수를 확인 할 수 있다`() {
        val player1Name = PlayerName("player1")
        val player2Name = PlayerName("player2")
        val game = BlackjackGame(PlayerList.createPlayerList(listOf(player1Name, player2Name), List(2) { Bet() }))
        val player1 = game.getGamerList().find { it.getName() == player1Name }
        val player2 = game.getGamerList().find { it.getName() == player2Name }
        assertThat(player1).isNotNull
        assertThat(player2).isNotNull
        val predictedNumberOfCard = 2
        assertThat(player1!!.getCards().size).isEqualTo(predictedNumberOfCard)
        assertThat(player2!!.getCards().size).isEqualTo(predictedNumberOfCard)
    }

    @Test
    fun `BlackjackGame을 통해 참가자에게 랜덤한 카드를 추가 할 수 있다`() {
        val player1Name = PlayerName("player1")
        val game = BlackjackGame(PlayerList.createPlayerList(listOf(player1Name), listOf(Bet())))
        val player1 = game.getGamerList().find { it.getName() == player1Name }
        require(player1 != null)
        game.getGamerList().forEach { game.addCardToPlayer(it) }
        val predictedNumberOfCard = 3
        assertThat(player1.getCards().size).isEqualTo(predictedNumberOfCard)
    }
}
