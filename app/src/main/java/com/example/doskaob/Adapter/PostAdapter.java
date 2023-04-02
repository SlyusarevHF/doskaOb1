package com.example.doskaob.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doskaob.DbManager;
import com.example.doskaob.MainActivity;
import com.example.doskaob.NewPost;
import com.example.doskaob.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {
    private List<NewPost> arrayPost;
    private Context context;
    private OnItemClickCustom onItemClickCustom;
    private DbManager dbManager;

    public PostAdapter(List<NewPost> arrayPost, Context context, OnItemClickCustom onItemClickCustom) {
        this.arrayPost = arrayPost;
        this.context = context;
        this.onItemClickCustom = onItemClickCustom;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads, parent, false);
        return new ViewHolderData(view, onItemClickCustom);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.setData(arrayPost.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayPost.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView TvPriceTel, TvDesc, TvTit;
        private ImageView imAds;
        private LinearLayout edit_layout;
        private ImageButton del_button;
        private OnItemClickCustom onItemClickCustom;


        public ViewHolderData(@NonNull View itemView, OnItemClickCustom onItemClickCustom ) {
            super(itemView);
            TvTit = itemView.findViewById(R.id.tvTit);
            TvPriceTel = itemView.findViewById(R.id.tvPriceTel);
            TvDesc = itemView.findViewById(R.id.tvDesc);
            imAds = itemView.findViewById(R.id.imAds);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            del_button = itemView.findViewById(R.id.imDelAds);
            itemView.setOnClickListener(this);
            this.onItemClickCustom = onItemClickCustom;
        }
        public void setData(NewPost newPost)
        {
            if(newPost.getUid().equals(MainActivity.MAUTH))
            {
                edit_layout.setVisibility(View.VISIBLE);
            }
            else
            {
                edit_layout.setVisibility(View.GONE);
            }
            Picasso.get().load(newPost.getImageId()).into(imAds);
            TvTit.setText(newPost.getTitle());
            String price_tel = "Цена : " + newPost.getPrice() + " Тел : " + newPost.getTel();
            TvPriceTel.setText(price_tel);
            String textDesc = null;
            if(newPost.getDisc().length() > 50) textDesc = newPost.getDisc().substring(0,50) + "...";
            TvDesc.setText(textDesc);

            del_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbManager.deleteItem(newPost);
                    arrayPost.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            onItemClickCustom.onItemSelected(getAdapterPosition());
        }
    }
    public interface OnItemClickCustom
    {
        public void onItemSelected(int position);
    }

    public void updateAdapter(List<NewPost> listData)
    {
        arrayPost.clear();
        arrayPost.addAll(listData);
        notifyDataSetChanged();
    }
    public void setDbManager(DbManager dbManager)
    {
        this.dbManager = dbManager;
    }
}
