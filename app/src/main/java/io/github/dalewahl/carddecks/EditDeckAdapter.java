package io.github.dalewahl.carddecks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.dalewahl.carddecks.database.Card;

public class EditDeckAdapter extends RecyclerView.Adapter<EditDeckAdapter.EditDeckViewHolder> {
    private long deck_id;

    EditDeckAdapter(long deck_id) {
        this.deck_id = deck_id;
    }

    public static class EditDeckViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView nameTextView;

        public EditDeckViewHolder(View view) {
            super(view);
            this.containerView = view.findViewById(R.id.card_row);
            this.nameTextView = view.findViewById(R.id.card_row_name);

            this.containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO go to card view
                    Context context = v.getContext();
                    Card card = (Card) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), EditCardActivity.class);
                    intent.putExtra("deck_id", card.deck_id);
                    intent.putExtra("id", card.id);
                    intent.putExtra("front_text", card.front_text);
                    intent.putExtra("back_text", card.back_text);

                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Card> cards = new ArrayList<>();

    @Override
    public EditDeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_deck_item, parent, false);

        return new EditDeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditDeckViewHolder holder, int position) {
        Card current = cards.get(position);
        holder.containerView.setTag(current);
        holder.nameTextView.setText(current.front_text);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void reload() {
        cards = MainActivity.database.deckDao().getDeck(deck_id);
        notifyDataSetChanged();
    }

}
