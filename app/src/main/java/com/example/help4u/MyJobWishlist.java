package com.example.help4u;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyJobWishlist extends RecyclerView.Adapter<MyJobWishlist.MyAllJobWishLists> {

    Context context;
    ArrayList<WishList> jobwishlist;
    ArrayList<String> keys;

    public MyJobWishlist(Context c, ArrayList<WishList> wl, ArrayList<String> k)
    {
        context = c;
        jobwishlist = wl;
        keys = k;
    }

    @NonNull
    @Override
    public MyAllJobWishLists onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAllJobWishLists(LayoutInflater.from(context).inflate(R.layout.jobwishlistcardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAllJobWishLists holder, int position) {
        holder.mgetjobtitle.setText(jobwishlist.get(position).getJobtitle());
        holder.mgetjobdescription.setText(jobwishlist.get(position).getJobdescription());
        holder.mgetposition.setText(jobwishlist.get(position).getPosition());
        Picasso.get().load(jobwishlist.get(position).getPhotourl()).into(holder.mgetphotourl);
    }

    @Override
    public int getItemCount() {
        return jobwishlist.size();
    }

    class MyAllJobWishLists extends RecyclerView.ViewHolder
    {
        // Declare Variables in job wish list
        ImageView mgetphotourl;
        TextView mgetjobtitle, mgetjobdescription, mgetposition;

        public MyAllJobWishLists(@NonNull View itemView) {
            super(itemView);
            mgetphotourl = itemView.findViewById(R.id.getphotourl);
            mgetjobtitle = itemView.findViewById(R.id.getjobtitle);
            mgetjobdescription = itemView.findViewById(R.id.getjobdescription);
            mgetposition = itemView.findViewById(R.id.getposition);
        }
    }
}
