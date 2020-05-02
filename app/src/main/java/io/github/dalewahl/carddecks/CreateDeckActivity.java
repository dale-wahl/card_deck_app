package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;

import java.util.List;

import io.github.dalewahl.carddecks.database.Category;
import io.github.dalewahl.carddecks.database.Deck;

public class CreateDeckActivity extends AppCompatActivity {
    EditText deck_name;
    EditText deck_language;
    EditText deck_categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);

        deck_name = findViewById(R.id.deck_name);
        deck_language = findViewById(R.id.deck_language);
        deck_categories = findViewById(R.id.deck_categories);

        deck_name.setText("Deck Name!");
        deck_language.setText("English");
        deck_categories.setText("Conversation Starter, Flashcards, Trivia");
    }

    public void saveNewDeck(View view) {
        // Load Deck
        Deck deck = new Deck();
        deck.universal_id = deck_name.getText().toString().replace(" ", "_")
                .toLowerCase() + "_" + getRandomIntegerBetweenRange(1000,9999);
        String universal_id = deck.universal_id;
        Log.d("CreateDeck", "Universal ID:" + universal_id);

        deck.name = deck_name.getText().toString();
        deck.language = deck_language.getText().toString().trim().toLowerCase();

        // TODO create way to import image
        // Right now just set to resource
        deck.resource_image = true;
        deck.deck_image = "NULL";


        deck.last = false;
        long deck_id = MainActivity.database.deckDao().insertDeck(deck);
        Log.d("CreateDeck", "New Deck ID:" + deck_id);

        // Load Deck Categories
        String[] categories = deck_categories.getText().toString().split(",");
        for (int i = 0; i < categories.length; i++) {
            Category category = new Category();
            category.deck_id = deck_id;
            category.category = categories[i].trim().toLowerCase();
            Log.d("CreateDeck", "Category:" + category.category);
            MainActivity.database.deckDao().insertCategory(category);
        }
        // Add all categories
        Category category = new Category();
        category.deck_id = deck_id;
        category.category = "all categories";
        MainActivity.database.deckDao().insertCategory(category);

        // Add custom category
        category = new Category();
        category.deck_id = deck_id;
        category.category = "custom";
        MainActivity.database.deckDao().insertCategory(category);

        setResult(1);
        finish();
        Intent intent = new Intent(getApplicationContext(), EditDeckActivity.class);
        intent.putExtra("id", deck_id);
        startActivityForResult(intent, 3);

    }

    public static long getRandomIntegerBetweenRange(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return Math.round(x);
    }

}
