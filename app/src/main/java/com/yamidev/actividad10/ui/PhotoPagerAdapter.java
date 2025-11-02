package com.yamidev.actividad10.ui;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yamidev.actividad10.data.Photo;
import com.yamidev.actividad10.R;


import java.util.List;

public class PhotoPagerAdapter extends RecyclerView.Adapter<PhotoPagerAdapter.VH> {

    private final List<Photo> photos;

    public PhotoPagerAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_pager, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Photo p = photos.get(position);
        String demoUrl = "https://picsum.photos/600/900?random=" + position;
        Glide.with(h.img.getContext())
                .load(demoUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(h.img);

        h.title.setText(p.displayName != null ? p.displayName : "Foto");
        h.desc.setText(p.description != null ? p.description : "");
    }

    @Override public int getItemCount() { return photos.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView title; TextView desc;
        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFull);
            title = itemView.findViewById(R.id.txtTitle);
            desc = itemView.findViewById(R.id.txtDesc);
        }
    }
}