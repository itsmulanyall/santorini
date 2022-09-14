package it.polimi.ingsw.client.view.gui;


import it.polimi.ingsw.client.view.ClientInfo.GameInfo;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.ForBoardGame.ControllerBoard;
import it.polimi.ingsw.client.view.gui.ForCards.ControllerChooseDeck;
import it.polimi.ingsw.client.view.gui.ForCards.ControllerThreePlayersChooseCard;
import it.polimi.ingsw.client.view.gui.ForCards.ControllerTwoPlayersChooseCard;
import it.polimi.ingsw.client.view.gui.ForEndGame.ControllerEndGame;
import it.polimi.ingsw.client.view.gui.ForNumberOfPlayers.ControllerChooseNumberOfPlayers;
import it.polimi.ingsw.client.view.gui.ForUsername.ControllerUsername;
import it.polimi.ingsw.client.view.gui.ForUsersInfoTableView.UsersInfoTableView;
import it.polimi.ingsw.client.view.gui.ForWorkersColor.ControllerChooseColorForWorkers;
import it.polimi.ingsw.shared.message.*;
import it.polimi.ingsw.shared.model.CardDetails;
import it.polimi.ingsw.shared.model.GodCards;
import it.polimi.ingsw.shared.model.PlayerDetails;
import it.polimi.ingsw.shared.socket.Client;
import it.polimi.ingsw.shared.utils.CircularList;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;





/**
 * This class represents the view if the player chooses to use the GUI interface. It extends the abstract
 * class View. It is held accountable for the game's flow on the client side.
 * It is both observer and observable to the client class
 */



public class ViewGUI extends View implements Runnable {

    private Client client;

    private ClientMessage response;

    private final Client.Unlock senderLock;

    private final Client.Unlock guiResponseLock;

    private ArrayList<CardDetails> handToShow;

    private ArrayList<CardDetails> handForTable;

    private ControllerUsername controllerUsername;

    Scene chooseUsernameScene;

    private ControllerChooseNumberOfPlayers controllerChooseNumberOfPlayers;

    Scene chooseNumbOfPlayersScene;

    private ControllerChooseDeck controllerChooseDeck;

    Scene chooseDeckScene;

    private ControllerThreePlayersChooseCard controllerThreePlayersChooseCard;

    Scene chooseCard3PlayersScene;

    private ControllerTwoPlayersChooseCard controllerTwoPlayersChooseCard;

    Scene chooseCard2PlayersScene;

    private ControllerEndGame controllerEndGame;

    Scene chooseFirstPlayerScene;

    private ControllerChooseColorForWorkers controllerChooseColorForWorkers;

    Scene chooseColorScene;

    private UsersInfoTableView usersInfoTableView;

    Scene userInfoScene;

    private ControllerBoard controllerBoard;

    Scene boardScene;

    private ControllerEndGame controllerEndGamE;
    Scene endGameScene;

    private final Stage window;


    /**
     * ViewGUI's constructor
     * @param window
     */
    public ViewGUI(Stage window){
        this.window = window;
        this.response = new ClientMessage(null, null, null);
        this.senderLock = new Client.Unlock();
        this.guiResponseLock = new Client.Unlock();
        gameInfo = new GameInfo();
        playersInGame = new CircularList<>();
        changeSceneChooseUsername();
        changeSceneChooseNumberOfPlayers();
        changeSceneSelectDeck();
        changeSceneChooseCard3Players();
        changeSceneChooseCard2Players();
        changeSceneChooseColor();
        changeSceneUserInfoTableView();
        changeScenetoBoard();
    }



    /**
     * changes to scene in which the player chooses the username
     */


     public void changeSceneChooseUsername(){
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/provaUsername.fxml"));
       try {
           Parent boardPaneParent = loader.load();
           chooseUsernameScene = new Scene(boardPaneParent);
           controllerUsername = loader.getController();
           controllerUsername.setViewGUI(this);
       } catch (IOException e) {
           e.printStackTrace();
       }
     }





    /**
     * changes to scene in which the first connected player chooses the number of players
     */

