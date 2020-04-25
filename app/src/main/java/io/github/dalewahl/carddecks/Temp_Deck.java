package io.github.dalewahl.carddecks;

import android.graphics.Bitmap;

public class Temp_Deck {
    private String name;
    private String universal_id;
    private String deck_url;
    private String deck_image_url;
    public Bitmap image;

    Temp_Deck(String name, String universal_id, String deck_url, String deck_image_url) {
        this.name = name;
        this.universal_id = universal_id;
        this.deck_url = deck_url;
        this.deck_image_url = deck_image_url;
    }

    public String getName() {
        return name;
    }

    public String getUniversal_id() {
        return universal_id;
    }

    public String getDeck_url() {
        return deck_url;
    }

    public String getDeck_image_url() {
        return deck_image_url;
    }
}
