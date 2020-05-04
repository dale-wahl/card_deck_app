package io.github.dalewahl.carddecks;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class NewDeckAdapter extends RecyclerView.Adapter<NewDeckAdapter.ViewHolder> {

    private List<Temp_Deck> decks;

    private LayoutInflater inflater;
    private NewDeckAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    NewDeckAdapter(Context context, List<Temp_Deck> decks) {
        this.decks = decks;
        this.inflater = LayoutInflater.from(context);
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
        holder.textView.setText(decks.get(position).getName());
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