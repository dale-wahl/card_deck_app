package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.dalewahl.carddecks.database.Card;

public class DeckActivity extends AppCompatActivity {
    private ImageView card_image;
    private long deck_id;
    private List<Card> current_deck = new ArrayList<>();
    private int deck_size = 0;
    private int current_card = 0;
    private String card_side;
    private Button button;
    private boolean back_images = true;
    private boolean images = true;
    private boolean back_text = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        card_image = findViewById(R.id.cardImage);
        button = findViewById(R.id.flip_card);

        deck_id = getIntent().getLongExtra("deck_id", 0);
        //Log.d("Load Deck", "Open Deck ID:" + deck_id);

        current_deck = MainActivity.database.deckDao().getDeck(deck_id);
        Log.d("Load Deck", "Deck size:" + current_deck.size());

        deck_size = current_deck.size();
        shuffle();
        // Explore our deck
        if (current_deck.get(current_card).back_image.equals("NULL")) {
            // checks back images
            back_images = false;
        }

        if (current_deck.get(current_card).front_image.equals("NULL")) {
            // checks to see if only text
            images = false;
        }
        if (current_deck.get(current_card).back_text.equals("NULL")){
            // checks to see if there is back text as well
            back_text = false;
        }
        if (back_images || back_text) {
            backCard();
        } else {
            frontCard();
            }
    }

    public void backCard() {
        if (back_images){
            card_image.setImageResource(getResources().getIdentifier(current_deck.get(current_card).back_image, "drawable", getPackageName()));
        } else {
            button.setText(current_deck.get(current_card).back_text);
        }
        card_side = "back";
    }

    public void frontCard() {
        if (images) {
            card_image.setImageResource(getResources().getIdentifier(current_deck.get(current_card).front_image, "drawable", getPackageName()));
        } else {
            button.setText(current_deck.get(current_card).front_text);
        }
        card_side = "front";
    }

    public void prevCard(View view) {
        // onClick from button
        applyPrevCard();
    }

    public void applyPrevCard(){
        if (current_card == 0) {
            // Do Nothing
        } else {
            current_card--;
            frontCard();
        }
    }

    public void nextCard(View view) {
        // onClick from button
        applyNextCard();
    }

    public void applyNextCard() {
        // Separated from onClick to use elsewhere
        if (current_card == deck_size - 1) {
            // End of deck reached
            // Currently just start over; TODO add notification
            shuffle();
            current_card = -1;
        }
        current_card++;
        if (back_images || back_text) {
            backCard();
        } else {
            frontCard();
        }
    }

    public void flipCard(View view) {
        if (back_images || back_text) {
            // Something on the back? Flip it!
            if (card_side.equals("back")) {
                frontCard();
            } else {
                backCard();
            }
        } else {
            // Otherwise just go to the next card
            applyNextCard();
        }
    }

    public void shuffle() {
        Collections.shuffle(current_deck);
    }
}
