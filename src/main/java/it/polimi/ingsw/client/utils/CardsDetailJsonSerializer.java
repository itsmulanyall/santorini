package it.polimi.ingsw.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.shared.model.CardDetails;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CardsDetailJsonSerializer {

    /**
     * serializes all the cards
     * @param args none
     * @throws IOException if cannot write file
     */
    public static void main( String[] args ) throws IOException {
        String name;
        String description;
        CardDetails c;
        BufferedWriter bw;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String folder = "./src/main/resources/client/godCardsDetails/";

        name = "Apollo";
        description = "God Of Music\nYour Move: Your Worker may move into an opponent Worker\u0027s space by forcing their Worker to the space yours just vacated.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Artemis";
        description = "Goddess of the Hunt\nYour Move: Your Worker may move one additional time, but not back to its initial space.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Athena";
        description = "Goddess of Wisdom\nOpponent\u0027s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Atlas";
        description = "Titan Shouldering the Heavens\nYour Build: Your Worker may build a dome at any level.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Demeter";
        description = "Goddess of the Harvest\nYour Build: Your Worker may build one additional time, but not on the same space.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Hephaestus";
        description = "God of Blacksmiths\nYour Build: Your Worker may build one additional block (not dome) on top of your first block.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Minotaur";
        description = "Bull-headed Monster\nYour Move: Your Worker may move into an opponent Worker\u0027s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Pan";
        description = "God of the Wild\nWin Condition: You also win if your Worker moves down two or more levels.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Prometheus";
        description = "Titan Benefactor of Mankind\nYour Turn: If your Worker does not move up, it may build both before and after moving.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Hera";
        description = "Goddess of Marriage\nOpponent\u0027s Turn: An opponent cannot win by moving into a perimeter space.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Hestia";
        description = "Goddess of Heart and Home\nYour Build: Your Worker may build one additional time, but this cannot be on a perimeter space.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Poseidon";
        description = "God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Triton";
        description = "God of the Waves\nYour Move: Each time your Worker moves into a perimeter space, it may immediately move again.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

        name = "Zeus";
        description = "God of the Sky\nYour Build: Your Worker may build a block under itself.";
        c = new CardDetails(name, description);
        bw = new BufferedWriter(new FileWriter(folder + name + "_godCard.json"));
        gson.toJson(c, bw);
        bw.flush();
        bw.close();

    }

}
