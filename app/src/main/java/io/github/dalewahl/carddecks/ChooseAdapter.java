package io.github.dalewahl.carddecks;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.dalewahl.carddecks.database.Deck;

//MyRecyclerViewAdapter
public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private List<Deck> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    ChooseAdapter(Context context, List<Deck> data) {
        this.context = context;
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.choose_item_text);
            imageView = itemView.findViewById(R.id.choose_item_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mClickListener != null) {
                showPopup(v);
                return true;
            } else return false;
        }

        public void showPopup(final View v) {
            final PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    long deck_id = mData.get(getAdapterPosition()).id;
                    switch (item.getItemId()) {
                        case R.id.edit_deck:
                            Intent intent = new Intent(v.getContext(), EditDeckActivity.class);
                            intent.putExtra("id", deck_id);
                            getActivity(v.getContext()).startActivityForResult(intent, 3);

                            return true;
                        case R.id.delete_deck:
                            // Pull last deck viewed cause that causes problems if deleted
                            long last_deck = MainActivity.database.deckDao().lastDeck().get(0).id;
                            MainActivity.database.deckDao().deleteDeck(deck_id);
                            MainActivity.database.deckDao().deleteDeckCards(deck_id);
                            MainActivity.database.deckDao().deleteDeckCategories(deck_id);
                            // Check last deck and then assign it somewhere if it was this deck
                            if (last_deck == deck_id) {
                                MainActivity.database.deckDao().setLast(MainActivity.database.deckDao().allDecks().get(0).id);
                            }
                            reload();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.deck_edit_delete, popup.getMenu());
            popup.show();
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
            if(!deck.deck_image.equals("NULL")){
                holder.imageView.setImageResource(context.getResources().getIdentifier(deck.deck_image, "drawable", context.getPackageName()));
            } else {
                holder.imageView.setImageResource(context.getResources().getIdentifier("blank_back", "drawable", context.getPackageName()));
            }
        } else {
            holder.imageView.setImageBitmap(MainActivity.loadImageFromStorage(deck.deck_image, deck.universal_id + "_image.png"));
        }
    }
    public void reload() {
        Intent intent = new Intent(context, ChooseActivity.class);
        getActivity(context).finish();
        context.startActivity(intent);
    }

    // This is totally stackoverflow. I REALLY need to learn more about context and activity and WTF
    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }
}