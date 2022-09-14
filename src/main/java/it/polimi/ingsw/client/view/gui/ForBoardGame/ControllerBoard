package it.polimi.ingsw.client.view.gui.ForBoardGame;


import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.shared.model.*;
import it.polimi.ingsw.shared.utils.CircularList;
import it.polimi.ingsw.shared.utils.Point;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ControllerBoard implements Initializable {
    @FXML
    private GridPane boardPane;
    @FXML
    private ImageView femaleWorkerImage;
    @FXML
    private ImageView maleWorkerImage;
    @FXML
    private ImageView towerImage;
    @FXML
    private Button femaleWorkerButton;
    @FXML
    private Button maleWorkerButton;
    @FXML
    private Button move;
    @FXML
    private Button build;
    @FXML
    private Button undo;
    @FXML
    private Button endTurn;
    @FXML
    private Label maleWorkerLabel;
    @FXML
    private Label femaleWorkerLabel;

    private static ViewGUI viewGUI;

    private ArrayList<Circle> circleArrayList = new ArrayList<>();

    private ArrayList<Point> forWorkers;

    private BoardDetails boardDetails;

    private Circle circle;

    private Button[][] buttonsClicked;

    private Circle[][] circlesToShow;

    private StackPane[][] cells;

    private WorkerDetails workers;

    private CircularList<PlayerDetails> usersInfo;

    private ArrayList<CardDetails> cardsForTable;

    private ArrayList<Action> actionsForWorkers;

    private ArrayList<Point> circles;

    private ArrayList<Point> sharedPoints;

    private String username;

    private PlayerDetails myPlayer;

    private boolean maleWorker;

    private boolean femaleWorker;

    private boolean sharedPoint;

    private Point buttonPoint;

    private int actionIndex;

    public void setViewGUI(ViewGUI viewGUI) {
        ControllerBoard.viewGUI = viewGUI;
    }

    /*methods for shadows*/
    public DropShadow getBorderGlow(int depth) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(javafx.scene.paint.Color.YELLOW);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        return borderGlow;
    }

    public InnerShadow getInnerGlow(int depth) {
        InnerShadow innerGlow = new InnerShadow();
        innerGlow.setOffsetY(5);
        innerGlow.setOffsetX(5);
        innerGlow.setColor(javafx.scene.paint.Color.YELLOW);
        innerGlow.setWidth(depth);
        innerGlow.setHeight(depth);
        return innerGlow;
    }

    public void build() {
        towerImage.setEffect(getBorderGlow(100));
    }

    /*called for placing worker the first time*/
    public void placingWorkers(){
            setAllButtonsOff();
            setWorkers(myPlayer.getColor(),circlesToShow[buttonPoint.getY()][buttonPoint.getX()]);
            sendAnswer();
            maleWorkerImage.setEffect(null);
            femaleWorkerImage.setEffect(null);
    }

    /*controller place male worker on button clicked */
    public void placeMaleWorkerOnBoard() {
        maleWorkerImage.setEffect(getBorderGlow(100));
        getAllowedMovements();
    }

    /*controller place female worker on button clicked*/
    public void placeFemaleWorkerOnBoard() {
        femaleWorkerImage.setEffect(getBorderGlow(100));
        getAllowedMovements();
    }


    /*to set worker on board*/
    public void setWorkers(it.polimi.ingsw.shared.color.Color c, Circle circle){
        circle.setRadius(20);
        circle.setFill(Color.rgb(c.getRGBValues().x,c.getRGBValues().y,c.getRGBValues().z));
        circle.setVisible(true);
    }


    /*update board with values from view gui*/
    public void setBoardUpdate() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Point p = new Point(j,i);
                if (boardDetails.getTile(i,j).hasWorker()) {
                    setWorkers(boardDetails.getTile(i,j).getWorker().getColor(),circlesToShow[i][j]);
                    if (myPlayer.getColor().equals(boardDetails.getTile(i,j).getWorker().getColor())) {
                        circles.add(p);
                    }
                    if (boardDetails.getTile(i,j).getLevels().size() > 1 && !boardDetails.getTile(i, j).getLevels().contains(BuildLevel.DOME)) {
                        switch (boardDetails.getTile(i, j).getLevels().size()) {
                            case 2:
                                cells[i][j].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/level1.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                                break;
                            case 3:
                                cells[i][j].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/level2.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                                break;
                            case 4:
                                cells[i][j].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/level3.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                                break;
                        }
                    }
                    if (boardDetails.getTile(i, j).getLevels().contains(BuildLevel.DOME)) {
                        cells[i][j].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/levelDome.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                    }
                }
            }
        }
    }

        /*for action of type pass*/
        public void getActionPass () {
            setAllButtonsOff();
            for (Action a : actionsForWorkers) {
                if (a.getWorker() == null) {
                    actionIndex = actionsForWorkers.indexOf(a);
                }
            }
            sendAnswer();
        }

        /*for action of type move with two workers*/
        public void setSingleWorkerForMove() {
                femaleWorkerLabel.setText("("+ circles.get(0).getY()+";"+circles.get(0).getX()+")");
                femaleWorkerButton.setVisible(true);
                femaleWorkerButton.setOnAction(e -> {
                    placingWorkers();
                    circlesToShow[circles.get(0).getY()][circles.get(0).getX()].setVisible(false);
                    for (Action a: actionsForWorkers){
                        if (a.getFrom().equals(circles.get(0))&&a.getTo().equals(buttonPoint)){
                            actionIndex = actionsForWorkers.indexOf(a);
                        }
                    }
                });
                maleWorkerLabel.setText("("+ circles.get(1).getY()+";"+circles.get(1).getX()+")");
                maleWorkerButton.setVisible(true);
                maleWorkerButton.setOnAction(e -> {
                    placingWorkers();
                    circlesToShow[circles.get(1).getY()][circles.get(1).getX()].setVisible(false);
                    for (Action a: actionsForWorkers){
                        if (a.getFrom().equals(circles.get(1))&&a.getTo().equals(buttonPoint)){
                            actionIndex = actionsForWorkers.indexOf(a);
                        }
                    }
                });
        }

        /*for action of type Move*/
        public void getActionMoveToShow (){
            for (Action a : actionsForWorkers) {
                if (a.getWorker() != null) {
                    buttonsClicked[a.getTo().getY()][a.getTo().getX()].setVisible(true);
                    buttonsClicked[a.getTo().getY()][a.getTo().getX()].setEffect(getBorderGlow(100));
                    buttonsClicked[a.getTo().getY()][a.getTo().getX()].setOnAction(e -> {
                        buttonPoint = new Point(a.getTo().getX(), a.getTo().getY());
                        if (sharedPoint){
                            sharedPoints.contains(a.getTo());
                            setSingleWorkerForMove();
                        }else{
                        placingWorkers();
                        circlesToShow[a.getFrom().getY()][a.getFrom().getX()].setVisible(false);
                        actionIndex = actionsForWorkers.indexOf(a);
                    }
                    });
                }
            }
        }

        /*for two workers build*/
        public void setSingleWorkerForBuild() {
            femaleWorkerLabel.setText("("+ circles.get(0).getY()+";"+circles.get(0).getX()+")");
            femaleWorkerButton.setVisible(true);
            femaleWorkerButton.setOnAction(e -> {
                for (Action a: actionsForWorkers){
                    if (a.getFrom().equals(circles.get(0))&&a.getTo().equals(buttonPoint)){
                        setCellId(a.getBuildLevel());
                        actionIndex = actionsForWorkers.indexOf(a);
                    }
                }
            });
            maleWorkerLabel.setText("("+ circles.get(1).getY()+";"+circles.get(1).getX()+")");
            maleWorkerButton.setVisible(true);
            maleWorkerButton.setOnAction(e -> {
                for (Action a: actionsForWorkers){
                    if (a.getFrom().equals(circles.get(1))&&a.getTo().equals(buttonPoint)){
                        setCellId(a.getBuildLevel());
                        actionIndex = actionsForWorkers.indexOf(a);
                    }
                }
            });
        }

        /*for action of type build*/
        public void getActionBuildToShow () {
            for (Action a : actionsForWorkers) {
                if (a.getWorker() != null) {
                    buttonsClicked[a.getTo().getY()][a.getTo().getX()].setVisible(true);
                    buttonsClicked[a.getTo().getY()][a.getTo().getX()].setEffect(getBorderGlow(100));
                    buttonsClicked[a.getTo().getY()][a.getTo().getX()].setOnAction(e -> {
                        buttonPoint = new Point(a.getTo().getX(), a.getTo().getY());
                        if (sharedPoint){
                            sharedPoints.contains(a.getTo());
                            setSingleWorkerForBuild();
                        }else {
                            setCellId(a.getBuildLevel());
                            actionIndex = actionsForWorkers.indexOf(a);
                            sendAnswer();
                        }
                    });
                }
            }
        }

        public void setCellId (BuildLevel buildLevel){
            setAllButtonsOff();
            switch (buildLevel) {
                case LEVEL_1:
                    cells[buttonPoint.getY()][buttonPoint.getX()].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/level1.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                    break;
                case LEVEL_2:
                    cells[buttonPoint.getY()][buttonPoint.getX()].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/level2.png); -fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                    break;
                case LEVEL_3:
                    cells[buttonPoint.getY()][buttonPoint.getX()].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/level3.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                    break;
                case DOME:
                    cells[buttonPoint.getY()][buttonPoint.getX()].setStyle("-fx-background-image: url(client/images/buildingObjects/towerLevels/levelDome.png);-fx-background-repeat: stretch; -fx-background-size: 100% 100%;");
                    break;
            }
        }

        /*set all the buttons from allowed movement given by the controller*/
        public void getAllowedMovements () {
            for (Point p : forWorkers) {
                buttonsClicked[p.getY()][p.getX()].setVisible(true);
                buttonsClicked[p.getY()][p.getX()].setEffect(getBorderGlow(100));
                buttonsClicked[p.getY()][p.getX()].setOnAction(e -> {
                    buttonPoint = new Point(p.getX(), p.getY());
                    placingWorkers();
                });
            }
        }

        /*set all button of cells off*/
        public void setAllButtonsOff () {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    buttonsClicked[j][i].setVisible(false);
                }
            }
        }

        /*sets arrays from the gridPane with elements*/
        private void getNodeFromBoard (GridPane gridPane,int col, int row){
            for (Node node : gridPane.getChildren()) {
                Integer c = GridPane.getColumnIndex(node);
                Integer r = GridPane.getRowIndex(node);
                if (c == col && r == row && (node instanceof StackPane)) {
                    StackPane cellPane = (StackPane) node;
                    Button button = new Button();
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    cellPane.getChildren().add(circle);
                    cellPane.getChildren().add(button);
                    cells[row][col] = cellPane;
                    buttonsClicked[row][col] = button;
                    circlesToShow[row][col] = circle;
                    button.setText("here");
                    button.setVisible(false);
                    circle.setVisible(false);
                }
            }
        }

        /*create shape first time for place workers*//*
        public void createShape () {
            for (PlayerDetails p : usersInfo) {
                p.getUsername().equals(username);
                myPlayer = p;
            }
            circle = new Circle();
            circle.setRadius(20);
            circle.setFill(Color.rgb(myPlayer.getColor().getRGBValues().x, myPlayer.getColor().getRGBValues().y, myPlayer.getColor().getRGBValues().z));
        }*/

        /*getter ands setter to get information from view gui*/
        public ArrayList<Point> getForWorkers () {
            return forWorkers;
        }

        public void setForWorkers (ArrayList < Point > forWorkers) {
            this.forWorkers = new ArrayList<>(forWorkers);
            if (forWorkers.size() % 2 == 0) {
                femaleWorkerButton.setVisible(true);
                femaleWorkerButton.setOnAction(e -> {
                    placeFemaleWorkerOnBoard();
                });
            } else {
                maleWorkerButton.setVisible(true);
                maleWorkerButton.setOnAction(e -> {
                    placeMaleWorkerOnBoard();
                });
            }
        }

        public BoardDetails getBoardDetails () {
            return boardDetails;
        }

        public void setBoardDetails (BoardDetails boardDetails){
            this.boardDetails = boardDetails;
            setBoardUpdate();
        }

        public CircularList<PlayerDetails> getUsersInfo () {
            return usersInfo;
        }

        public void setUsersInfo (CircularList < PlayerDetails > usersInfo, String username){
            this.usersInfo = usersInfo;
            this.username = username;
            for (PlayerDetails p : usersInfo){
                if (p.getUsername().equals(username))
                    myPlayer = p;
            }
        }

        public ArrayList<CardDetails> getCardsForTable () {
            return cardsForTable;
        }

        public void setCardsForTable (ArrayList < CardDetails > cardsForTable) {
            this.cardsForTable = cardsForTable;
        }

        public Point getButtonPoint () {
            return buttonPoint;
        }

        public void setButtonPoint (Point buttonPoint){
            this.buttonPoint = buttonPoint;
        }

        public ArrayList<Action> getActionsForWorkers () {
            return actionsForWorkers;
        }

        public void setActionsForWorkers (ArrayList < Action > actionsForWorkers) {
            this.actionsForWorkers = new ArrayList<>(actionsForWorkers);
            for (Action a : actionsForWorkers) {
                switch (a.getActionType()) {
                    case MOVE:
                        move.setVisible(true);
                        break;
                    case BUILD:
                        build.setVisible(true);
                        break;
                    case PASS:
                        endTurn.setVisible(true);
                        break;
                }
            }
            maleWorker = false;
            femaleWorker = false;
            sharedPoint = false;
            sharedPoints = new ArrayList<>();
            ArrayList<Point> maleWorker = new ArrayList<>();
            ArrayList<Point> femaleWorker = new ArrayList<>();
            for (Action a : actionsForWorkers){
                if (a.getWorker()!=null) {
                    if (circles.get(0).equals(a.getFrom())) {
                        maleWorker.add(a.getTo());
                    }
                    if (circles.get(1).equals(a.getFrom())) {
                        femaleWorker.add(a.getTo());
                    }
                }
            }
            for (Point point : maleWorker) {
                for (int j = 0; j < femaleWorker.size(); j++) {
                    if (point.equals(femaleWorker.get(j))) {
                        sharedPoints.add(point);
                    }
                }
            }
        }


        /*sent answer for worker placement*/
        public int getPlaceWorkerAnswer () {
            int index = 0;
            int i = 0;
            for (Point p : forWorkers) {
                if (p.equals(buttonPoint))
                    index = i;
                i++;
            }
            ;
            return index;
        }

        /*get answer for action choice*/
        public int getActionAnswer () {
            return actionIndex;
        }





        public void clearBoard() {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    for (Node node : boardPane.getChildren()) {
                        Integer c = GridPane.getColumnIndex(node);
                        Integer r = GridPane.getRowIndex(node);
                        if (c == i && r == j && (node instanceof StackPane)) {
                            StackPane cellPane = (StackPane) node;
                            if (!cellPane.getChildren().isEmpty())
                            cellPane.getChildren().clear();
                        }
                    }
                }
            }
        }



        /*for unlock purpose*/
        public void sendAnswer () {
            viewGUI.guiConfirm();
        }

        /*for Action button off*/
        public void initButtonsOff () {
            move.setVisible(false);
            build.setVisible(false);
            undo.setVisible(false);
            endTurn.setVisible(false);
            femaleWorkerButton.setVisible(false);
            maleWorkerButton.setVisible(false);
        }

        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            clearBoard();
            cells = new StackPane[5][5];
            circles = new ArrayList<>();
            circlesToShow = new Circle[5][5];
            buttonsClicked = new Button[5][5];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    getNodeFromBoard(boardPane, j, i);
                }
            }
            /*setting all buttons*/
            initButtonsOff();
        }
}
