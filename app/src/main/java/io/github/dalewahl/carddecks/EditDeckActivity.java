package io.github.dalewahl.carddecks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.github.dalewahl.carddecks.database.Card;

public class EditDeckActivity extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditDeckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        textView = findViewById(R.id.deck_name);
        recyclerView = findViewById(R.id.edit_deck_recyclerview);
        layoutManager = new LinearLayoutManager(this);

        Intent intent = getIntent();
        final long deck_id = intent.getLongExtra("id", 0);
        textView.setText(MainActivity.database.deckDao().deckName(deck_id));
        adapter = new EditDeckAdapter(deck_id);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.add_card_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card = new Card();
                card.deck_id = deck_id;
                card.front_text = "New card!";
                card.front_image = "NULL";
                card.back_text = "NULL";
                card.back_image = "NULL";
                long id = MainActivity.database.deckDao().insertCard(card);

                Intent intent = new Intent(getApplicationContext(), EditCardActivity.class);
                intent.putExtra("deck_id", card.deck_id);
                intent.putExtra("id", id);
                intent.putExtra("front_text", card.front_text);
                intent.putExtra("back_text", card.back_text);
                startActivityForResult(intent, 0);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            adapter.reload();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.reload();
    }

    // Attempting to add a swipe to delete thing

}
