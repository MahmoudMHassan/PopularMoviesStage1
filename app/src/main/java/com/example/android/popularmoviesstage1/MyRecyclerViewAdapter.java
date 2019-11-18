package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = MyRecyclerViewAdapter.class.getSimpleName();
    private List<String> imageList;
    private Context c;

    public MyRecyclerViewAdapter(Context c, List imageList){
        this.c = c;
        this.imageList= imageList;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        final String path = imageList.get(position);

        Picasso.get()
                .load(path)
                .resize(250,250)
                .centerCrop()
                .into(holder.movieImage);
        holder.movieImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                //handle click event on image
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.movie_image);
        }

        //        void bind(int listIndex) {
//            movieImage.set
//        }

    }
}
