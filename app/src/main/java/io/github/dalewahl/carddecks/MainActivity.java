package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ImageButton;

import java.io.InputStream;
import java.util.List;

import io.github.dalewahl.carddecks.database.DecksDatabase;

public class MainActivity extends AppCompatActivity {
    public static DecksDatabase database;
    private ImageButton lastButtonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room
                .databaseBuilder(getApplicationContext(), DecksDatabase.class, "decks")
                .allowMainThreadQueries()
                .build();

        InputStream inputStream = getResources().openRawResource(R.raw.playing_cards);
        new csvBuildDeck(inputStream);


        lastButtonImage = findViewById(R.id.last_deck);
        lastButtonImage.setImageResource(R.drawable.card_back);
    }


}
