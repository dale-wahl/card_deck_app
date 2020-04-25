package io.github.dalewahl.carddecks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.dalewahl.carddecks.database.Deck;

//MyRecyclerViewAdapter
public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private List<Deck> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ChooseAdapter(Context context, List<Deck> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.imageView.getContext();
        holder.textView.setText(mData.get(position).name);
        loadDeckImageView(context, holder, mData.get(position));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.choose_item_text);
            imageView = itemView.findViewById(R.id.choose_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Deck getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public static void loadDeckImageView(Context context, ViewHolder holder, Deck deck) {
        if (deck.resource_image) {
            holder.imageView.setImageResource(context.getResources().getIdentifier(deck.deck_image, "drawable", context.getPackageName()));
        } else {
            holder.imageView.setImageBitmap(MainActivity.loadImageFromStorage(deck.deck_image, deck.universal_id + "_image.png"));
        }
    }
    public void reload() {
        // TODO test!!!
        mData = MainActivity.database.deckDao().allDecks();
        notifyDataSetChanged();
    }
}