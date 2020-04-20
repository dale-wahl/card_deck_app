package io.github.dalewahl.carddecks;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

import io.github.dalewahl.carddecks.database.Card;
import io.github.dalewahl.carddecks.database.Category;
import io.github.dalewahl.carddecks.database.Deck;

public class csvBuildDeck {
    InputStream inputStream;

    public csvBuildDeck(InputStream inputStream) {
        this.inputStream = inputStream;
        long deck_id = 0;

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line = "";
        //MainActivity.database.clearAllTables();
        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(";");

                if (tokens[0].equals("Deck")) {
                    //Log.d("csvBuildDeck" , "Token Length:" + String.valueOf(tokens.length));
                    if (MainActivity.database.deckDao().deckNames().contains(tokens[1])) {
                        Log.d("csvBuildDeck", "Deck already loaded:" + tokens[1]);
                        break;
                    }
                    if (tokens.length != 3) {
                        Log.d("csvBuildDeck", "Deck format incorrect.");
                        break;
                    }
                    Deck deck = new Deck();
                    deck.name = tokens[1];
                    deck.language = tokens[2];
                    deck.last = true;
                    deck_id = MainActivity.database.deckDao().insertDeck(deck);
                    Log.d("csvBuildDeck", "New Deck ID:" + deck_id);
                    Log.d("Loading Deck", "Loaded:" + Arrays.toString(tokens));
                    continue;
                }
                if (tokens[0].equals("Categories")) {
                    if (deck_id == 0) {
                        Log.d("Loading Categories", "deck_id is null; categories can't load");
                        continue;
                    }
                    int counter = 0;
                    for (String cat : tokens) {
                        if (counter == 0) {
                            counter++;
                            continue;
                        }
                        Category category = new Category();
                        category.deck_id = deck_id;
                        category.category = cat;
                        MainActivity.database.deckDao().insertCategory(category);
                    }
                    Log.d("Loading Categories", "Loaded:" + Arrays.toString(tokens));
                    continue;
                }
                if (deck_id == 0) {
                    Log.d("Loading Cards", "deck_id is null; can't load cards!!!");
                    continue;
                } else {
                    if (tokens.length != 4) {
                        Log.d("Loading Cards", "Data invalid format; card skipped:" + tokens[0] + ", " + tokens[1]);
                        continue;
                    }
                    Card card = new Card();
                    card.deck_id = deck_id;
                    card.front_text = tokens[0];
                    card.front_image = tokens[1];
                    card.back_text = tokens[2];
                    card.back_image = tokens[3];
                    Log.d("Loading Cards", "Loaded:" + Arrays.toString(tokens));
                }
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
    }
}
