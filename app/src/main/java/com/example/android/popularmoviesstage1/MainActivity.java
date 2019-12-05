package com.example.android.popularmoviesstage1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.api_movies.NetworkUtils;
import com.example.android.popularmoviesstage1.api_movies.Parse_APIs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.MovieItemClickListener {
    private RecyclerView rv;
    private ProgressBar loadingData;

    private final String TOP_RATED_LIST_KEY = "TOP_RATED_LIST_KEY";
    private final String MOST_POPULAR_LIST_KEY = "MOST_POPULAR_LIST_KEY";
    public static final String SELECTED_MOVIE_KEY = "SELECTED_MOVIE_KEY";

    private ArrayList<MovieDataModel> topRatedMoviesList = new ArrayList<>();
    private ArrayList<MovieDataModel> mostPopularMoviesList = new ArrayList<>();

    private MyRecyclerViewAdapter movieListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv = findViewById(R.id.rv_movies_list);
        loadingData = findViewById(R.id.pb_loading_data);



        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        movieListAdapter = new MyRecyclerViewAdapter(mostPopularMoviesList, MainActivity.this);
        rv.setLayoutManager(sglm);
        rv.setAdapter(movieListAdapter);

        if(savedInstanceState != null) {
            topRatedMoviesList = savedInstanceState.getParcelableArrayList(TOP_RATED_LIST_KEY);
            mostPopularMoviesList = savedInstanceState.getParcelableArrayList(MOST_POPULAR_LIST_KEY);
            showData();
        } else {
            loadData();
        }
        //rv.setHasFixedSize(true);

//        List<String> imageList = new ArrayList<>();
//        imageList.add("https://farm5.staticflickr.com/4403/36538794702_83fd8b63b7_c.jpg");
//        imageList.add("https://farm5.staticflickr.com/4354/35684440714_434610d1d6_c.jpg");
//        imageList.add("https://farm5.staticflickr.com/4301/35690634410_f5d0e312cb_c.jpg");
//        imageList.add("https://farm4.staticflickr.com/3854/32890526884_7dc068fedd_c.jpg");
//        imageList.add("https://farm8.staticflickr.com/7787/18143831616_a239c78056_c.jpg");
//        imageList.add("https://farm9.staticflickr.com/8745/16657401480_57653ac8b0_c.jpg");
//        imageList.add("https://farm3.staticflickr.com/2917/14144166232_44613c53c7_c.jpg");
//        imageList.add("https://farm8.staticflickr.com/7453/13960410788_3dd02b7a02_c.jpg");
//        imageList.add("https://farm1.staticflickr.com/920/29297133218_de38a7e4c8_c.jpg");
//        imageList.add("https://farm2.staticflickr.com/1788/42989123072_6720c9608d_c.jpg");
//        imageList.add("https://farm1.staticflickr.com/888/29062858008_89851766c9_c.jpg");
//        imageList.add("https://farm2.staticflickr.com/1731/27940806257_8067196b41_c.jpg");
//        imageList.add("https://farm1.staticflickr.com/884/42745897912_ff65398e38_c.jpg");
//        imageList.add("https://farm2.staticflickr.com/1829/27971893037_1858467f9a_c.jpg");
//        imageList.add("https://farm2.staticflickr.com/1822/41996470025_414452d7a0_c.jpg");
//        imageList.add("https://farm2.staticflickr.com/1793/42937679651_3094ebb2b9_c.jpg");
//        imageList.add("https://farm1.staticflickr.com/892/42078661914_b940d96992_c.jpg");
//        MyRecyclerViewAdapter iga = new MyRecyclerViewAdapter(MainActivity.this, imageList);
//        rv.setAdapter(iga);
    }

    private void showData(){
        loadingData.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.VISIBLE);
        movieListAdapter.setMoviesList(mostPopularMoviesList);
    }

    private void loadData() {
        new ConnectionChecker().execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu_items,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.sort_by) {
            if(item.getTitle().toString().equals(getString(R.string.most_popular))) {
                item.setTitle(R.string.top_rated);
                movieListAdapter.setMoviesList(mostPopularMoviesList);
                return true;
            } else {
                item.setTitle(R.string.most_popular);
                movieListAdapter.setMoviesList(topRatedMoviesList);
                return true;
            }

        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClickListener(MovieDataModel movie) {
        Intent movieDetailsIntent = new Intent(MainActivity.this,DetailActivity.class);
        movieDetailsIntent.putExtra(SELECTED_MOVIE_KEY, movie);
        startActivity(movieDetailsIntent);

    }


    class ConnectionChecker extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            return NetworkUtils.isOnline();
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            super.onPostExecute(isConnected);
            if(!isConnected){
                showErrorLoadingDataDialog();
            } else {
                new DataLoaderFromAPI().execute();
            }
        }
    }

    class DataLoaderFromAPI extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            topRatedMoviesList = Parse_APIs.getTopRatedMoviesList();
            mostPopularMoviesList = Parse_APIs.getMostPopularMoviesList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if((topRatedMoviesList != null && !topRatedMoviesList.isEmpty()) &&
                    (mostPopularMoviesList != null && !mostPopularMoviesList.isEmpty())){
                showData();
            } else {
                showErrorLoadingDataDialog();
            }
        }
    }

    public void showErrorLoadingDataDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setMessage(R.string.connection_error_message);
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
            }
        });
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadData();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

}
