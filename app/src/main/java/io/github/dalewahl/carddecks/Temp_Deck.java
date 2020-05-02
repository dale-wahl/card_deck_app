package io.github.dalewahl.carddecks;

import android.graphics.Bitmap;

import java.util.List;

public class Temp_Deck {
    private String name;
    private String universal_id;
    private String deck_url;
    private String deck_image_url;
    private List<String> languages;
    private List<String> categories;
    public Bitmap image;

    Temp_Deck(String name, String universal_id, String deck_url, String deck_image_url, List<String> languages, List<String> categories) {
        this.name = name;
        this.universal_id = universal_id;
        this.deck_url = deck_url;
        this.deck_image_url = deck_image_url;
        this.languages = languages;
        this.categories = categories;
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

    public List<String> getLanguages() {return languages;}

    public List<String> getCategories() {return categories;}
}
