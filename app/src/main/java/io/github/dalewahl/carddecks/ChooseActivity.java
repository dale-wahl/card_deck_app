package io.github.dalewahl.carddecks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseActivity extends AppCompatActivity {
    private ImageView choose_image1;
    private TextView choose_text1;
    private ImageView choose_image2;
    private TextView choose_text2;
    private ImageView choose_image3;
    private ImageView choose_image4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        //for

        choose_image1 = findViewById(R.id.choose_image1);
        choose_text1 = findViewById(R.id.choose_text1);
        choose_image2 = findViewById(R.id.choose_image2);
        choose_text2 = findViewById(R.id.choose_text2);
        choose_image3 = findViewById(R.id.choose_image3);
        choose_image4 = findViewById(R.id.choose_image4);

        choose_image1.setImageResource(R.drawable.card_as);
        choose_text1.setText("TESTING!");
        choose_image2.setImageResource(R.drawable.card_ac);
        choose_text2.setText("TESTING!");
        choose_image3.setImageResource(R.drawable.card_ad);
        choose_image4.setImageResource(R.drawable.card_ah);


    }

    public void loadChoices(String image, String text) {

    }

    public void chooseNewDeck(View view) {
        Intent intent = new Intent(this, ChooseActivity.class);
        Log.d("ChooseActivity", "Choosing Deck");
        startActivity(intent);
    }
}
