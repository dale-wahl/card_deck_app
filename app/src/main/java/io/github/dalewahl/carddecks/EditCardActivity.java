package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.github.dalewahl.carddecks.database.Card;

public class EditCardActivity extends AppCompatActivity {
    private TextView deckName;
    private EditText frontText;
    private EditText backText;
    long deck_id;
    String front_text;
    String back_text;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        deckName = findViewById(R.id.deck_name);
        frontText = findViewById(R.id.front_text);
        backText = findViewById(R.id.back_text);

        Intent intent = getIntent();
        deck_id = intent.getLongExtra("deck_id", 0);
        id = intent.getLongExtra("id", 0);
        front_text = intent.getStringExtra("front_text");
        back_text = intent.getStringExtra("back_text");

        deckName.setText(MainActivity.database.deckDao().deckName(deck_id));
        frontText.setText(front_text);
        backText.setText(back_text);
    }
    public void deleteCard(View view) {
        MainActivity.database.deckDao().deleteCard(id);
        finish();
    }
    public void saveCard(View view) {
        if (id != 0){
            MainActivity.database.deckDao().saveCard(id, frontText.getText().toString(), backText.getText().toString());
            setResult(1);
            finish();
        } else {
            finish();
        }
    }

}
