package it.polimi.ingsw.client.utils;

import com.google.gson.Gson;
import it.polimi.ingsw.shared.model.CardDetails;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CardsDetailJsonDeserializer {


    /**
     * reads name and description of every card from files in resources -> client/
     * @return all cards name and description as arraylist of cardDetails
     */
    public ArrayList<CardDetails> getDetailedCards(ArrayList<String> cardsNames){

        Gson gson = new Gson();
        ArrayList<CardDetails> cardsDetails = new ArrayList<>(14);
        final String path = "client/godCardsDetails";
        File jarFile = new File(CardsDetailJsonDeserializer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if(jarFile.isFile()) {  // Run with JAR file
            try {
                JarFile jar = new JarFile(jarFile);
                for(String cardName : cardsNames) {
                    for(Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                        final String name = entries.nextElement().getName();
                        if (name.contains(path) && name.contains(cardName+"_godCard")) { //filter according to the path and the name of the card
                            InputStream is = CardsDetailJsonDeserializer.class.getClassLoader().getResourceAsStream(name);
                            InputStreamReader br = new InputStreamReader(is);
                            cardsDetails.add(gson.fromJson(br, CardDetails.class));
                        }
                    }
                }
                jar.close();
            }catch (Exception ignored){}
        } else { // Run with IDE
            final URL url = CardsDetailJsonDeserializer.class.getResource("/"+path);
            if (url != null) {
                try {
                    final File files = new File(url.toURI());
                    for(String cardName : cardsNames) {
                        for (File file : files.listFiles()) {
                            if(file.getName().contains(cardName+"_godCard.json")) { //filters according to the name card
                                FileReader fr = new FileReader(file);
                                BufferedReader br = new BufferedReader(fr);
                                cardsDetails.add(gson.fromJson(br, CardDetails.class));
                            }
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
        return cardsDetails;
    }
}
