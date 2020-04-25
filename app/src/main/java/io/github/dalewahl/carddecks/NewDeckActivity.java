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

public class NewDeckActivity extends AppCompatActivity implements NewDeckAdapter.ItemClickListener {
    NewDeckAdapter adapter;
    public List<Temp_Deck> decks = new ArrayList<>();
    public int image_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deck);

        loadDecks();

        RecyclerView recyclerView = findViewById(R.id.new_deck_recycler_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new NewDeckAdapter(this, decks);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        new DownloadDeck(adapter.getItem(position).getDeck_url(), getApplicationContext());
        setResult(1);
        // Need a pause before finishing for the deck to load...
        mHandler.postDelayed(mRunnable, 1500);
    }

    // All this to do a 2 second delay...
    private static class MyHandler extends Handler {}
    private final MyHandler mHandler = new MyHandler();
    private MyRunnable mRunnable = new MyRunnable(this);

    public class MyRunnable implements Runnable {
        private final WeakReference<Activity> mActivity;

        public MyRunnable(Activity activity) {
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

                        // Go get the deck image
                        new retrieveDeckImage().execute(deck_image_url);

                        decks.add(new Temp_Deck(name, universal_id, deck_url, deck_image_url));
                        Log.d("NewDeckActivity", "Loaded deck:" + name);
                        Log.d("NewDeckActivity", "Loaded deck:" + deck_image_url);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NewDeckAdapter Request", "That didn't work!" + error);
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
                Log.d("NewDeckAdapter", "Downloaded Image");
                return BitmapFactory.decodeStream(url.openStream());
            }
            catch (IOException e) {
                Log.e("NewDeckAdapter", "Download deck image error", e);
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
