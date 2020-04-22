package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra("deck_id", adapter.getItem(position).id);
        Log.d("ChooseActivity", "Open Deck ID:" + adapter.getItem(position).id);
        startActivity(intent);
    }
}
