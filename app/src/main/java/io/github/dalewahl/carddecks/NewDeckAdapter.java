package io.github.dalewahl.carddecks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.dalewahl.carddecks.database.Deck;

public class NewDeckAdapter extends RecyclerView.Adapter<NewDeckAdapter.ViewHolder> {

    private List<Temp_Deck> decks;

    private LayoutInflater inflater;
    private NewDeckAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    NewDeckAdapter(Context context, List<Temp_Deck> decks) {
        this.decks = decks;
        this.inflater = LayoutInflater.from(context);
        Log.d("NewDeckAdapter", "Testing, testing, 1, 2, 3.");
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.new_deck_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the Views in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Context context = holder.imageView.getContext();
        holder.textView.setText(decks.get(position).getName());
        Log.d("NewDeckAdapter", "Loaded deck:" + decks.get(position).getDeck_image_url());
        holder.imageView.setImageBitmap(decks.get(position).image);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return decks.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.new_item_text);
            imageView = itemView.findViewById(R.id.new_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Temp_Deck getItem(int id) {
        return decks.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}