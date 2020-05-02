package io.github.dalewahl.carddecks;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import io.github.dalewahl.carddecks.database.Card;
import io.github.dalewahl.carddecks.database.Category;
import io.github.dalewahl.carddecks.database.Deck;

public class DownloadDeck {
    long deck_id;
    String universal_id;
    Context context;
    String image_path;
    JSONObject responseJSON;

    public DownloadDeck(String url, Context current_context) {
        context = current_context;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Response:" + response);

                try {
                    responseJSON = new JSONObject(response);

                    if (MainActivity.database.deckDao().deckNames().contains(responseJSON.getString("universal_id"))) {
                        Log.d("DownloadDeck", "Deck already loaded:" + responseJSON.getString("universal_id"));
                    } else {
                        // Download the deck image
                        // TODO catch no image? All decks should have deck image...
                        String image_url = responseJSON.getString("deck_image");
                        new DownloadImage().execute(image_url);

                        // Image downloaded, now load rest of deck
                        loadDatabase(responseJSON);

                        //TODO Download all card images (gross)
                    }

                } catch (JSONException e) {
                    Log.e("JSON Request", "Deck json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON Request", "That didn't work!" + error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(request);
    }

    private void loadDatabase(JSONObject responseJSON) throws JSONException {
        // Load Deck
        Deck deck = new Deck();
        deck.universal_id = responseJSON.getString("universal_id");
        universal_id = deck.universal_id;
        deck.name = responseJSON.getString("name");
        deck.language = responseJSON.getString("languages").toLowerCase();
        // tells it that the image will be downloaded (if it exists)
        deck.resource_image = false;

        //deck.deck_url = responseJSON.getString("deck_url");

        // image_path here seems to be null, i.e. not updated yet
        deck.deck_image = image_path;
        Log.d("JSON Request", "URL saved to deck_image:" + image_path);
        // Remove prior lasts
        MainActivity.database.deckDao().removeLast();
        deck.last = true;
        deck_id = MainActivity.database.deckDao().insertDeck(deck);
        Log.d("JSON Request", "New Deck ID:" + deck_id);

        // Load Deck Categories
        JSONArray categories = responseJSON.getJSONArray("categories");
        for (int i = 0; i < categories.length(); i++) {
            Category category = new Category();
            category.deck_id = deck_id;
            category.category = categories.getString(i).toLowerCase();
            Log.d("JSON Request", "Category:" + category.category);
            MainActivity.database.deckDao().insertCategory(category);
        }
        Category category = new Category();
        category.deck_id = deck_id;
        category.category = "all categories";
        MainActivity.database.deckDao().insertCategory(category);

        // Load Cards
        JSONArray cards = responseJSON.getJSONArray("cards");
        for (int i = 0; i < cards.length(); i++) {
            JSONObject temp_card = cards.getJSONObject(i);
            Card card = new Card();
            card.deck_id = deck_id;
            card.front_text = temp_card.getString("front_text");
            card.front_image = temp_card.getString("front_image");
            card.back_text = temp_card.getString("back_text");
            card.back_image = temp_card.getString("back_image");
            MainActivity.database.deckDao().insertCard(card);
            Log.d("JSON Request", "Card:" + card.front_text);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            } catch (IOException e) {
                Log.e("JSON Request", "Download image error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // load the bitmap
            try {
                image_path = saveToInternalStorage(context, bitmap, universal_id + "_image.png", "imageDir");
                // NOW that we have the image_path, we can update the deck_image in database
                MainActivity.database.deckDao().updateDeckImage(image_path, deck_id);
                Log.d("Save Photo:", "filename:" + universal_id + "_image.png");
                Log.d("Save Photo:", "image_path:" + image_path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveToInternalStorage(Context context, Bitmap bitmapImage, String filename, String directory_name) throws IOException {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/directory_name
        File directory = cw.getDir(directory_name, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, filename);
        Log.d("Save Photo:", "saveToInternalStorage mypath:" + mypath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        Log.d("Save Photo:", "saveToInternalStorage directory.getAbsolutePath():" + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }
}