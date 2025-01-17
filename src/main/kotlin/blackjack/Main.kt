package blackjack

import blackjack.domain.BlackjackGame
import blackjack.domain.player.Bet
import blackjack.domain.player.Player
import blackjack.domain.player.PlayerList
import blackjack.domain.player.PlayerName
import blackjack.ui.InputView.readInputForBet
import blackjack.ui.InputView.readInputForMoreCard
import blackjack.ui.InputView.readInputForPlayer
import blackjack.ui.ResultView
import blackjack.ui.ResultView.printAskAmountOfBet
import blackjack.ui.ResultView.printAskTakeMoreCard
import blackjack.ui.ResultView.printDealerHitResult
import blackjack.ui.ResultView.printPlayerHand
import blackjack.ui.ResultView.printPlayerResult
import blackjack.ui.ResultView.printTotalResultByPlayerResult

fun main() {
    val playerNameList = getPlayerNameList()
    val betList = getBetList(playerNameList)
    val blackjackGame = initBlackjackGame(playerNameList, betList)
    playBlackjackGame(blackjackGame)
    endBlackjackGame(blackjackGame)
}

fun getPlayerNameList(): List<PlayerName> {
    ResultView.printRequestPlayerNames()
    return readInputForPlayer()
}

fun getBetList(playerNameList: List<PlayerName>): List<Bet> =
    playerNameList.map {
        printAskAmountOfBet(it)
        readInputForBet()
    }

fun initBlackjackGame(playerNameList: List<PlayerName>, betList: List<Bet>): BlackjackGame {
    val blackjackGame = BlackjackGame(PlayerList.createPlayerList(playerNameList, betList))
    ResultView.printAddCardsForInit(playerNameList.joinToString(", "))
    printPlayerHand(blackjackGame.getDealer())
    blackjackGame.getGamerList()
        .forEach(::printPlayerHand)
    return blackjackGame
}

fun playBlackjackGame(blackjackGame: BlackjackGame) {
    blackjackGame
        .getGamerList()
        .forEach(blackjackGame::checkUserCardAddable)
    blackjackGame.playDealerTurn()
    printPlayResult(blackjackGame)
}

fun endBlackjackGame(blackjackGame: BlackjackGame) {
    val playerList = blackjackGame.getGameResultList()
    printTotalResultByPlayerResult(playerList)
}

fun printPlayResult(blackjackGame: BlackjackGame) {
    printPlayerResult(blackjackGame.getDealer())
    blackjackGame.getGamerList().forEach(::printPlayerResult)
}

fun BlackjackGame.checkUserCardAddable(player: Player) {
    if (player.isHandAddable())
        takeMoreCard(player)
}

fun BlackjackGame.playDealerTurn() {
    var hitCount = 0
    while (isDealerCardAddable()) {
        hitCount++
        addCardToDealer()
    }
    if (hitCount > 0)
        printDealerHitResult(hitCount)
}

fun BlackjackGame.takeMoreCard(player: Player) {
    printAskTakeMoreCard(player)
    val result = readInputForMoreCard()
    if (result) {
        addCardToPlayer(player)
        printPlayerHand(player)
        return checkUserCardAddable(player)
    }
    printPlayerHand(player)
}
