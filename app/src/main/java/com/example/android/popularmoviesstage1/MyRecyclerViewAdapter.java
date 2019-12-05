package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.api_movies.Parse_APIs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = MyRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<MovieDataModel> moviesList;
    private MovieItemClickListener movieItemClickListener;
//    private List<String> imageList;
//    private Context c;

    public MyRecyclerViewAdapter(ArrayList<MovieDataModel> moviesList, MovieItemClickListener movieItemClickListener){
        this.moviesList = moviesList;
        this.movieItemClickListener = movieItemClickListener;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View movieItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent,false);
        return new ViewHolder(movieItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieDataModel path = moviesList.get(position);
        String TAG = "Movie Index and Title";
        Log.d(TAG, position + " " + path.getOriginalTitle());
        Picasso.get()
                .load(Parse_APIs.BASE_API_IMAGE_URL + path.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fit()
                .into(holder.movieImage);
        holder.bind(path);
//        holder.movieImage.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v){
//                //handle click event on image
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null == moviesList)? 0 : moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieImage;
        MovieDataModel movie;

        public ViewHolder(View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.movie_image);
            movieImage.setOnClickListener(this);
        }

        void bind(MovieDataModel movie) {
            this.movie= movie;
        }

        @Override
        public void onClick(View view) {
            movieItemClickListener.onMovieClickListener(movie);
        }
    }

    public interface MovieItemClickListener{
        void onMovieClickListener(MovieDataModel movie);
    }

    void setMoviesList(ArrayList<MovieDataModel> moviesList){
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }
}
