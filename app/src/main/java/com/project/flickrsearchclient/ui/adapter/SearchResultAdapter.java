package com.project.flickrsearchclient.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.flickrsearchclient.R;
import com.project.flickrsearchclient.model.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.PictureViewHolder> {

    private final List<Photo> mPhotos = new ArrayList<>();

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_item, viewGroup, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);

        String titleText = photo.getTitle();
        if (!TextUtils.isEmpty(titleText)) {
            holder.title.setText(titleText);
        }

        Glide.with(holder.picture.getContext()).load(photo.getUrl())
                .placeholder(R.drawable.ic_photo_white_48dp)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void addItems(List<Photo> photos) {
        int initialSize = mPhotos.size();
        mPhotos.addAll(photos);
        notifyItemRangeInserted(initialSize, mPhotos.size());
    }

    public void setItems(List<Photo> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        notifyItemRangeInserted(0, mPhotos.size());
    }

    public void clean() {
        int initialSize = mPhotos.size();
        mPhotos.clear();
        notifyItemRangeRemoved(0, initialSize);
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final ImageView picture;
        final CardView container;

        PictureViewHolder(View itemView) {
            super(itemView);
            title = ButterKnife.findById(itemView, R.id.title_textview);
            picture = ButterKnife.findById(itemView, R.id.picture_imageview);
            container = ButterKnife.findById(itemView, R.id.container_cardview);
        }
    }
}
