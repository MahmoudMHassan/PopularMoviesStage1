package com.example.android.popularmoviesstage1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.api_movies.Parse_APIs;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent mainActivityIntent = getIntent();
        MovieDataModel selectedMovie = mainActivityIntent.getParcelableExtra(MainActivity.SELECTED_MOVIE_KEY);

        TextView originalTitleTextView = findViewById(R.id.tv_original_title);
        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        TextView averageRateTextView = findViewById(R.id.tv_average_rate);
        TextView overviewTextView = findViewById(R.id.tv_overview);
        ImageView moviePosterImageView = findViewById(R.id.iv_movie_poster);

        overviewTextView.setText(selectedMovie.getOverview());
        originalTitleTextView.setText(selectedMovie.getOriginalTitle());
        releaseDateTextView.setText(selectedMovie.getReleaseDate());
        averageRateTextView.setText(String.format(Locale.US, "%2.1f/10", selectedMovie.getVoteAverage()));

        Picasso.get()
                .load(Parse_APIs.BASE_API_IMAGE_URL + selectedMovie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fit()
                .into(moviePosterImageView);



    }
}
