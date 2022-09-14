package it.polimi.ingsw.client.view.cli;


import it.polimi.ingsw.client.utils.CardsDetailJsonDeserializer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.ClientInfo.GameInfo;
import it.polimi.ingsw.shared.message.*;
import it.polimi.ingsw.shared.model.BuildLevel;
import it.polimi.ingsw.shared.model.CardDetails;
import it.polimi.ingsw.shared.model.PlayerDetails;
import it.polimi.ingsw.shared.socket.Client;
import it.polimi.ingsw.shared.utils.CircularList;
import it.polimi.ingsw.shared.utils.Point;

import java.util.ArrayList;
import java.util.Scanner;

import static it.polimi.ingsw.client.view.cli.CliConstants.*;


/**
 * This class represents the view if the player chooses to use the CLI interface. It extends the abstract
 * class View. It is held accountable for the game's flow on the client side.
 * It is both observer and observable to the client class
 */



public class ViewCLI extends View implements Runnable {

    private static Scanner scanner = null;

    private Client client;

    private final CliFramework cliFramework;

    private final Client.Unlock senderLock;





    /**
     * ViewCLI's constructor
     */

    public ViewCLI() {
        scanner = new Scanner(System.in);
        this.response = new ClientMessage(null, null, null);
        this.senderLock = new Client.Unlock();
        this.cliFramework = new CliFramework();
        gameInfo = new GameInfo();
    }

