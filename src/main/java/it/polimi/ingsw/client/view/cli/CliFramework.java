package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.utils.CardsDetailJsonDeserializer;
import it.polimi.ingsw.shared.color.Color;
import it.polimi.ingsw.shared.model.Action;
import it.polimi.ingsw.shared.model.BuildLevel;
import it.polimi.ingsw.shared.model.CardDetails;
import it.polimi.ingsw.shared.model.PlayerDetails;
import it.polimi.ingsw.shared.utils.CircularList;
import it.polimi.ingsw.shared.utils.Point;
import it.polimi.ingsw.shared.utils.Triplet;
import it.polimi.ingsw.shared.utils.Tuple;

import java.io.PrintStream;
import java.util.ArrayList;

import static it.polimi.ingsw.client.view.cli.CliConstants.*;

class CliFramework {

    private final char verticalMonoLineSegment = '|';

    private final String topRightMonoCornerMap = "┐";

    private final String downLeftMonoCornerMap = "└";

    private final String downRightMonoCornerMap = "┘";

    private final String topLeftMonoCornerMap = "┌";

    private final String topCellMonoSeparator = "┬";

    private final String downCellMonoSeparator = "┴";

    private final String topLeftCornerBoard = "╔";

    private final String topRightCornerBoard = "╗";

    private final String downRightCornerBoard = "╝";

    private final String downLeftCornerBoard = "╚";

    private final String horizontalLineSegment = "═";

    private final String verticalLineSegment = "║";

    private final String verticalRightCellSeparator = "╣";

    private final String verticalLeftCellSeparator = "╠";

    private final String crossCellSeparator = "╬";

    private final String downCellSeparator = "╩";

    private final String topCellSeparator = "╦";

    private final char sand = '^';

    private final char water = '~';

    private final char horizontalMonoLineSegment = '─';

    private final char nl = '\n';

    private final char s = ' ';


    private final String esc = "\u001b[";
    private final String color_bg = esc+"48;5;";
    private final String reversed = esc+"7m";
    private final String bold = esc+"1m";
    private final String clear_full_screen = esc+"2J";
    private final String clear_begin_to_curs_screen = esc+"1J";
    private final String move_cursor_to_topleft = esc+"0;0H";
    private final String move_cursor_to_start_column = esc+"79;134H";
    private final String save_cursor = esc+"s";
    private final String restore_cursor = esc+"u";
    private final String up = "A";
    private final String down = "B";
    private final String right = "C";
    private final String left = "D";

    //environment
    private final String color_water = esc+"48;5;19m";
    private final String color_bg_gravel = esc+"48;2;85;46;44m";

    private final String color_black = esc+"38;2;0;0;0m";
    private final String worker_icon_high =  " 0[■]";
    private final String worker_icon_medUp = "-┼-| ";
    private final String worker_icon_medLow =" |   ";
    private final String worker_icon_low =   "/ \\  ";
    private final int worker_dim = 4;

    //soil
    private final String color_bg_grass = color_bg+"34m";
    private final String color_bg_level1 = color_bg+"250m";
    private final String color_bg_level2 = color_bg+"252m";
    private final String color_bg_level3 = color_bg+"255m";
    private final String color_bg_dome = color_bg+"33m";
    private final String rstColor = esc+"0m";

    private final Tuple<Integer, Integer> aspectRatio = new Tuple<>(5, 3); //optimal is 5,3 and cell_size 3
    private final int cellDimX;
    private final int cellDimY;
    private final int cellSize;

    private final StringBuilder bb = new StringBuilder();
    private final PrintStream out = System.out;


    public CliFramework() {
        cellSize = 4;
        cellDimX = cellSize*aspectRatio.x;
        cellDimY = cellSize*aspectRatio.y;
        createEmptyBoard();
    }


    public void setCursorToPosition() {
        System.out.print(move_cursor_to_topleft+"\u001b[79;135H");
    }

    /**
     * flusher of the buffer that prints everything that's in the string builder
     */
    public void printScreen() {
        bufferScreen();
        out.flush();
    }

    /**
     * called during the setup of the connection
     */
    public void printNumPlayersRequest() {
        System.out.println(GET_NUM_PLAYERS);
    }


    /**
     * checks if there's a message box
     * @return true if found
     */
    public boolean containsMessageBoxInterface() {
        return bb.indexOf("Message Box")!=-1;
    }


