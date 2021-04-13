package com.example.widget_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public QuoteManager quoteManager;

    private TextView twQuote = null;
    private CheckBox chbFavourite = null;
    Button btnPreviousQuote = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inheritance...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get widgets by id
        Button btnNextQuote = findViewById(R.id.btnNextQuote);
        btnPreviousQuote =  findViewById(R.id.btnPreviousQuote);
        FloatingActionButton btnCopy = findViewById(R.id.btnCopy);
        FloatingActionButton btnSearch = findViewById(R.id.btnSearch);
        chbFavourite = findViewById(R.id.chbFavourite);
        twQuote = findViewById(R.id.twQuote);
        twQuote.setMovementMethod(new ScrollingMovementMethod());

        // OnClicks
        btnNextQuote.setOnClickListener(btnNextQuoteOnClick);
        btnPreviousQuote.setOnClickListener(btnPrevQuoteOnClick);
        btnCopy.setOnClickListener(btnCopyOnClick);
        chbFavourite.setOnClickListener(chbFavouriteChecked);

        quoteManager = new QuoteManager(this);

        // Set Quote of the day
        SetQuoteToScreen(quoteManager.getCurrentQuote());
    }

    // Sets quote to screen
    private void SetQuoteToScreen(Quote quote) {
        if(quoteManager.getPreviousQuote() == quoteManager.getCurrentQuote()) {
            btnPreviousQuote.setEnabled(false);
        } else if(!btnPreviousQuote.isEnabled()){
            btnPreviousQuote.setEnabled(true);
        }
        chbFavourite.setChecked(quoteManager.isFavourite(quote));
        twQuote.setText(quote.toString());
        twQuote.refreshDrawableState();
    }

    // btnPrevQuote OnClick
    public View.OnClickListener btnPrevQuoteOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            SetQuoteToScreen(quoteManager.previousQuote());
        }
    };

    // btnNextQuote OnClick
    public View.OnClickListener btnNextQuoteOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            SetQuoteToScreen(quoteManager.nextQuote());
        }
    };

    // btnCopy OnClick
    public View.OnClickListener btnCopyOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            // Get quote as text
             String quote_text = quoteManager.getCurrentQuote().toString();

            // Set quote as clipboard
            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            ClipData myClip = ClipData.newPlainText("quote", quote_text);
            clipboardManager.setPrimaryClip(myClip);

            // Show copies to clipboard
            String COPY_QUOTE_STR = "Copied to Clipboard...";
            Toast toast = Toast.makeText(getApplicationContext(), COPY_QUOTE_STR, Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    public  View.OnClickListener chbFavouriteChecked = new View.OnClickListener() {
        public void onClick(View v) {
            if (chbFavourite.isChecked()){
                quoteManager.setFavourite(quoteManager.getCurrentQuote());
                // Show copies to clipboard
                String str = "Added to favourites";
                Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                quoteManager.removeFavourite(quoteManager.getCurrentQuote());
                String str = "Removed to favourites";
                Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };

}