    public void changeSceneChooseNumberOfPlayers() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/chooseNumberOfPlayers.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            chooseNumbOfPlayersScene = new Scene(anchorPaneParent);
            controllerChooseNumberOfPlayers = loader.getController();
            controllerChooseNumberOfPlayers.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeSceneToEndGame() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/EndGame.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            endGameScene = new Scene(anchorPaneParent);
            controllerEndGamE = loader.getController();
            controllerEndGamE.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /**
     * changes to the scene in which the deck is selected
     */

    public void changeSceneSelectDeck() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/selectDeck.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            chooseDeckScene = new Scene(anchorPaneParent);
            controllerChooseDeck = loader.getController();
            controllerChooseDeck.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /**
     * changes to the scene in which 3 players choose a card
     */
    public void changeSceneChooseCard3Players() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/selectCard3Players.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            chooseCard3PlayersScene = new Scene(anchorPaneParent);
            controllerThreePlayersChooseCard = loader.getController();
            controllerThreePlayersChooseCard.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * changes to the scene in which 2 players choose a card
     */

    public void changeSceneChooseCard2Players(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/selectCard2Players.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            chooseCard2PlayersScene = new Scene(anchorPaneParent);
            controllerTwoPlayersChooseCard = loader.getController();
            controllerTwoPlayersChooseCard.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    /**
     * changes to the scene in which the players choose a color
     */

    public void changeSceneChooseColor(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/provaChooseColor.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            chooseColorScene = new Scene(anchorPaneParent);
            controllerChooseColorForWorkers = loader.getController();
            controllerChooseColorForWorkers.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * changes to the scene in which the user table info is shown
     */


    public void changeSceneUserInfoTableView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/usersInfoTableView.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            userInfoScene = new Scene(anchorPaneParent);
            usersInfoTableView = loader.getController();
            usersInfoTableView.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /**
     * changes to the scene in which the board is shown
     */

    public void changeScenetoBoard(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/provaBoard.fxml"));
        try {
            Parent anchorPaneParent = loader.load();
            boardScene = new Scene(anchorPaneParent);
            controllerBoard = loader.getController();
            controllerBoard.setViewGUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Message message) {
        if(message.getMessageSender() == MessageSender.CONTROLLER){


            /**
             * Messages which are received from the Controller based on the event
             */

            switch(message.getMessageEvent()){

                case USERNAME:
                    playersUsername = ((ControllerMessage)message).getPlayersInGame();
                    if (username==null){
                        chooseUsername();
                        waitingForClient();
                        String res = controllerUsername.getUsername();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.USERNAME,MessageSender.CONTROLLER);
                        response.setUsername(res);
                    } else{
                        gameInfo.setPlayersInGame(playersInGame);
                        showUsersInfoTable();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.USERNAME, MessageSender.CONTROLLER);
                    }
                    break;

                case NUMB_PLAYERS:
                    chooseNumbOfPlayers();
                    waitingForClient();
                    int num = controllerChooseNumberOfPlayers.getSelectedNumber();
                    response=new ClientMessage(MessageTypes.RESPONSE,Events.NUMB_PLAYERS,MessageSender.CONTROLLER);
                    response.setNumbPlayers(num);
                    break;
            }
        }
        else if(message.getMessageSender() == MessageSender.MODEL){



            /**
             * Messages which are received from the Model based on the event
             */

                switch(message.getMessageEvent()){

                    case USERNAME:
                        if (username == null) {
                            username = ((PlayerDetailsModelMessage)message).getPlayerDetails().getUsername();
                            response = new ClientMessage(MessageTypes.INFORMATION, Events.USERNAME, MessageSender.CONTROLLER);
                            gameInfo.setUsername(username);
                        }
                        break;

                    case NUMB_PLAYERS:
                        numberOfPlayers = ((ModelMessage) message).getNumberOfPlayersConfirmed();
                        gameInfo.setNumberOfPlayers(numberOfPlayers);
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.NUMB_PLAYERS, MessageSender.CONTROLLER);
                        break;

                    case DECK:
                        handConfirmed =((ModelMessage) message).getHandConfirmed();
                        for (CardDetails c: handConfirmed){
                            gameInfo.getHand().add(c.getCardName());
                        }
                        hand= gameInfo.getHand();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.DECK, MessageSender.SETUP_CONTROLLER);
                        break;

                    case CARD:
                        playerInfo = ((PlayerDetailsModelMessage)message).getPlayerDetails();
                        userNameMessage = playerInfo.getUsername();
                        for (int i = 0; i < gameInfo.getNumberOfPlayers() ; i++) {
                            if (gameInfo.getPlayersInGame().get(i).getUsername().equals(userNameMessage)){
                                gameInfo.getPlayersInGame().remove(i);
                                gameInfo.getPlayersInGame().add(i,playerInfo);
                                gameInfo.getHand().remove(playerInfo.getCard().getCardName());
                            }
                        }
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.CARD, MessageSender.SETUP_CONTROLLER);
                        break;

                    case COLOR:
                        /*receiving message*/
                        playerInfo = ((PlayerDetailsModelMessage)message).getPlayerDetails();
                        userNameMessage = playerInfo.getUsername();
                        /*setting game info*/
                        for (int i = 0; i < gameInfo.getNumberOfPlayers() ; i++) {
                            if (gameInfo.getPlayersInGame().get(i).getUsername().equals(userNameMessage)){
                                gameInfo.getPlayersInGame().remove(i);
                                gameInfo.getPlayersInGame().add(i,playerInfo);
                                gameInfo.getColorList().remove(playerInfo.getColor().getColorName());
                            }
                        }
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.COLOR, MessageSender.SETUP_CONTROLLER);
                        break;

