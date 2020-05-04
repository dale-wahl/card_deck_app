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

    public csvBuildDeck(InputStream inputStream, boolean last_deck) {
        this.inputStream = inputStream;
        long deck_id = 0;

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(";");

                if (tokens[0].equals("Deck")) {
                    if (MainActivity.database.deckDao().deckNames().contains(tokens[1])) {
                        // This checks to see if the deck has already been loaded
                        break;
                    }
                    if (tokens.length != 5) {
                        // This checks format of csv
                        break;
                    }
                    Deck deck = new Deck();
                    deck.universal_id = tokens[1];
                    deck.name = tokens[2];
                    deck.language = tokens[3];
                    deck.deck_image = tokens[4];
                    deck.last = last_deck;
                    deck.resource_image = true;
                    deck_id = MainActivity.database.deckDao().insertDeck(deck);
                    continue;
                }
                if (tokens[0].equals("Categories")) {
                    if (deck_id == 0) {
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
                        category.category = cat.toLowerCase();
                        MainActivity.database.deckDao().insertCategory(category);
                    }
                    Category category = new Category();
                    category.deck_id = deck_id;
                    category.category = "all categories";
                    MainActivity.database.deckDao().insertCategory(category);
                    continue;
                }
                if (deck_id == 0) {
                    // checks that a deck_id was assigned
                    continue;
                } else {
                    if (tokens.length != 4) {
                        // checks that csv is formatted correctly
                        continue;
                    }
                    Card card = new Card();
                    card.deck_id = deck_id;
                    card.front_text = tokens[0];
                    card.front_image = tokens[1];
                    card.back_text = tokens[2];
                    card.back_image = tokens[3];
                    MainActivity.database.deckDao().insertCard(card);
                }
            }
            inputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
