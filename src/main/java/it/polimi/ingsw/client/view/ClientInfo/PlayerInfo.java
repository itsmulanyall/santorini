package it.polimi.ingsw.client.view.ClientInfo;

public class PlayerInfo {

    private String name;
    private String color;
    private String cardName;
    private String cardDescription;

    public PlayerInfo(String name, String color,String cardName,String cardDescription){
        this.name = name;
        this.color = color;
        this.cardName = cardName;
        this.cardDescription = cardDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }
}
