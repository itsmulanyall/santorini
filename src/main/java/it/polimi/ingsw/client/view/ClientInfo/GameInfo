package it.polimi.ingsw.client.view.ClientInfo;


import it.polimi.ingsw.client.utils.CardsDetailJsonDeserializer;
import it.polimi.ingsw.shared.color.Colors;
import it.polimi.ingsw.shared.model.CardDetails;
import it.polimi.ingsw.shared.model.GodCards;
import it.polimi.ingsw.shared.model.PlayerDetails;
import it.polimi.ingsw.shared.utils.CircularList;

import java.util.ArrayList;

public class GameInfo {

    private String username;

    private int numberOfPlayers;

    private String challenger;

    private ArrayList<String> hand = new ArrayList<>();

    private CircularList<PlayerDetails> playersInGame;

    private ArrayList<String> colorList;

    ArrayList<CardDetails> fullDeck = new ArrayList<>(GodCards.values().length);

    private CardDetails cardDetails;

    public GameInfo(){
        colorList = new ArrayList<>();
        for (Colors c: Colors.values() ) {
            colorList.add(c.toString());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getChallenger() {
        return challenger;
    }

    public void setChallenger(String challenger) {
        this.challenger = challenger;
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public CircularList<PlayerDetails> getPlayersInGame() {
        return playersInGame;
    }

    public void setPlayersInGame(CircularList<PlayerDetails> playersInGame) {
        this.playersInGame = playersInGame;
    }
    //non serve
    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    public ArrayList<String> getColorList() {
        return colorList;
    }

    public ArrayList<CardDetails> getFullDeck() {
        ArrayList<String> names = new ArrayList<>(14);
        for (GodCards g : GodCards.values()) {
            names.add(g.toString().substring(0, 1).toUpperCase() + g.toString().substring(1).toLowerCase());
        }
        return new CardsDetailJsonDeserializer().getDetailedCards(names);
    }




}
