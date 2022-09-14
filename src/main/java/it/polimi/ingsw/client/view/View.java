package it.polimi.ingsw.client.view;


import it.polimi.ingsw.client.view.ClientInfo.GameInfo;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.shared.message.ClientMessage;
import it.polimi.ingsw.shared.message.Message;
import it.polimi.ingsw.shared.model.*;
import it.polimi.ingsw.shared.socket.Client;
import it.polimi.ingsw.shared.utils.CircularList;
import it.polimi.ingsw.shared.utils.Point;

import java.util.ArrayList;


public abstract class View extends Observable<ClientMessage> implements Observer<Message> {

    protected String username;

    protected GameInfo gameInfo;

    protected String color;

    protected String userCard;

    protected PlayerDetails playerInfo;

    protected int numberOfPlayers;

    protected String challenger;

    protected String firstPlayer;

    protected ArrayList<CardDetails> handConfirmed;

    protected ArrayList<String> hand;

    protected CircularList<String> playersUsername;

    protected CircularList<PlayerDetails> playersInGame;

    protected ArrayList<String> colorList;

    protected ClientMessage response;

    protected ArrayList<Point> availablePositions;

    protected ArrayList<Action> possibleActions;

    protected String userNameMessage;

    protected WorkerDetails workerMessage;

    protected BoardDetails board;

    public abstract void setClient(Client client);
}


