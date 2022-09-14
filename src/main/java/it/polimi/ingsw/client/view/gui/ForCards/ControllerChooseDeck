package it.polimi.ingsw.client.view.gui.ForCards;

import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.shared.model.CardDetails;
import it.polimi.ingsw.shared.model.GodCards;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * This controller shows the cards to the challenger
 */


public class ControllerChooseDeck implements Initializable {
    @FXML
    TreeView<String> godCards;
    @FXML
    TextArea cardDescription;
    @FXML
    Label firstCard;
    @FXML
    Label secondCard;
    @FXML
    Label numberOfPlayers;
    @FXML
    ImageView godImages;
    @FXML
    Button selectedCard;
    @FXML
    Button goToCard2Players;
    @FXML
    ListView<String> selectedCards;

    private static ViewGUI viewGUI;


    /**
     * viewGUI setter
     * @param viewGUI
     */

    public void setViewGUI(ViewGUI viewGUI) {
        ControllerChooseDeck.viewGUI = viewGUI;
    }

    //public static ObservableList<String> cardChoosed = FXCollections.observableArrayList();
    private int i = 0;
    private Image cardImage;
    private String godImage;
    private String godName;
    private String selectedGod;
    private String description;
    private ObservableList<String> cardsToAdd;
    private ObservableList<String> handConfirmed;
    boolean alreadyPresent=false;
    private int numberOfCards;
    private ArrayList<CardDetails> fullDeckToShow;





    /**
     * this method shows the card image and description  on the TreeView ,
     * it prohibits the player from choosing the card that have already been chosen
     * @param mouseEvent
     * @throws FileNotFoundException
     */
    public void showCardImageAndDescription(javafx.scene.input.MouseEvent mouseEvent) throws FileNotFoundException {
        TreeItem<String> item = godCards.getSelectionModel().getSelectedItem();
        godName = item.getValue();
        //to choose only the right number of cards
        if(i== numberOfCards || godName.equals("God Cards") || godName.equals("Advanced God Cards" )|| godName.equals("Simple God Cards")||selectedCards.getItems().contains(godName)){
            selectedCard.setDisable(true);
            if(i==numberOfCards){
                goToCard2Players.setDisable(false);
                firstCard.setText("You have reached the maximum!");
                secondCard.setText("If you want to choose another card ,you have to remove one.");}
        }else{
            goToCard2Players.setDisable(true);
            selectedCard.setDisable(false);
            cardImage = new Image("client/images/godCards/" + godName + ".jpg");
            godImages.setImage(cardImage);
            int indexCard = GodCards.valueOf(godName.toUpperCase()).ordinal();
            description = fullDeckToShow.get(indexCard).getCardDescription();
            cardDescription.setText(godName + ":\nKnown as the " + description);
        }
    }





    /**
     * this method adds the selected card to the list view
     */

    public void addSelectedCardToListView(){
        TreeItem<String> item = godCards.getSelectionModel().getSelectedItem();
        selectedGod = item.getValue();
        cardsToAdd = FXCollections.observableArrayList();
            cardsToAdd.add(item.getValue());
            selectedCards.getItems().addAll(cardsToAdd);
            i++;
        for(String cardList : cardsToAdd) {
           if(cardList.equals(godName)) {
               selectedCard.setDisable(true);
           }
        }
    }





    /**
     * this method removes the card from the list view
     */

    public void removeSelectedCard() {
        if (i>0) {
           String selectedItem = selectedCards.getSelectionModel().getSelectedItem().toString();
           selectedCards.getItems().remove(selectedItem);
           firstCard.setText("");
           secondCard.setText("");
            i--;
        }else{
            secondCard.setText("There are no cards left. Please select a card from the list above.");
        }
    }


    /**
     * handGUI getter
     * @return
     */

    public int[] getHandGui(){
        int[] hand = new int[numberOfCards];
        handConfirmed = selectedCards.getItems();
        ArrayList<String> newHand = new ArrayList<>();
        newHand.addAll(handConfirmed);
        for (int j = 0; j <numberOfCards ; j++) {
            int i =GodCards.valueOf(newHand.get(j).toUpperCase()).ordinal();
          hand[j] = i;
        }
        return hand;
    }




    public void confirmDeck(){
        viewGUI.guiConfirm();
    }



    /**
     * setters
     */


    public void setFullDeckToShow(ArrayList<CardDetails> fullDeckToShow) {
        this.fullDeckToShow = fullDeckToShow;
    }


    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
        numberOfPlayers.setText("" + numberOfCards);
    }




    /**
     * getters
     */

    public ArrayList<CardDetails> getFullDeckToShow() {
        return fullDeckToShow;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }




    /**
     * initializes the objects before a scene starts
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     //initialaze tree titles
        TreeItem<String> root = new TreeItem<>("God Cards");
        root.setExpanded(true);

        TreeItem<String> nodeS = new TreeItem<>("Simple God Cards");
        nodeS.setExpanded(true);

        TreeItem<String> nodeA = new TreeItem<>("Advanced God Cards");
        nodeA.setExpanded(true);

        root.getChildren().addAll(nodeS,nodeA);


        int i = 0;
        for (GodCards g: GodCards.values()){
            godName = g.toString();
            godName = godName.substring(0,1).toUpperCase() + godName.substring(1).toLowerCase();
            TreeItem<String> node = new TreeItem<>(godName);
            if(i==6||i == 7|| i==10||i>11){
                nodeA.getChildren().add(node);
                i++;
            }else{
                nodeS.getChildren().add(node);
                i++;
            }
        }
        godCards.setRoot(root);
        //initialiaze textfield
        cardDescription.setEditable(false);
        cardDescription.setWrapText(true);
        cardDescription.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 16; -fx-text-fill: #ffa2e9;");
        //initialaze labels for selected cards
        firstCard.setText("");
        firstCard.setWrapText(true);
        secondCard.setText("");
        //initialize button
        goToCard2Players.setDisable(true);

        //initialize the listview
    }


}
