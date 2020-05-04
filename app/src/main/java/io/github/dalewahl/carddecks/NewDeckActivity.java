package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewDeckActivity extends AppCompatActivity implements NewDeckAdapter.ItemClickListener, AdapterView.OnItemSelectedListener{
    private NewDeckAdapter adapter;
    private List<Temp_Deck> decks = new ArrayList<>();
    private List<Temp_Deck> filtered = new ArrayList<>();
    private int image_count = 0;

    // Spinners for filtering
    private Spinner language_spinner;
    // Need to find way to pull in languages from JSON...
    private static final String[] languages = {"All Languages", "English", "French"};

    private Spinner category_spinner;
    // Need to find way to pull in languages from JSON...
    private static final String[] categories = {"All Categories", "Conversation Starter", "Trivia", "Flashcards"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deck);

        loadDecks();

        RecyclerView recyclerView = findViewById(R.id.new_deck_recycler_view);
        int numberOfColumns = getResources().getInteger(R.integer.grid_columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new NewDeckAdapter(this, filtered);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        language_spinner = findViewById(R.id.language_spinner);
        category_spinner = findViewById(R.id.category_spinner);
        ArrayAdapter<String> lang_spinner_adapter = new ArrayAdapter<>(NewDeckActivity.this,
                android.R.layout.simple_spinner_item,languages);
        ArrayAdapter<String> cat_spinner_adapter = new ArrayAdapter<>(NewDeckActivity.this,
                android.R.layout.simple_spinner_item,categories);

        lang_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_spinner.setAdapter(lang_spinner_adapter);
        language_spinner.setOnItemSelectedListener(this);

        cat_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(cat_spinner_adapter);
        category_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        new DownloadDeck(adapter.getItem(position).getDeck_url(), getApplicationContext());
        setResult(1);
        // Need a pause before finishing for the deck to load...
        mHandler.postDelayed(mRunnable, 1000);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filtered.clear();
        for (Temp_Deck deck : decks) {
            if (deck.getLanguages().contains(language_spinner.getSelectedItem().toString().toLowerCase()) && deck.getCategories().contains(category_spinner.getSelectedItem().toString().toLowerCase())) {
                filtered.add(deck);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // All this to do a 2 second delay...
    private static class MyHandler extends Handler {}
    private final MyHandler mHandler = new MyHandler();
    private MyRunnable mRunnable = new MyRunnable(this);

    public class MyRunnable implements Runnable {
        private final WeakReference<Activity> mActivity;

        private MyRunnable(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            Activity activity = mActivity.get();
            if (activity != null) {
                // This is where the finish() from onItemClick ended up!
                finish();
            }
        }
    }

    public void loadDecks() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://raw.githubusercontent.com/dale-wahl/additional_decks/master/deck_library.txt";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseJSON = new JSONArray(response);
                    for (int i = 0; i < responseJSON.length(); i++) {
                        JSONObject new_deck = responseJSON.getJSONObject(i);
                        String name = new_deck.getString("name");
                        String universal_id = new_deck.getString("universal_id");
                        String deck_url = new_deck.getString("deck_url");
                        String deck_image_url = new_deck.getString("deck_image_url");

                        // Get languages
                        List<String> languages = new ArrayList<>();
                        JSONArray language_array = new_deck.getJSONArray("languages");
                        for (int j = 0; j < language_array.length(); j++) {
                            String temp_language = language_array.getString(j).toLowerCase();
                            languages.add(temp_language);
                        }
                        languages.add("all languages");

                        // Get categories
                        List<String> categories = new ArrayList<>();
                        JSONArray category_array = new_deck.getJSONArray("categories");
                        for (int j = 0; j < category_array.length(); j++) {
                            String temp_category = category_array.getString(j).toLowerCase();
                            categories.add(temp_category);
                        }
                        categories.add("all categories");

                        // Go get the deck image
                        new retrieveDeckImage().execute(deck_image_url);

                        decks.add(new Temp_Deck(name, universal_id, deck_url, deck_image_url, languages, categories));
                    }
                    filtered.addAll(decks);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(request);
    }

    private class retrieveDeckImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            }
            catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // load the bitmap into the ImageView!
            decks.get(image_count).image = bitmap;
            image_count ++;
            adapter.notifyDataSetChanged();
        }
    }
}
