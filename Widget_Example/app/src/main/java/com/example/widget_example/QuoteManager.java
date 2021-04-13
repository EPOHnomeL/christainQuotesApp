package com.example.widget_example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuoteManager {

    private static int NUMBER_OF_QUOTES = 0;
    private static final int QUOTES_INC = 10;

    // A quote will be current_quotes[quote_index]
    private List<Quote> allQuotes = new ArrayList<>();           // List of all quotes begins with 10 and increments with 10 each time
    private final List<Integer> currentQuotesIds = new ArrayList<>();
    private int quoteIndex = 0;        // 0 of prev_quotes is verse of the day, States the index of current quote from quotes var
    private final Quote quoteOfTheDay; // Equivalent to quotes[0]
    private final List<Integer> favourites = new ArrayList<>();            // Array of quoteIds which is the line number in text file


    public QuoteManager(Activity activity){

        // Get all the quotes from the json file
        allQuotes = new ArrayList<>(getQuotes(activity));
        Collections.sort(allQuotes);

        // Increase current_quotes by 10 quotes
        increaseCurrentQuotes();

        // Set quote of the day
        quoteOfTheDay = getQuoteFromIndex(0);
    }

    public Set<Quote> getQuotes(final Activity activity) {

        Set<Quote> quoteList = new HashSet<>();
        String json = readQuotesFile(activity);
        Type listType = new TypeToken<HashSet<Quote>>() {}.getType();
        // convert json into a list of Users
        try {
            quoteList = new Gson().fromJson(json, listType);
            NUMBER_OF_QUOTES = quoteList.size();
        }
        catch (Exception e) {
            // we never know :)
            Log.e("error parsing", e.toString());
        }
        return quoteList;
    }

    private  String readQuotesFile(final Activity act)
    {
        String text = "";
        try {
            InputStream is = act.getResources().openRawResource(R.raw.quotes);

            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public void increaseCurrentQuotes(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        Date now = new Date();
        int seed = Integer.parseInt(formatter.format(now));
        Random random = new Random(seed);
        for (int i=0; i<QUOTES_INC; i++){
            int random_int = random.nextInt(NUMBER_OF_QUOTES);
            currentQuotesIds.add(random_int);
        }
    }

    public Quote getCurrentQuote(){
        return allQuotes.get(currentQuotesIds.get(quoteIndex));
    }

    public Quote nextQuote(){
        quoteIndex++;
        if ((quoteIndex % 10) == 0){ increaseCurrentQuotes(); }
        return getCurrentQuote();
    }

    public Quote getPreviousQuote(){
        int index = quoteIndex - 1;
        if(index == -1) { index =  0; }
        return getQuoteFromIndex(index);
    }
    public Quote previousQuote(){
        quoteIndex--;
        if(quoteIndex == -1) { quoteIndex = 0; }
        return getCurrentQuote();
    }

    public Quote getQuoteFromIndex(int index){
        return allQuotes.get(currentQuotesIds.get(index));
    }

    public Quote getQuoteOfTheDay() {
        return quoteOfTheDay;
    }

    public boolean isFavourite(Quote quote){ return favourites.contains(quote.getId()); }
    public void setFavourite(Quote quote){ favourites.add(quote.getId()); }
    public void removeFavourite(Quote quote){ favourites.remove(quote.getId()); }
}
