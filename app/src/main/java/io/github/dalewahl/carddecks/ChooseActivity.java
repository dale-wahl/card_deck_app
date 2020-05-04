package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.github.dalewahl.carddecks.database.Deck;

public class ChooseActivity extends AppCompatActivity implements ChooseAdapter.ItemClickListener, PopupMenu.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {

    ChooseAdapter adapter;
    public List<Deck> decks;
    public List<Deck> filtered = new ArrayList<>();

    private Spinner category_spinner;
    // Ideally this should pull in a a query
    private static final String[] categories = {"All Categories", "Conversation Starter", "Trivia", "Flashcards", "Custom"};

    public ChooseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // Occasionally when clicking too quickly in the download activity, this query fails
        // I do not understand why
        // But since I added this try catch, I have not been able to recreate the problem...
        try {
            loadDecks();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        int numberOfColumns = getResources().getInteger(R.integer.grid_columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ChooseAdapter(this, decks);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        category_spinner = findViewById(R.id.category_spinner);
        ArrayAdapter<String> cat_spinner_adapter = new ArrayAdapter<>(ChooseActivity.this,
                android.R.layout.simple_spinner_item,categories);
        cat_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(cat_spinner_adapter);
        category_spinner.setOnItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.add_deck_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    public void loadDecks() {
        decks = MainActivity.database.deckDao().allDecks();
        filtered.addAll(decks);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.deck_options_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download_deck:
                Intent intent1 = new Intent(getApplicationContext(), NewDeckActivity.class);
                startActivityForResult(intent1, 0);
                return true;
            case R.id.create_edit:
                Intent intent2 = new Intent(getApplicationContext(), CreateDeckActivity.class);
                startActivityForResult(intent2, 1);
                return true;
            default:
                return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra("deck_id", adapter.getItem(position).id);
        MainActivity.database.deckDao().removeLast();
        MainActivity.database.deckDao().setLast(adapter.getItem(position).id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        decks.clear();
        for (Deck deck : filtered) {
            // Category filter has to query database for deck categories each time to compare
            // with choice; could be more efficient method
            if (MainActivity.database.deckDao().deckCategories(deck.id)
                    .contains(category_spinner.getSelectedItem().toString().toLowerCase())){
                decks.add(deck);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
