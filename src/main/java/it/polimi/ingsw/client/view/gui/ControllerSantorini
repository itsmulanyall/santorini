package it.polimi.ingsw.client.view.gui;



import it.polimi.ingsw.shared.socket.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * creates the clients for the viewGUI
 */


public class ControllerSantorini {

    private static ViewGUI viewGUI;
    private Client client;

    private String ip;
    private int port;
    @FXML
    TextField ipText;
    @FXML
    TextField portText;
    @FXML
    Button gotoNickName;
    @FXML
    Button setIpAndPort;

    public void setSetIpAndPort(){
        ip = ipText.getText();
        String p = portText.getText();
        port = Integer.parseInt(p);
    }

    public void getViewGUI() {
        viewGUI = new ViewGUI((Stage)gotoNickName.getScene().getWindow());
        try{
        client= new Client(viewGUI,ip ,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
    }


}