    @Override
    public void update(Message message) {
        if(message.getMessageSender() == MessageSender.CONTROLLER){


            /**
             * Messages which are received from the Controller based on the event
             */


            switch(message.getMessageEvent()){

                case USERNAME:
                    if (username==null){
                        playersUsername = ((ControllerMessage)message).getPlayersInGame();
                        print(GET_NAME);
                        String res=scanner.nextLine();
                        while(playersUsername.contains(res)){
                            print("Username already taken, insert another one");
                            res = scanner.nextLine();
                        }
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.USERNAME, MessageSender.CONTROLLER);
                        response.setUsername(res);}
                    else {
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.USERNAME, MessageSender.CONTROLLER);
                    }
                    break;

                case NUMB_PLAYERS:
                    cliFramework.printNumPlayersRequest();
                    int num = scanner.nextInt();
                    while (num<1||num>2){
                        print(NOT_A_VALID_MODE);
                        num = scanner.nextInt();
                    }
                    response=new ClientMessage(MessageTypes.RESPONSE, Events.NUMB_PLAYERS, MessageSender.CONTROLLER);
                    response.setNumbPlayers(num+1);
                    break;

            }
        } else if(message.getMessageSender() == MessageSender.MODEL){


            /**
             * Messages which are received from the Model based on the event
             */


            switch(message.getMessageEvent()){

                case USERNAME:
                    if (username == null) {
                        username = ((PlayerDetailsModelMessage)message).getPlayerDetails().getUsername();
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.USERNAME, MessageSender.CONTROLLER);
                        gameInfo = new GameInfo();
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
        } else if(message.getMessageSender() == MessageSender.SETUP_CONTROLLER){





            /**
             * Messages which are received from the SetupController based on the event
             */


            switch(message.getMessageEvent()) {

                case CHALLENGER:
                    challenger = ((ControllerMessage) message).getChallenger();
                    numberOfPlayers = ((ControllerMessage) message).getNumbPlayers();
                    playersUsername = ((ControllerMessage) message).getPlayersInGame();
                    playersInGame = new CircularList<>();
                    for (String p: playersUsername){
                        playerInfo= new PlayerDetails(p,null,null,null);
                        playersInGame.add(playerInfo);
                    }
                    gameInfo.setPlayersInGame(playersInGame);
                    //print("\nthe server will choose now a challenger:\n");
                    if(gameInfo.getNumberOfPlayers() == 0){
                        gameInfo.setNumberOfPlayers(numberOfPlayers);
                    }

                    response = new ClientMessage(MessageTypes.INFORMATION, Events.CHALLENGER, MessageSender.SETUP_CONTROLLER);
                    break;

                case DECK:
                    int[] selectedCards= new int[numberOfPlayers];
                    if (username.equals(challenger)){
                        ArrayList<CardDetails> deck = gameInfo.getFullDeck();
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printCardsSelection(deck, gameInfo.getNumberOfPlayers());
                        cliFramework.printScreen();
                        ArrayList<Integer> indexSelected = new ArrayList<>(gameInfo.getNumberOfPlayers());
                        int set=0;
                        do {

                            int cardIndex = -1;
                            AsyncReader as = read(getRegexFromOneToRange(deck.size()), 180);
                            if(as.isDone())
                                cardIndex = as.getInputAsInt();

                            if(!indexSelected.contains(cardIndex)){
                                indexSelected.add(cardIndex);
                                cliFramework.lightUpCardIndex(cardIndex);
                                cliFramework.printScreen();
                                set++;
                            }
                        } while (set<gameInfo.getNumberOfPlayers());
                        for (int i = 0; i <numberOfPlayers ; i++) {
                            selectedCards[i]=indexSelected.get(i)-1;
                        }
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.DECK , MessageSender.SETUP_CONTROLLER);
                        response.setHand(selectedCards);
                    }else{
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printEnemyTurn(challenger,WAITING_FOR_CHALLENGER_CHOOSE_DECK);
                        cliFramework.setCursorToPosition();
                        cliFramework.printScreen();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.DECK , MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case CARD:
                    if(message.getPlayerUsername().equals(username)){
                        ArrayList<CardDetails> cardInHand = new CardsDetailJsonDeserializer().getDetailedCards(gameInfo.getHand());


                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printCardsSelection(cardInHand,1);
                        cliFramework.printScreen();


                        int indexOfCard = 1;
                        AsyncReader as = read(getRegexFromOneToRange(handConfirmed.size()), 180);
                        if(as.isDone())
                            indexOfCard = as.getInputAsInt();

                        response = new ClientMessage(MessageTypes.RESPONSE, Events.CARD, MessageSender.SETUP_CONTROLLER);
                        response.setCard(indexOfCard-1);
                        response.setUsername(username);
                    }else{
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printEnemyTurn(message.getPlayerUsername(),WAITING_FOR_ENEMY_TO_CHOOSE_CARD);
                        cliFramework.printScreen();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.CARD , MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case FIRST_PLAYER:
                    if (username.equals(challenger)){
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printPlayerToChoose(playersUsername);
                        cliFramework.printScreen();


                        int firstPlayerNumber = 1;
                        AsyncReader as = read(getRegexFromOneToRange(playersUsername.size()), 180);
                        if(as.isDone())
                            firstPlayerNumber = as.getInputAsInt();

                        response = new ClientMessage(MessageTypes.RESPONSE, Events.FIRST_PLAYER, MessageSender.SETUP_CONTROLLER);
                        response.setFirstPlayer(playersUsername.get(firstPlayerNumber-1));
                    }else{
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printEnemyTurn(challenger,WAITING_FOR_CHALLENGER_CHOOSE_FIRST_PLAYER);
                        response = new ClientMessage(MessageTypes.INFORMATION, Events.FIRST_PLAYER, MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case COLOR:
                    if (message.getPlayerUsername().equals(username)) {
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printColorToChoose(gameInfo.getColorList());
                        cliFramework.printScreen();

                        int col = 1;
                        AsyncReader as = read(getRegexFromOneToRange(gameInfo.getColorList().size()), 60);
                        if(as.isDone())
                            col = as.getInputAsInt();


                        response = new ClientMessage(MessageTypes.RESPONSE, Events.COLOR, MessageSender.SETUP_CONTROLLER);
                        response.setColor(gameInfo.getColorList().get(col-1));
                    }else{
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                        cliFramework.printEnemyTurn(message.getPlayerUsername(),WAITING_FOR_ENEMY_TO_CHOOSE_COLOR);
                        cliFramework.printScreen();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.COLOR, MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case PLACE_WORKER:
                    Point pointToDraw;
                    cliFramework.updatePlayers(gameInfo.getPlayersInGame(),username);
                    for (int row = 0; row<5 ; row++) {
                        for (int col = 0; col<5 ; col++) {
                            pointToDraw = new Point(col,row);
                            if (board.getTile(row,col).hasWorker()){
                                cliFramework.removeWorker(pointToDraw);
                                cliFramework.drawWorker(board.getTile(row,col).getWorker().getColor(),pointToDraw);
                            }
                        }
                    }
                    if (message.getPlayerUsername().equals(username)){
                        cliFramework.printAvailablePosesPerWorker(availablePositions);
                        cliFramework.printScreen();
                        //showMessage("Type the position");

                        int pos = 1;

                        AsyncReader as = read(getRegexFromOneToRange(availablePositions.size()), 180);
                        if(as.isDone())
                            pos = as.getInputAsInt();


                        pos--;
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.PLACE_WORKER, MessageSender.SETUP_CONTROLLER);
                        response.setSelectedPosition(pos);
                    }
                    else{
                        cliFramework.printEnemyTurn(message.getPlayerUsername(),WAITING_FOR_ENEMY_TO_PLACE_WORKERS);
                        cliFramework.printScreen();
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
                        //showMessage(START_TURN);
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.SHOW_POSSIBLE_ACTIONS, MessageSender.TURN_MANAGING_CONTROLLER);
                    } else {
                        cliFramework.updatePlayers(gameInfo.getPlayersInGame(), username);
                        cliFramework.printEnemyTurn(message.getPlayerUsername(), WAITING_FOR_ENEMY_TO_FINISH_TURN);
                        cliFramework.printScreen();

                        response = new ClientMessage(MessageTypes.CONFIRM, Events.SHOW_POSSIBLE_ACTIONS, MessageSender.TURN_MANAGING_CONTROLLER);
                    }
                    break;

                case ASK_TO_SELECT_ACTION:
                    Point pointToDraw;
                    cliFramework.updatePlayers(gameInfo.getPlayersInGame(), username);
                    for (int row = 0; row < 5; row++) {
                        for (int col = 0; col < 5; col++) {
                            pointToDraw = new Point(col, row);
                            cliFramework.removeWorker(pointToDraw);
                            if (board.getTile(row, col).hasWorker()) {
                                cliFramework.drawWorker(board.getTile(row, col).getWorker().getColor(), pointToDraw);
                            }
                            if (board.getTile(row, col).getLevels().size() > 1) {
                                //cliFramework.removeLevel(pointToDraw);
                                if (board.getTile(row, col).getLevels().contains(BuildLevel.DOME)) {
                                    cliFramework.drawLevel(pointToDraw, BuildLevel.DOME);
                                } else {
                                    cliFramework.drawLevel(pointToDraw, BuildLevel.values()[board.getTile(row, col).getLevels().size() - 1]);
                                }
                            }
                        }
                    }
                    if (message.getPlayerUsername().equals(username)) {
                        cliFramework.printAllowedActions(possibleActions);
                        cliFramework.printScreen();
                        int chosenAction = 1;
                        String regex = getRegexFromOneToRange(possibleActions.size());
                        AsyncReader as = read(regex, 180);
                        if(as.isDone())
                            chosenAction = as.getInputAsInt();

                        chosenAction--;
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.ASK_TO_SELECT_ACTION, MessageSender.TURN_MANAGING_CONTROLLER);
                        response.setSelectedAction(chosenAction);
                    } else {
                        cliFramework.printEnemyTurn(message.getPlayerUsername(), WAITING_FOR_ENEMY_TO_FINISH_TURN);
                        cliFramework.printScreen();
                        response = new ClientMessage(MessageTypes.CONFIRM, Events.PLACE_WORKER, MessageSender.SETUP_CONTROLLER);
                    }
                    break;

                case WIN:
                    if (message.getPlayerUsername().equals(username)){

                        cliFramework.printPlayerWin(YOU);
                        cliFramework.printScreen();
                        response = new ClientMessage(MessageTypes.RESPONSE, Events.WIN, MessageSender.TURN_MANAGING_CONTROLLER);
                    }else{
                        cliFramework.printPlayerWin(username);
                        cliFramework.printScreen();

                        response = new ClientMessage(MessageTypes.CONFIRM, Events.WIN, MessageSender.TURN_MANAGING_CONTROLLER);
                    }
                    break;

                case LOSS:
                    if (message.getPlayerUsername().equals(username)){
                        cliFramework.printPlayerLost(YOU);
                        cliFramework.printScreen();

                    }else{
                        cliFramework.printPlayerLost(message.getPlayerUsername());

                    }
                    response = new ClientMessage(MessageTypes.RESPONSE, Events.LOSS, MessageSender.TURN_MANAGING_CONTROLLER);
                    break;
            }
        }



        senderLock.unLock();

    }




    /**
     * Checks if the usernames utilize alphanumerical characters
     * @param rng
     * @return
     */

    private String getRegexFromOneToRange(int rng) {
        int frstDigit = rng%10;
        if(rng<10)
            if(rng>1)
                return "^[1-"+rng+"]$";
            else
                return "^1$";
        else if(rng<20)
            if(rng>10)
                return "^[1-9]$|^1+[0-"+frstDigit+"]$";
            else
                return "^[1-9]$|^1+0$";
        else if(rng<30)
            if(rng>20)
                return "^[1-9]$|^1+[0-9]$|^2+[0-"+frstDigit+"]$";
            else
                return "^[1-9]$|^1+[0-9]$|^2+0$";
        else if(rng<40)
            if(rng>30)
                return "^[1-9]$|^1+[0-9]$|^2+[0-9]$|^3+[0-"+frstDigit+"]$";
            else
                return "^[1-9]$|^1+[0-9]$|^2+[0-9]$|^3+0$";
        else if(rng<50)
            if(rng>40)
                return "^[1-9]$|^1+[0-9]$|^2+[0-9]$|^3+[0-9]$|^4+[0-"+frstDigit+"]$";
            else
                return "^[1-9]$|^1+[0-9]$|^2+[0-9]$|^3+[0-9]$|^4+0$";

        return ".^";
    }




    /**
     * it manages the flow's synchronization
     */

    @Override
    public void run() {

        while(true){
            try{
                senderLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            notify(response);



            senderLock.reset();
        }


    }


    /**
     * This method is used to implement the CLI framework better
     * @param string
     */

    public void print(String string) {
        if(cliFramework.containsMessageBoxInterface()) {
            cliFramework.printMessage(string);
        } else {
            System.out.println(string);
        }
    }





    /**
     * This method is used to implement the CLI framework better
     * @param message
     */

    protected void printLiveTimerMessage(String message) {
        cliFramework.printTimer(message);
    }




    /**
     * This method is used to implement the CLI framework better
     * @param regExToMatch
     * @param secondsAvailable
     * @return
     */


    private AsyncReader read(String regExToMatch, int secondsAvailable) {
        AsyncReader as = new AsyncReader(this, regExToMatch, secondsAvailable);
        Thread read = new Thread(as);
        read.start();
        try {
            read.join();
        } catch (InterruptedException ignored) { }
        return as;
    }


    /**
     * client setter
     * @param client
     */


    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * This method is used to implement the CLI framework better
     */

    protected void setCursor() {
        if(cliFramework.containsMessageBoxInterface())
            cliFramework.setCursorToPosition();
    }


    /**
     * client getter
     * @return
     */

    public Client getClient() {
        return client;
    }


    /**
     * CLI framework getter
     * @return
     */

    public CliFramework getCliFramework() {
        return cliFramework;
    }


}