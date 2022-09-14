package it.polimi.ingsw.client.view.cli;

public class CliConstants {

    public static final String CLI_OR_GUI = "Select visualization mode:\nc <- Command Line Interface\ng <- Graphical User Interface";
    public static final String CHOSEN_CLI = "c";
    public static final String CHOSEN_GUI = "g";
    public static final String WRONG_CLI_GUI_CHOICE = "Input error, please provide a correct choice!";

    public static final String TIME_LEFT = " Time left:";
    public static final String SECONDS = "s ";
    public static final String MINUTES = "m";
    public static final String TIME_EXPIRED = " Out of Time! ";
    public static final String GET_NAME = "What username do you want to use?";
    public static final String NAME_ALREADY_TAKEN = "This username has already been taken!";
    public static final String GET_NUM_PLAYERS = "You are the first one, what mode do you want do play?\n[1]Two Players Mode  or [2]Three Player Mode";
    public static final String NOT_A_VALID_MODE = "You inserted an invalid mode";

    public static final String YOU = "You";
    public static final String CHOOSE_A_PLAYER_FOR_TURN = "Challenger! Insert the username of the player that will start placing his workers";
    public static final String YOU_ARE_THE_CHALLENGER = "You are the challenger!";
    public static final String SELECT = "Select ";
    public static final String CARD_NAME = "Name:";
    public static final String CARD_DESCRIPTION =" Description:";

    public static final String RECEIVED_AVAILABLE_POSES_PER_WORKER = "These are the allowed poses (x, y) for placing your worker,choice by inserting a number between those on the left";
    public static final String CAN_PLACE_WORKER = "You can place worker in cell ";
    public static final String IS_PLAYER_IN_TURN = "is in turn";
    public static final String RECEIVED_ALLOWED_ACTIONS = "These are the allowed actions, make your action by inserting one of the left numbers";
    public static final String CAN = "Your worker can";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String FORCE = "and FORCE enemy worker to move";
    public static final String YOU_LOSS = "have lost!";
    public static final String PLAYER_LOSS = "has lost!";
    public static final String PLAYER_WON = "has won!";
    public static final String YOU_WON = "have won!";

    public static final String CONNECTION_CLOSED = "Connection closed!";
    public static final String CANNOT_REACH_A_PLAYER = "cannot be reached due to a network problem, the game is closing...";

    public static final String WAITING_FOR_CHALLENGER_CHOOSE_DECK = "waiting for the challenger to pick the deck...";
    public static final String WAITING_FOR_CHALLENGER_CHOOSE_FIRST_PLAYER ="Waiting for the challenger to choose the first player...";
    public static final String WAITING_FOR_ENEMY_TO_CHOOSE_COLOR = "Waiting for the player to choose the workers color...";
    public static final String WAITING_FOR_ENEMY_TO_CHOOSE_CARD = "Waiting for the other players to choose their card...";
    public static final String WAITING_FOR_ENEMY_TO_PLACE_WORKERS = "Waiting for the player to place the worker";
    public static final String WAITING_FOR_ENEMY_TO_FINISH_TURN = "Waiting for the player to finish the turn";

    public static final String START_TURN= "your turn starts now!";
}
