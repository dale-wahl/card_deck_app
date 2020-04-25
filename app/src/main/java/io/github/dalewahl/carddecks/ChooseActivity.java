package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.github.dalewahl.carddecks.database.Deck;

public class ChooseActivity extends AppCompatActivity implements ChooseAdapter.ItemClickListener {

    ChooseAdapter adapter;
    private List<Deck> decks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        decks = MainActivity.database.deckDao().allDecks();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ChooseAdapter(this, decks);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.add_deck_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewDeckActivity.class);
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
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra("deck_id", adapter.getItem(position).id);
        Log.d("ChooseActivity", "Open Deck ID:" + adapter.getItem(position).id);
        MainActivity.database.deckDao().removeLast();
        MainActivity.database.deckDao().setLast(adapter.getItem(position).id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // May not be needed here...
        // Seems to make the screen flicker as it runs a query then updates
        //adapter.reload();
    }
}