                    case BOARD:
                        board = ((BoardModelMessage)message).getBoardConfirmed();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.BOARD, MessageSender.SETUP_CONTROLLER);
                        break;

                    case AVAILABLE_POSITION:
                        availablePositions = ((AvailablePosModelMessage)message).getAvailablePositionConfirmed();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.AVAILABLE_POSITION, MessageSender.SETUP_CONTROLLER);
                        break;

                    case PLACE_WORKER:
                        availablePositions = ((AvailablePosModelMessage)message).getAvailablePositionConfirmed();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.PLACE_WORKER, MessageSender.SETUP_CONTROLLER);
                        break;

                    case WORKER_DETAILS:
                        playerInfo = ((PlayerDetailsModelMessage)message).getPlayerDetails();
                        userNameMessage = playerInfo.getUsername();
                        for (int i = 0; i < gameInfo.getNumberOfPlayers() ; i++) {
                            if (gameInfo.getPlayersInGame().get(i).getUsername().equals(userNameMessage)) {
                                gameInfo.getPlayersInGame().remove(i);
                                gameInfo.getPlayersInGame().add(i,playerInfo);
                            }
                        }
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.WORKER_DETAILS, MessageSender.SETUP_CONTROLLER);
                        break;

                    case SHOW_POSSIBLE_ACTIONS:
                        userNameMessage = message.getPlayerUsername();
                        if (userNameMessage.equals(username)) {
                            possibleActions = ((PossibleActionsModelMessage) message).getPossibleActionConfirmed();
                        }
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.SHOW_POSSIBLE_ACTIONS, MessageSender.TURN_MANAGING_CONTROLLER);
                        break;
                    case ASK_TO_SELECT_ACTION:
                        board = ((BoardModelMessage) message).getBoardConfirmed();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.ASK_TO_SELECT_ACTION, MessageSender.TURN_MANAGING_CONTROLLER);
                        break;

                    case REMOVE_LOSER:
                        board = ((BoardModelMessage) message).getBoardConfirmed();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.REMOVE_LOSER, MessageSender.TURN_MANAGING_CONTROLLER);
                        break;

                    case LOSS:
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.LOSS, MessageSender.TURN_MANAGING_CONTROLLER);
                        break;

                    case END_GAME:
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.END_GAME, MessageSender.TURN_MANAGING_CONTROLLER);
                        break;
                }
        }

        else if(message.getMessageSender() == MessageSender.SETUP_CONTROLLER){


            /**
             * Messages which are received from the SetupController based on the event
             */



            switch (message.getMessageEvent()) {
                case CHALLENGER:
                    challenger = ((ControllerMessage) message).getChallenger();
                    numberOfPlayers = ((ControllerMessage) message).getNumbPlayers();
                    playersUsername = ((ControllerMessage) message).getPlayersInGame();
                    for (String p : playersUsername) {
                        playerInfo = new PlayerDetails(p, null, null, null);
                        playersInGame.add(playerInfo);
                    }
                    gameInfo.setPlayersInGame(playersInGame);
                    if (gameInfo.getNumberOfPlayers() == 0) {
                        gameInfo.setNumberOfPlayers(numberOfPlayers);
                    }
                    response = new ClientMessage(MessageTypes.INFORMATION, Events.CHALLENGER, MessageSender.SETUP_CONTROLLER);
                    break;

                case DECK:
                    if (username.equals(challenger)){
                        chooseDeck();
                        waitingForClient();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.DECK , MessageSender.SETUP_CONTROLLER);
                        response.setHand(controllerChooseDeck.getHandGui());
                    }else{
                        showUsersInfoTable();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.DECK , MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case CARD:
                    handToShow = new ArrayList<>(gameInfo.getHand().size());
                    for (String c : gameInfo.getHand()){
                        handToShow.add(gameInfo.getFullDeck().get(GodCards.valueOf(c.toUpperCase()).ordinal()));
                    }
                    if (handToShow.size()==numberOfPlayers)
                        handForTable = new ArrayList<>(handToShow);
                    if(message.getPlayerUsername().equals(username)){
                        int indexOfCard;
                        if (hand.size()==3){
                            chooseCard3Players();
                            waitingForClient();
                            indexOfCard = controllerThreePlayersChooseCard.indexCard();
                        }else if (hand.size()==2){
                            chooseCard2Players();
                            waitingForClient();
                            indexOfCard = controllerTwoPlayersChooseCard.indexCard();
                        }else{
                            indexOfCard = 0;
                        }
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.CARD, MessageSender.SETUP_CONTROLLER);
                        response.setCard(indexOfCard);
                        response.setUsername(username);
                    }else{
                        usersInfoTableView.setCardDetails(handForTable);
                        showUsersInfoTable();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.CARD , MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case FIRST_PLAYER:
                    if (username.equals(challenger)){
                        usersInfoTableView.forTheFirstPlayer();
                        showUsersInfoTable();
                        waitingForClient();
                        firstPlayer = usersInfoTableView.getFirstPlayerName();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.FIRST_PLAYER, MessageSender.SETUP_CONTROLLER);
                        response.setFirstPlayer(firstPlayer);
                    }else{
                        usersInfoTableView.setCardDetails(handForTable);
                        showUsersInfoTable();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.FIRST_PLAYER, MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case COLOR:
                    if (message.getPlayerUsername().equals(username)) {
                        chooseColors();
                        waitingForClient();
                        String color = controllerChooseColorForWorkers.getColor();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.COLOR, MessageSender.SETUP_CONTROLLER);
                        response.setColor(color);
                    }else{
                        usersInfoTableView.setCardDetails(handForTable);
                        showUsersInfoTable();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.COLOR, MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case PLACE_WORKER:
                    controllerBoard.initButtonsOff();
                    if (message.getPlayerUsername().equals(username)){
                        controllerBoard.setForWorkers(availablePositions);
                        showBoard();
                        waitingForClient();
                        int index = controllerBoard.getPlaceWorkerAnswer();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.PLACE_WORKER, MessageSender.SETUP_CONTROLLER);
                        response.setSelectedPosition(index);
                    }
                    else{
                        showBoard();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.PLACE_WORKER, MessageSender.SETUP_CONTROLLER);
                    }
                    break;
            }
        }

        else if(message.getMessageSender() == MessageSender.TURN_MANAGING_CONTROLLER){



            /**
             * Messages which are received from the TurnManagingController based on the event
             */



            switch(message.getMessageEvent()) {
                case SHOW_POSSIBLE_ACTIONS:
                    if (message.getPlayerUsername().equals(username)) {
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.SHOW_POSSIBLE_ACTIONS, MessageSender.TURN_MANAGING_CONTROLLER);
                    } else {
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.SHOW_POSSIBLE_ACTIONS, MessageSender.TURN_MANAGING_CONTROLLER);
                    }
                    break;

                case ASK_TO_SELECT_ACTION:
                    controllerBoard.initButtonsOff();
                    if (message.getPlayerUsername().equals(username)) {
                        controllerBoard.setActionsForWorkers(possibleActions);
                        showBoard();
                        waitingForClient();
                        int chosenAction = controllerBoard.getActionAnswer();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.ASK_TO_SELECT_ACTION, MessageSender.TURN_MANAGING_CONTROLLER);
                        response.setSelectedAction(chosenAction);
                    } else {
                        showBoard();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.PLACE_WORKER, MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case WIN:
                    if (message.getPlayerUsername().equals(username)){
                        controllerEndGamE.setWinnerId();
                        showEndGame();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.WIN, MessageSender.TURN_MANAGING_CONTROLLER);
                    }else{
                        controllerEndGamE.setLoserId();
                        showEndGame();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.WIN, MessageSender.TURN_MANAGING_CONTROLLER);
                    }
                    break;

                case LOSS:
                    if (message.getPlayerUsername().equals(username)){

                    }else{

                    }
                    for (PlayerDetails p: gameInfo.getPlayersInGame()){
                        if (p.getUsername().equals(message.getPlayerUsername())){
                            gameInfo.getPlayersInGame().remove(p);
                        }
                    }
                    response = new ClientMessage(MessageTypes.RESPONSE, Events.LOSS, MessageSender.TURN_MANAGING_CONTROLLER);
                    break;
            }

        }


            senderLock.unLock();
            guiResponseLock.reset();


    }


    /**
     * launches the username scene
     */

    public void chooseUsername(){
        Platform.runLater(()-> {
            controllerUsername.setUsernamePlayers(playersUsername);
            window.setScene(chooseUsernameScene);
            window.show();});
    }


    /**
     * launches the number of players scene
     */

    public void chooseNumbOfPlayers(){
        Platform.runLater(()-> {window.setScene(chooseNumbOfPlayersScene);
            window.show();});
    }


    /**
     * launches the deck scene
     */

    public void chooseDeck(){
        Platform.runLater(()->{
            controllerChooseDeck.setNumberOfCards(gameInfo.getNumberOfPlayers());
            controllerChooseDeck.setFullDeckToShow(gameInfo.getFullDeck());
            window.setScene(chooseDeckScene);
            window.show();});
    }





    /**
     * launches the scene in which 3 players choose the cards
     */

    public void chooseCard3Players(){
        Platform.runLater(()->{
            controllerThreePlayersChooseCard.setCardsToShow(handToShow);
            window.setScene(chooseCard3PlayersScene);
            window.show();});
    }



    /**
     * launches the scene in which 2 players choose the cards
     */

    public void chooseCard2Players(){
        Platform.runLater(()->{
            controllerTwoPlayersChooseCard.setCardsToShow(handToShow);
            window.setScene(chooseCard2PlayersScene);
            window.show();});
    }



    /**
     * launches the scene in which the colors are chosen
     */

    public void chooseColors(){
        Platform.runLater(()->{
            controllerChooseColorForWorkers.setColors(gameInfo.getColorList());
            window.setScene(chooseColorScene);
            window.show();});
    }



    /**
     * launches the scene in which the infoTable is shown
     */

    public void showUsersInfoTable(){
        Platform.runLater(()->{
            usersInfoTableView.setPlayersInfo(gameInfo.getPlayersInGame(),playersUsername);
            window.setScene(userInfoScene);
            window.show();
        });
    }




    /**
     * launches the scene in which the board is shown
     */

    public void showBoard(){
        Platform.runLater(()->{
            controllerBoard.setUsersInfo(playersInGame, username);
            controllerBoard.setCardsForTable(handForTable);
            controllerBoard.setBoardDetails(board);
            window.setScene(boardScene);
            window.show();
        });
    }


    public void showEndGame(){
        Platform.runLater(()->{
            window.setScene(endGameScene);
            window.show();
        });
    }




    public void waitingForClient(){
        try {
             guiResponseLock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void guiConfirm(){
        guiResponseLock.unLock();
    }


    /**
     * client setter
     * @param client
     */

    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * client getter
     * @return
     */

    public Client getClient() {
        return client;
    }


    /**
     * synchronization
     */

    @Override
    public void run() {
        while (true) {
            try {
                senderLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            notify(response);

            senderLock.reset();
        }
    }
}