    /**
     * to be called when a player receives a message thats not for him and allows other players to know who is turn of
     * @param username of the player that should have received the actions
     */
    public void printEnemyTurn(String username, String event) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        bb.replace(cursor, cursor+"Player".length(), "Player");
        cursor+="Player".length()+1;
        bb.replace(cursor, cursor+username.length()+1+IS_PLAYER_IN_TURN.length(), username+" "+IS_PLAYER_IN_TURN);
        cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+4;
        bb.replace(cursor, cursor+event.length(),event);
    }


    /**
     * lights up a index selected by a player
     * @param index of the card in the deck
     */
    public void lightUpCardIndex(int index) {
        int cursor;
        if((cursor = bb.indexOf(index +". Name"))!=-1) {
            bb.insert(cursor, reversed);
            cursor+=reversed.length()+2;
            bb.insert(cursor, rstColor);
        }
    }


    /**
     * to be called when this player receives a deck or a hand to choose cards from
     * @param cards is the list of <cardName, description> of the gods to choose from
     * @param numberOfCards to choose, can be 1 in case of a card to pick from hand, or can be 2/3 in case of the challenger has to choose cards for other players
     */
    public void printCardsSelection(ArrayList<CardDetails> cards, int numberOfCards) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        if(numberOfCards>1) {
            bb.replace(cursor, cursor+YOU_ARE_THE_CHALLENGER.length()+1, YOU_ARE_THE_CHALLENGER+" ");
            cursor+=YOU_ARE_THE_CHALLENGER.length()+1;
        }
        bb.replace(cursor, cursor+SELECT.length(), SELECT);
        cursor+=SELECT.length();
        bb.replace(cursor, cursor+String.valueOf(numberOfCards).length(), String.valueOf(numberOfCards));
        cursor+=String.valueOf(numberOfCards).length()+1;
        String c = numberOfCards>1 ? "cards" : "card";
        bb.replace(cursor, cursor+c.length(), c);

        for(int i=0; i<cards.size(); i++) {
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), cursor)+1))+rstColor.length()+4;
            bb.replace(cursor, cursor+String.valueOf(i+1).length()+1, i+1 +".");
            cursor+=String.valueOf(i+1).length()+2;
            bb.replace(cursor, cursor+CARD_NAME.length(), CARD_NAME);
            cursor+=CARD_NAME.length()+1;
            bb.replace(cursor, cursor+cards.get(i).getCardName().length(), cards.get(i).getCardName());
            cursor+=cards.get(i).getCardName().length()+1;
            bb.replace(cursor, cursor+CARD_DESCRIPTION.length(), CARD_DESCRIPTION);
            cursor+=CARD_DESCRIPTION.length()+1;
            String descFirstPart = cards.get(i).getCardDescription().substring(0, cards.get(i).getCardDescription().indexOf("\n"));
            bb.replace(cursor, cursor+descFirstPart.length(), descFirstPart);
            String descSecondPart = cards.get(i).getCardDescription().substring(cards.get(i).getCardDescription().indexOf("\n")+1);
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+4;
            if(descSecondPart.length()/(27*aspectRatio.x-4)>0) {
                bb.replace(cursor, cursor+27*aspectRatio.x-5, descSecondPart.substring(0, 27*aspectRatio.x-5));
                cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+4;
                bb.replace(cursor, cursor+descSecondPart.substring(27*aspectRatio.x-5).length(), descSecondPart.substring(27*aspectRatio.x-5));
            } else
                bb.replace(cursor, cursor+descSecondPart.length(), descSecondPart);
        }
    }


    /**
     * to be called when firstly received a circular rotation of players, or when the rotation has been updated because a player lost the match or has received a card or a color
     * @param players rotation
     * sets the cards
     */
    public void updatePlayers(CircularList<PlayerDetails> players, String thisPlayerName) {
        CircularList<PlayerDetails> copy = new CircularList<>(players);
        while (!copy.getFirst().getUsername().equals(thisPlayerName)) copy.goNext();
        int cursor = bb.indexOf(topRightMonoCornerMap)+2;

        //first time
        if(bb.charAt(cursor)!=topLeftMonoCornerMap.charAt(0))
            createInformationInterfaces(cursor, copy);
        clearMessageBox();
        cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), 0)+1)+1));
        for(int indxPlayer=0; indxPlayer<copy.size(); indxPlayer++) {
            cursor = bb.indexOf("Card: ", cursor)+"Card: ".length();
            if (copy.getFirst().hasCard() && bb.charAt(cursor)==s) {
                int descCurs;
                ArrayList<String> name = new ArrayList<>();
                name.add(copy.getFirst().getCard().getCardName());
                CardDetails card = new CardsDetailJsonDeserializer().getDetailedCards(name).get(0);
                bb.replace(cursor, cursor + card.getCardName().length(), card.getCardName());
                descCurs = bb.indexOf("Description: ", cursor)+("Description: ").length();
                String description = card.getCardDescription();
                String firstPart = description.substring(0, description.indexOf(nl));
                String secondPart = description.substring(description.indexOf(nl)+1);
                for(int plIndx=0; plIndx<indxPlayer; plIndx++)
                    descCurs+=9*aspectRatio.x;
                bb.replace(descCurs, descCurs+firstPart.length(), firstPart);
                int index=0;
                descCurs = bb.indexOf(String.valueOf(nl),bb.indexOf(String.valueOf(nl), descCurs)+1);
                while(index<secondPart.length()) {
                    int to = Math.min(index+(9 * aspectRatio.x - 3), secondPart.length()-1);
                    if(to!=secondPart.length()-1 && secondPart.charAt(to)!=s)
                        to = secondPart.lastIndexOf(String.valueOf(s), to);
                    String section = secondPart.substring(secondPart.charAt(index)==s ? index+1 : index, to);
                    descCurs = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), descCurs))+rstColor.length()+4;
                    for (int plIndx = 0; plIndx < indxPlayer; plIndx++)
                        descCurs += 9 * aspectRatio.x;
                    bb.replace(descCurs, descCurs + section.length(), section);
                    if(to!=secondPart.length()-1)
                        index = to;
                    else
                        break;
                }
            }
            if(copy.getFirst().hasAColor() && bb.charAt(bb.indexOf(s+copy.getFirst().getUsername()+s)-1)==s) {
                String username = copy.getFirst().getUsername();
                int nameCurs = bb.indexOf(s+username+s);
                String color = convertColor(copy.getFirst().getColor());
                bb.insert(nameCurs, color);
                nameCurs += color.length()+1;
                bb.replace(nameCurs, nameCurs + username.length(), username);
                nameCurs += username.length()+1;
                bb.insert(nameCurs, rstColor);
            }
            copy.goNext();
        }
    }


    /**
     * to be called when the challenger has to choose the player that will start to place his workers
     */
    public void printPlayerToChoose(CircularList<String> players) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        bb.replace(cursor, cursor+CHOOSE_A_PLAYER_FOR_TURN.length(), CHOOSE_A_PLAYER_FOR_TURN);
        for(int npl=0; npl<players.size(); npl++) {
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), cursor)+1))+rstColor.length()+4;
            bb.replace(cursor, cursor+players.get(npl).length()+3, (npl+1) + ". "+players.get(npl));
        }
    }

    /**
     * called when a player has to choose a color
     * @param colors to choose
     */
    public void printColorToChoose(ArrayList<String> colors) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        bb.replace(cursor, cursor+"Choose your color from the list (please enter the number next to the chosen color):".length(), "Choose your color from the list (please enter the number next to the chosen color):");
        for(int ncls=0; ncls<colors.size(); ncls++) {
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), cursor)+1))+rstColor.length()+4;
            String color = convertColor(new Color(colors.get(ncls)));
            bb.replace(cursor, cursor+colors.get(ncls).length()+3,(ncls+1) + ". " + colors.get(ncls));
            cursor+=3;
            bb.insert(cursor, color);
            cursor = bb.indexOf(String.valueOf(s), cursor);
            bb.insert(cursor, rstColor);
        }
    }


    /**
     * prints in the message box the available poses to place workers
     * @param poses available
     */
    public void printAvailablePosesPerWorker(ArrayList<Point> poses) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1)+1)+rstColor.length()+4;
        bb.replace(cursor, cursor+RECEIVED_AVAILABLE_POSES_PER_WORKER.length(), RECEIVED_AVAILABLE_POSES_PER_WORKER);
        for(Point p : poses) {
            if(poses.size()<24)
                cursor = bb.indexOf(rstColor + "| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), cursor) + 1)) + rstColor.length() + 4;
            else
                cursor = bb.indexOf(rstColor + "| ", bb.indexOf(String.valueOf(nl), cursor+1))+rstColor.length()+4;
            bb.replace(cursor, cursor+String.valueOf(poses.indexOf(p)+1).length()+1+CAN_PLACE_WORKER.length(), (poses.indexOf(p)+1)+"."+CAN_PLACE_WORKER);
            cursor+=String.valueOf(poses.indexOf(p)+1).length()+1+CAN_PLACE_WORKER.length();
            bb.replace(cursor, cursor+2+String.valueOf(p.getX()).length()+String.valueOf(p.getY()).length(), p.getX()+", "+p.getY());
        }
    }


    /**
     * prints in message box the allowed actions to choose from
     * @param allowedActions to choose from
     */
    public void printAllowedActions(ArrayList<Action> allowedActions) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        bb.replace(cursor, cursor+RECEIVED_ALLOWED_ACTIONS.length(), RECEIVED_ALLOWED_ACTIONS);
        for(Action a : allowedActions) {
            if(allowedActions.size()<24)
                cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), cursor)+1))+rstColor.length()+4;
            else
                cursor = bb.indexOf(rstColor + "| ", bb.indexOf(String.valueOf(nl), cursor+1))+rstColor.length()+4;
            String indexing = (allowedActions.indexOf(a)+1) +". ";
            bb.replace(cursor, cursor+indexing.length()+CAN.length(), indexing+CAN);
            cursor+=indexing.length()+CAN.length()+1;
            bb.replace(cursor, cursor+a.getActionType().toString().length(), a.getActionType().toString().toUpperCase());
            cursor+=a.getActionType().toString().length()+1;
            if(a.getFrom()!=null) {
                bb.replace(cursor, cursor+FROM.length(), FROM);
                cursor+=FROM.length()+1;
                bb.replace(cursor, cursor + 2 + String.valueOf(a.getFrom().getX()).length() + String.valueOf(a.getFrom().getY()).length(), a.getFrom().getX() + ", " + a.getFrom().getY());
                cursor+= 3 + String.valueOf(a.getFrom().getX()).length() + String.valueOf(a.getFrom().getY()).length();
            }
            if(a.getTo()!=null) {
                bb.replace(cursor, cursor+TO.length(), TO);
                cursor+=TO.length()+1;
                bb.replace(cursor, cursor + 2 + String.valueOf(a.getTo().getX()).length() + String.valueOf(a.getTo().getY()).length(), a.getTo().getX() + ", " + a.getTo().getY());
                cursor+= 3 + String.valueOf(a.getTo().getX()).length() + String.valueOf(a.getTo().getY()).length();
            }
            if(a.hasForcedMove()) {
                Action fm = a.getForcedMove();
                bb.replace(cursor, cursor+FORCE.length(), FORCE);
                cursor+=FORCE.length()+1;
                bb.replace(cursor, cursor+FROM.length(), FROM);
                cursor+=FROM.length()+1;
                bb.replace(cursor, cursor + 2 + String.valueOf(fm.getFrom().getX()).length() +
                        String.valueOf(fm.getFrom().getY()).length(), fm.getFrom().getX() + ", " + fm.getFrom().getY());
                cursor+= 3 + String.valueOf(fm.getFrom().getX()).length() + String.valueOf(fm.getFrom().getY()).length();
                bb.replace(cursor, cursor+TO.length(), TO);
                cursor+=TO.length()+1;
                bb.replace(cursor, cursor + 2 + String.valueOf(fm.getTo().getX()).length() + String.valueOf(fm.getTo().getY()).length(), fm.getTo().getX() + ", " + fm.getTo().getY());
            }
            if(a.getBuildLevel()!=null) {
                bb.replace(cursor, cursor+1, "a");
                cursor+=2;
                bb.replace(cursor, cursor+a.getBuildLevel().toString().length(), a.getBuildLevel().toString().toUpperCase());
            }
        }
    }


    /**
     * to be called when a player lose the game. this notifies it in the message box and removes his box
     * @param username that lost the game
     */
    public void printPlayerLost(String username) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        String whoHasLost = username.equals(YOU) ? YOU_LOSS : PLAYER_LOSS;
        bb.replace(cursor, cursor+username.length()+1+whoHasLost.length(), username+" "+whoHasLost);
    }


    public void printPlayerWin(String username) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        String whoHasWon = username.equals(YOU) ? YOU_WON : PLAYER_WON;
        bb.replace(cursor, cursor+username.length()+1+whoHasWon.length(), username+" "+whoHasWon);
    }

    /**
     * to be called when a player is unreachable before the printGameFinished (no need to call printPlayerLost
     * @param username of the player unreachable
     */
    public void printPlayerUnreachable(String username) {
        clearMessageBox();
        int cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box"))+1))+rstColor.length()+4;
        bb.replace(cursor, cursor+username.length()+CANNOT_REACH_A_PLAYER.length()+1, username+" "+CANNOT_REACH_A_PLAYER);
    }


    /**
     * to be called when received the message game finished from the server
     */
    public void printGameFinished() {
        System.out.println("Game is finished");
    }


    /**
     * to be called when a worker has been placed in a position
     * @param color of the worker to draw
     * @param pos where to draw the worker
     */
    public void drawWorker(Color color, Point pos) {
        String colour = convertColor(color);
        int cursor = getStartingCellIndex(pos);
        for(int line=0; line<(cellDimY/2)-worker_dim/2-1; line++)
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
        for(int wLine=0; wLine<worker_dim; wLine++) {
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
            cursor = getCellIndexBorderFromSide(cursor, pos.getX());
            cursor = bb.indexOf(String.valueOf(s), cursor);
            cursor += cellDimX/2 -worker_dim/2-1;
            bb.insert(cursor, colour);
            cursor += colour.length();
            String color_bold = esc + "1m";
            bb.insert(cursor, color_bold);
            cursor+= color_bold.length();
            String w_sym;
            if(wLine==0)
                w_sym = worker_icon_high;
            else if(wLine==1)
                w_sym = worker_icon_medUp;
            else if(wLine==2)
                w_sym = worker_icon_medLow;
            else w_sym = worker_icon_low;
            bb.replace(cursor, cursor+5, w_sym);
        }
    }


    /**
     * to be called when a worker is removed from a position because has been moved
     * @param pos to remove the worker from
     */
    public void removeWorker(Point pos) {
        int cursor = getStartingCellIndex(pos);
        for(int line=0; line<(cellDimY/2-4/2-1); line++)
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
        for(int wLine=0; wLine<4; wLine++) {
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
            cursor = getCellIndexBorderFromSide(cursor, pos.getX());
            cursor = bb.indexOf(String.valueOf(s), cursor);
            bb.delete(cursor, bb.indexOf(verticalLineSegment, cursor)-rstColor.length());
            StringBuilder sp= new StringBuilder();
            for(int spaces=1; spaces<cellDimX-1; spaces++)
                sp.append(s);
            bb.insert(cursor, sp);
        }
    }


    /**
     * to be called when received a board with a variation of level from the previous version
     * @param pos cell of the board where to draw the level
     * @param level is the build level target
     */
    public void drawLevel(Point pos, BuildLevel level) {
        String levName = null;
        String levCol = null;
        switch (level) {
            case LEVEL_1:
                levName = "LV.1";
                levCol = color_bg_level1;
                break;
            case LEVEL_2:
                levName = "LV.2";
                levCol = color_bg_level2;
                break;
            case LEVEL_3:
                levName = "LV.3";
                levCol = color_bg_level3;
                break;
            case DOME:
                drawDome(pos);
                return;
        }

        int cursor = getStartingCellIndex(pos);
        for(int cellOffset=1; cellOffset<cellDimY-1; cellOffset++) {
            cursor = getCellIndexBorderFromSide(bb.indexOf(String.valueOf(nl), cursor)+1, pos.getX())+1;
            bb.replace(cursor, bb.indexOf("m", cursor)+1, levCol);
            if(cellOffset==1) {
                cursor+=levCol.length();
                if(bb.substring(cursor, cursor+2).equals(esc))
                    cursor = bb.indexOf("m", cursor) + 1;
                else {
                    bb.insert(cursor, color_black);
                    cursor += color_black.length();
                }
                bb.replace(cursor, cursor + levName.length(), levName);
            }
        }
    }


    /**
     * removes the top level (dome-over-different-than-level3 safe)
     * @param pos of the cell to remove top level
     */
    public void removeLevel(Point pos) {
        int cursor = getStartingCellIndex(pos);
        cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
        cursor = getCellIndexBorderFromSide(cursor, pos.getX());
        String sub = bb.substring(cursor, cursor+color_bg_level3.length()+rstColor.length()+cellDimX);
        if(sub.contains("LV.1")) {
            destroyBuild(cursor+1, pos);
        } else if(sub.contains("LV.2"))
            drawLevel(pos, BuildLevel.LEVEL_1);
        else if(sub.contains("LV.3"))
            drawLevel(pos, BuildLevel.LEVEL_2);
        else {
            restoreTile(pos);
            if(sub.contains(color_bg_level1))
                drawLevel(pos, BuildLevel.LEVEL_1);
            else if(sub.contains(color_bg_level2))
                drawLevel(pos, BuildLevel.LEVEL_2);
            else if(sub.contains(color_bg_level3))
                drawLevel(pos, BuildLevel.LEVEL_3);
        }
    }


    /**
     * prints a message a tge bottom of the message box if there's one
     * @param message to print
     */
    public void printMessage(String message) {
        if(bb.indexOf("Message Box")!=-1) {
            clearLowSpace();
            int cursor;
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf("| └", bb.indexOf("| └")+2))+rstColor.length()+2;
            bb.replace(cursor, cursor+message.length(), message);
        }
    }


    public void printTimer(String message) {
        if(message!=null) {
            bufferScreen();
            out.print("\u001b[77;132H" + reversed + bold + message + rstColor + restore_cursor);
            out.flush();
        }
    }


    private void bufferScreen() {
        out.print(save_cursor + move_cursor_to_start_column + clear_begin_to_curs_screen + move_cursor_to_topleft + bb.toString() + move_cursor_to_topleft + restore_cursor);
    }


    /**
     * creates the players rotation boxes and message box
     * @param cursor to start from in string builder
     * @param players rotation for their boxes
     */
    private void createInformationInterfaces(int cursor, CircularList<PlayerDetails> players) {
        if(bb.charAt(cursor)!=topLeftMonoCornerMap.charAt(0)) {
            bb.replace(cursor, ++cursor, topLeftMonoCornerMap);
            for (int pl = 0; pl < players.size(); pl++) {
                for (int j = 1; j < 9 * aspectRatio.x; j++)
                    bb.replace(cursor, ++cursor, String.valueOf(horizontalMonoLineSegment));
                bb.replace(cursor, ++cursor, topCellMonoSeparator);
            }
            bb.replace(cursor - 1, cursor, topRightMonoCornerMap);
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            //username
            bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
            for (int plIndx = 0; plIndx < players.size(); plIndx++) {
                String username = players.getFirst().getUsername();
                cursor += (4 * aspectRatio.x + aspectRatio.x / 2) - (username.length() / 2);
                if (players.getFirst().hasAColor()) {
                    String color = convertColor(players.getFirst().getColor());
                    bb.insert(cursor-1, color);
                    cursor += color.length();
                    bb.replace(cursor, cursor + username.length(), username);
                    cursor += username.length()+1;
                    bb.insert(cursor, rstColor);
                    cursor += rstColor.length();
                } else {
                    bb.replace(cursor, cursor + username.length(), username);
                    cursor += username.length();
                }
                cursor += (4 * aspectRatio.x + aspectRatio.x / 2 + 1) - (username.length() / 2 + username.length() % 2);
                bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
                players.goNext();
            }
            // 1 line spacing
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
            for (int plIndx = 0; plIndx < players.size(); plIndx++) {
                cursor += 9 * aspectRatio.x;
                bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
            }
            //card name
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            bb.replace(cursor, ++cursor, String.valueOf(verticalMonoLineSegment));
            for (int playerCardIndx = 0; playerCardIndx < players.size(); playerCardIndx++) {
                cursor++;
                bb.replace(cursor, cursor + 5, "Card:");
                cursor += 9 * aspectRatio.x - 2;
                bb.replace(cursor, ++cursor, String.valueOf(verticalMonoLineSegment));
            }
            //1 line spacing
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
            for (int plIndx = 0; plIndx < players.size(); plIndx++) {
                cursor += 9 * aspectRatio.x;
                bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
            }
            //card description
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            bb.replace(cursor, ++cursor, String.valueOf(verticalMonoLineSegment));
            for (int playerCardIndx = 0; playerCardIndx < players.size(); playerCardIndx++) {
                cursor++;
                bb.replace(cursor, cursor + 12, "Description:");
                cursor += 9 * aspectRatio.x - 2;
                bb.replace(cursor, ++cursor, String.valueOf(verticalMonoLineSegment));
            }
            cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            for (int i = 0; i < 10; i++) {
                bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
                for (int playerCardIndx = 0; playerCardIndx < players.size(); playerCardIndx++) {
                    cursor += 9 * aspectRatio.x;
                    bb.replace(cursor, cursor + 1, String.valueOf(verticalMonoLineSegment));
                }
                cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), cursor))+rstColor.length()+2;
            }
            bb.replace(cursor, ++cursor, downLeftMonoCornerMap);
            for (int pl = 0; pl < players.size(); pl++) {
                for (int j = 1; j < 9 * aspectRatio.x; j++)
                    bb.replace(cursor, ++cursor, String.valueOf(horizontalMonoLineSegment));
                bb.replace(cursor, ++cursor, downCellMonoSeparator);
            }
            bb.replace(cursor - 1, cursor, downRightMonoCornerMap);

            //spacing
            for(int i=0; i<2; i++) cursor = bb.indexOf(String.valueOf(nl), cursor)+1;

            //box of messages
            cursor = bb.indexOf(rstColor+"| ", cursor)+rstColor.length()+2;
            bb.replace(cursor, ++cursor, topLeftMonoCornerMap);
            for (int j = 1; j < 27 * aspectRatio.x; j++)
                bb.replace(cursor, ++cursor, String.valueOf(horizontalMonoLineSegment));
            bb.replace(cursor, ++cursor, topRightMonoCornerMap);
            cursor = bb.indexOf(rstColor+"| ", cursor)+rstColor.length()+2;
            bb.replace(cursor, cursor+1, String.valueOf(verticalMonoLineSegment));
            cursor += (13*aspectRatio.x+aspectRatio.x/2)-"Message Box".length()/2;
            bb.replace(cursor, cursor+"Message Box".length(), "Message Box");
            cursor += (13*aspectRatio.x+aspectRatio.x/2)+"Message Box".length()/2+1;
            bb.replace(cursor, ++cursor, String.valueOf(verticalMonoLineSegment));
            for(int i=0; i<54; i++) {
                cursor = bb.indexOf(rstColor+"| ", cursor)+rstColor.length()+2;
                bb.replace(cursor, ++cursor, String.valueOf(verticalMonoLineSegment));
                cursor += 27*aspectRatio.x-1;
                bb.replace(cursor, cursor+1, String.valueOf(verticalMonoLineSegment));
            }
            cursor = bb.indexOf(rstColor+"| ", cursor)+rstColor.length()+2;
            bb.replace(cursor, ++cursor, downLeftMonoCornerMap);
            for (int j = 1; j < 27 * aspectRatio.x; j++)
                bb.replace(cursor, ++cursor, String.valueOf(horizontalMonoLineSegment));
            bb.replace(cursor, ++cursor, downRightMonoCornerMap);
            //spacing
            for(int i=0; i<4; i++) cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
            cursor = bb.indexOf(rstColor+"| ", cursor)+rstColor.length()+2;
            bb.replace(cursor, cursor+2, ">>");
        }

    }


    /**
     * clears the message box before printing new messages
     */
    private void clearMessageBox() {
        int cursor=0;
        for(int i=0; i<20; i++) cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
        cursor = bb.indexOf(rstColor+"| ", bb.indexOf(String.valueOf(nl), bb.indexOf("Message Box")))+rstColor.length()+1;
        StringBuilder sps = new StringBuilder();
        for(int n=0; n<27*aspectRatio.x-1; n++)
            sps.append(s);
        for(int i=0; i<53; i++) {
            cursor = bb.indexOf("| |", cursor)+3;
            bb.replace(cursor, bb.indexOf("|\n", cursor+4), sps.toString());
        }
    }


    private void clearLowSpace() {
        int cursor;
        cursor = bb.indexOf(rstColor+"| ", bb.indexOf("| └", bb.indexOf("| └")+2))+rstColor.length()+2;
        StringBuilder sps = new StringBuilder();
        for(int n=0; n<40; n++)
            sps.append(s);
        bb.replace(cursor, cursor+40, sps.toString());
    }

    /**
     * converts a color from model version in cli-printable version
     * @param colour to convert
     * @return color converted
     */
    private String convertColor(Color colour) {
        Triplet<Integer, Integer, Integer> trip = colour.getRGBValues();
        //workers
        String color_24bit = esc + "38;2;";
        return color_24bit + colour.getRGBValues().x + ";" + colour.getRGBValues().y + ";" + colour.getRGBValues().z + "m";
    }


    /**
     * called if underneath there's grass
     * @param cursor starting from
     * @param pos of the level to remove
     */
    private void destroyBuild(int cursor, Point pos) {
        int endCurs;
        StringBuilder sps = new StringBuilder();
        for(int j=1; j<cellDimX-1; j++) sps.append(s);
        endCurs = bb.indexOf(verticalLineSegment, cursor)-rstColor.length();
        bb.replace(cursor, endCurs, sps.toString());
        bb.insert(cursor, color_bg_grass);
        for(int i=2; i<cellDimY-1; i++) {
            cursor = getCellIndexBorderFromSide(bb.indexOf(String.valueOf(nl), cursor)+1, pos.getX())+1;
            bb.replace(cursor, bb.indexOf("m", cursor)+1, color_bg_grass);
        }
    }


    /**
     * called by removeLevel if there's a dome that has to be removed (so there's no worker in that tile), clears the tile and set to ground
     * @param pos is the cell of the map
     */
    private void restoreTile(Point pos) {
        int cursor = getStartingCellIndex(pos);
        int endCurs;
        StringBuilder sps = new StringBuilder();
        for(int j=1; j<cellDimX-1; j++) sps.append(s);
        for(int i=1; i<cellDimY-1; i++) {
            cursor = bb.indexOf(String.valueOf(nl), cursor) + 1;
            cursor = getCellIndexBorderFromSide(cursor, pos.getX())+1;
            endCurs = bb.indexOf(verticalLineSegment, cursor)-rstColor.length();
            bb.replace(cursor, endCurs, sps.toString());
        }
        bb.replace(cursor+cellDimX/2-4, cursor+cellDimX/2+2, "("+pos.getX()+", "+pos.getY()+")");
        bb.insert(cursor+cellDimX/2-4, color_black);
        cursor = getStartingCellIndex(pos);
        for(int i=1; i<cellDimX-1; i++) {
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
            cursor = getCellIndexBorderFromSide(cursor, pos.getX())+1;
            bb.insert(cursor, color_bg_grass);
            cursor = bb.indexOf(verticalLineSegment, cursor)-rstColor.length();
        }
    }


    /**
     * draws the dome
     * @param pos in this pos
     */
    private void drawDome(Point pos) {
        int cursor = getStartingCellIndex(pos);
        cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
        cursor = getCellIndexBorderFromSide(cursor, pos.getX())+1;
        String previousColor = bb.substring(cursor, bb.indexOf("m", cursor)+1);
        cursor = bb.indexOf("m", cursor)+1;
        if(bb.substring(cursor, cursor+2).equals(esc))
            cursor = bb.indexOf("m", cursor)+1;
        else {
            bb.insert(cursor, color_black);
            cursor += color_black.length();
        }
        bb.replace(cursor, cursor+"DOME".length(), "DOME");
        for(int i=1; i<cellDimY-1; i++) {
            if(i==1) {
                cursor = bb.indexOf(String.valueOf(s), cursor);
                bb.insert(cursor, color_bg_dome);
                cursor+=color_bg_dome.length();
                cursor = bb.indexOf(rstColor, cursor);
                cursor -= "DOME".length();
            } else {
                int spacing = 0;
                if(i==cellDimY-2)
                    spacing = 4;
                else if(i==2 || i==cellDimY-3)
                    spacing = 2;
                cursor = bb.indexOf(String.valueOf(s), cursor) + spacing;
                bb.insert(cursor, color_bg_dome);
                cursor += color_bg_dome.length();
                cursor = bb.indexOf(rstColor, cursor)-spacing;
            }
            bb.insert(cursor, previousColor);
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
            cursor = getCellIndexBorderFromSide(cursor, pos.getX());
        }
    }


    /**
     * fills all cells with grass (for ground)
     */
    private void fillCellsWithGrass() {
        int cursor = bb.indexOf(topLeftCornerBoard);
        for(int vCell=0; vCell<5; vCell++) {
            for(int cellOffset=1; cellOffset<cellDimY-1; cellOffset++) {
                cursor = getCellIndexBorderFromSide(bb.indexOf(String.valueOf(nl), cursor) + 1, 0) + 1;
                for (int hCell = 0; hCell < 5; hCell++) {
                    bb.insert(cursor, color_bg_grass);
                    cursor = bb.indexOf(verticalLineSegment, cursor);
                    bb.insert(cursor, rstColor);
                    cursor = bb.indexOf(verticalLineSegment, cursor) + 1;
                }
            }
            cursor = bb.indexOf(String.valueOf(nl), cursor)+1;
        }
    }


    /**
     * gets the index of the border of a cell from this cursor
     * @param cursor from this
     * @param xCell x coordinate of the cell
     * @return the index in the array
     */
    private int getCellIndexBorderFromSide(int cursor, int xCell) {
        int currCurs = bb.indexOf(verticalLineSegment, cursor)+1;
        for(int cell=0; cell<xCell; cell++)
            currCurs = bb.indexOf(verticalLineSegment, currCurs)+1;
        return currCurs-1;
    }


    /**
     * goes to the top left corner of a cell
     * @param cell coordinate
     * @return index of the corner of the cell
     */
    private int getStartingCellIndex(Point cell) {
        int cursorStartingCell=0;
        int startCell;
        //line 1 cell 1
        startCell = bb.indexOf(topLeftCornerBoard, 0);
        if(cell.getX()==0 && cell.getY()==0)
            return startCell;
        cursorStartingCell = startCell+1;
        //line 1 cell 2->5
        for(int j=1; j<5; j++) {
            startCell = bb.indexOf(topCellSeparator, cursorStartingCell);
            if(cell.getX()==j && cell.getY()==0)
                return startCell;
            cursorStartingCell = startCell+1;
        }
        //line 2->5
        for(int i=1; i<5; i++) {
            //cell 1
            startCell = bb.indexOf(verticalLeftCellSeparator, cursorStartingCell);
            if(cell.getX()==0 && cell.getY()==i)
                return startCell;
            cursorStartingCell = startCell+1;
            //cell 2->5
            for (int j = 1; j < 5; j++) {
                startCell = bb.indexOf(crossCellSeparator, cursorStartingCell);
                if(cell.getX()==j && cell.getY()==i)
                    return startCell;
                cursorStartingCell = startCell+1;
            }
        }
        return -1;
    }


    /**
     * allocates an empty board when the cli is launched
     */
    private void createEmptyBoard() {
        //Top decorator
        attachTopSea();
        attachTopBeach();

        //Board
        //cell row 1
        attachLeftBeach();
        bb.append(topLeftCornerBoard);
        for (int cell=0; cell<4; cell++) {
            fillWith(horizontalLineSegment);
            bb.append(topCellSeparator);
        }
        fillWith(horizontalLineSegment);
        bb.append(topRightCornerBoard);
        attachRightBeach();
        nextLine();

        for(int i=1; i<cellDimY-1; i++) {
            attachLeftBeach();
            for (int hCell = 0; hCell < 5; hCell++) {
                bb.append(verticalLineSegment);
                fillWith(String.valueOf(s));
            }
            bb.append(verticalLineSegment);
            attachRightBeach();
            nextLine();
        }
        //cells row 2->4
        for(int vCell=1; vCell<5; vCell++) {
            //top cell
            attachLeftBeach();
            bb.append(verticalLeftCellSeparator);
            for (int cell = 0; cell < 4; cell++) {
                fillWith(horizontalLineSegment);
                bb.append(crossCellSeparator);
            }
            fillWith(horizontalLineSegment);
            bb.append(verticalRightCellSeparator);
            attachRightBeach();
            nextLine();
            //inside cell
            for (int i = 1; i < cellDimY - 1; i++) {
                attachLeftBeach();
                for (int hCell = 0; hCell < 5; hCell++) {
                    bb.append(verticalLineSegment);
                    fillWith(String.valueOf(s));
                }
                bb.append(verticalLineSegment);
                attachRightBeach();
                nextLine();
            }
        }
        attachLeftBeach();
        bb.append(downLeftCornerBoard);
        for (int cell=0; cell<4; cell++) {
            fillWith(horizontalLineSegment);
            bb.append(downCellSeparator);
        }
        fillWith(horizontalLineSegment);
        bb.append(downRightCornerBoard);
        attachRightBeach();
        nextLine();

        //Lower decorator
        attachLowBeach();
        attachLowSea();

        fillCellsWithGrass();
        insertCoordinatesInCells();
    }


    /**
     * inserts all each coordinate at the bottom of each cell
     */
    private void insertCoordinatesInCells() {
        for(int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                int cursor = getStartingCellIndex(new Point(j, i));
                for(int ii=1; ii<cellDimY-1; ii++)
                    cursor = getCellIndexBorderFromSide(bb.indexOf(String.valueOf(nl), cursor), j)+1;
                cursor += color_bg_grass.length() + cellDimX/2 - 4;
                bb.insert(cursor, color_black);
                cursor+=color_black.length();
                bb.replace(cursor, cursor+6, "("+j+", "+i+")");
            }
        }
    }

    /**
     * allocates the space to the right for players and the message box
     */
    private void insertSpaceForPlayers() {
        for(int j=0; j<27*aspectRatio.x; j++) {
            bb.append(s);
        }
        bb.append("  ");
    }

    /**
     * inserts next lines
     */
    private void nextLine() {
        bb.append(verticalMonoLineSegment);
        insertSpaceForPlayers();
        bb.append(nl);
    }

    /**
     * environmental functions
     */
    private void attachTopSea() {
        bb.append(topLeftMonoCornerMap);
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachLines();
        for (int x = 0; x < aspectRatio.x-1; x++) {
            bb.append(horizontalMonoLineSegment);
            bb.append(horizontalMonoLineSegment);
        }
        for (int x = 0; x < 5 * cellSize * aspectRatio.x - 4; x++)
            bb.append(horizontalMonoLineSegment);
        for (int x = 0; x < aspectRatio.x-1; x++) {
            bb.append(horizontalMonoLineSegment);
            bb.append(horizontalMonoLineSegment);
        }
        for (int x = 0; x < aspectRatio.x-1; x++) {
            attachLines();
        }
        bb.append(topRightMonoCornerMap);
        insertSpaceForPlayers();
        bb.append(nl);

        for(int y=0; y<3*aspectRatio.y-1; y++) {
            bb.append(verticalMonoLineSegment);
            bb.append(color_water);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            bb.append(color_water);
            for (int x = 0; x < aspectRatio.x-1; x++) {
                bb.append(water);
                bb.append(water);
            }
            bb.append(color_water);
            for (int x = 0; x < 5 * cellSize * aspectRatio.x - 4; x++)
                bb.append(water);
            bb.append(color_water);
            for (int x = 0; x < aspectRatio.x-1; x++) {
                bb.append(water);
                bb.append(water);
            }
            for (int x = 0; x < aspectRatio.x-1; x++) {
                attachWater();
            }

            bb.append(rstColor);
            nextLine();
        }
    }

    /**
     * environmental functions
     */
    private void attachTopBeach() {
        for(int y=0; y<aspectRatio.y; y++) {
            bb.append(verticalMonoLineSegment);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachSand();
            bb.append(color_bg_gravel);
            for (int x = 0; x < 5*cellSize*aspectRatio.x-4; x++)
                bb.append(sand);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachSand();
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            nextLine();
        }
    }

    /**
     * environmental functions
     */
    private void attachWater() {
        bb.append(color_water);
        bb.append(water).append(water);
        bb.append(rstColor);
    }

    /**
     * environmental functions
     */
    private void attachLines() {
        bb.append(horizontalMonoLineSegment).append(horizontalMonoLineSegment);
    }

    /**
     * environmental functions
     */
    private void attachLowBeach() {
        for(int y=0; y<aspectRatio.y; y++) {
            bb.append(verticalMonoLineSegment);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachSand();
            bb.append(color_bg_gravel);
            for (int x = 0; x < 5*cellSize*aspectRatio.x-4; x++)
                bb.append(sand);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachSand();
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            nextLine();
        }
    }

    /**
     * environmental functions
     */
    private void attachLowSea() {
        for(int y=0; y<3*aspectRatio.y-1; y++) {
            bb.append(verticalMonoLineSegment);
            bb.append(color_water);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            bb.append(color_water);
            for (int x = 0; x < aspectRatio.x-1; x++)
                bb.append(water).append(water);
            bb.append(color_water);
            for (int x = 0; x < 5 * cellSize * aspectRatio.x - 4; x++)
                bb.append(water);
            bb.append(color_water);
            for (int x = 0; x < aspectRatio.x-1; x++)
                bb.append(water).append(water);
            for (int x = 0; x < aspectRatio.x-1; x++)
                attachWater();
            bb.append(rstColor);
            nextLine();
        }

        bb.append(downLeftMonoCornerMap);
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachLines();
        for (int x = 0; x < aspectRatio.x-1; x++)
            bb.append(horizontalMonoLineSegment).append(horizontalMonoLineSegment);
        for (int x = 0; x < 5 * cellSize * aspectRatio.x - 4; x++)
            bb.append(horizontalMonoLineSegment);
        for (int x = 0; x < aspectRatio.x-1; x++)
            bb.append(horizontalMonoLineSegment).append(horizontalMonoLineSegment);
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachLines();
        bb.append(downRightMonoCornerMap);
        insertSpaceForPlayers();
    }

    /**
     * environmental functions
     */
    private void attachSand() {
        bb.append(color_bg_gravel);
        for(int x=0; x<2; x++)
            bb.append(sand);
        bb.append(rstColor);
    }

    /**
     * environmental functions
     */
    private void attachLeftBeach() {
        bb.append(verticalMonoLineSegment);
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachWater();
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachSand();
    }

    /**
     * environmental functions
     */
    private void attachRightBeach() {
        bb.append(color_bg_gravel);
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachSand();
        for (int x = 0; x < aspectRatio.x-1; x++)
            attachWater();
    }

    /**
     * fills a line with element
     */
    private void fillWith(String elem) {
        for(int x=0; x<cellSize*aspectRatio.x-2; x++)
            bb.append(elem);
    }


    /*------------------------------------------------------------Testing--------------------------------------------------*/


    /*
    public static void main(String[] args) {
        ModelInfo m = new ModelInfo();
        CliFramework c = new CliFramework(m);
        c.drawLevel(new Point(0, 0), BuildLevel.DOME);
        c.removeLevel(new Point(0, 0));
        c.printScreen();

     */
        /*
        m.setUsername("Gaggio");
        CircularList<PlayerDetails> pl = new CircularList<>();
        pl.add(new PlayerDetails("Marco", new CardDetails("Apollo", "God Of Music\nYour Move: Your Worker may move into an opponent Worker\u0027s space by forcing their Worker to the space yours just vacated."), new Color("pink"), null));
        pl.add(new PlayerDetails("Giovanna", null, new Color("purple"), null));
        pl.add(new PlayerDetails("Gaggio", null, new Color("orange"), null));
        c.updatePlayers(pl);
        c.printScreen();
        */

        /*
        c.printInputNotValid();
        c.printEnemyTurn("Marco");
        ArrayList<Tuple<String, String>> car = new ArrayList<>();
        car.add(new Tuple<>("Apollo", "God Of Music\nYour Move: Your Worker may move into an opponent Worker\u0027s space by forcing their Worker to the space yours just vacated."));
        car.add(new Tuple<>("Prometheus", "Titan Benefactor of Mankind\nYour Turn: If your Worker does not move up, it may build both before and after moving."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        car.add(new Tuple<>("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."));
        c.printCardsSelection(car, 1);
        c.printScreen();
        ArrayList<Point> p = new ArrayList<>();
        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++)
                p.add(new Point(j, i));
        c.printAvailablePosesPerWorker(p);
        c.printScreen();
        ArrayList<Action> al = new ArrayList<>();
        Action a = new Action(null, Action.ActionType.MOVE, new Point(1, 0), new Point(0, 0), null, null);
        al.add(new Action(null, Action.ActionType.PASS, null, null, null, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.BUILD, new Point(0, 0), new Point(0, 0), null, BuildLevel.LEVEL_1));
        al.add(new Action(null, Action.ActionType.PASS, null, null, null, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.BUILD, new Point(0, 0), new Point(0, 0), null, BuildLevel.LEVEL_1));
        al.add(new Action(null, Action.ActionType.PASS, null, null, null, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.MOVE, new Point(0, 0), new Point(0, 1), a, null));
        al.add(new Action(null, Action.ActionType.PASS, null, null, null, null));
        c.printAllowedActions(al);
        c.printScreen();
         */
        /*
        c.printPlayerLost("Marco");
        c.printScreen();
         */
       /*
        c.printPlayerUnreachable("Marco");
        c.printScreen();*/
        /*c.drawWorker(new Color("pink"), new Point(0, 0));
        c.removeWorker(new Point(0, 0));
        c.printScreen();
        c.drawLevel(new Point(0, 0), BuildLevel.DOME);
        c.printScreen();
        c.removeLevel(new Point(0, 0));
        c.printScreen(); */
        /*ArrayList<String> strs = new ArrayList<>(3);
        strs.add("Marco");
        strs.add("Giovanna");
        strs.add("Gaggio");
        c.printPlayerToChoose(strs);
        c.printScreen();
        strs.clear();
        strs.add("pink");
        strs.add("purple");
        strs.add("orange");
        c.printColorToChoose(strs);
        c.printScreen();*/
        /*
        pl.removeFirst();
        pl.removeLast();
        pl.add(new PlayerDetails("Gaggio", new CardDetails("Poseidon", "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."), new Color("orange"), null));
        c.updatePlayers(pl);
        c.printScreen();
         */
    //}

}