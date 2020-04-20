package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageButton lastButtonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastButtonImage = findViewById(R.id.last_deck);
        lastButtonImage.setImageResource(R.drawable.card_as);
    }
}
