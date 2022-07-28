package com.example.mytherapist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytherapist.Model.ArticleRetrieved;
import com.example.mytherapist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private List<ArticleRetrieved> userList;

    public ArticleAdapter(Context context, List<ArticleRetrieved> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.articles_displayed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ArticleRetrieved user= userList.get(position);

        holder.article.setText(user.gettitle());
        holder.source.setText(user.getdate());

        Glide.with(context).load(user.getsrc()).into(holder.imageView);







    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView article, source;

        private ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            article= itemView.findViewById(R.id.headline);
            source= itemView.findViewById(R.id.source);
            imageView=itemView.findViewById(R.id.articlePicture);


        }
    }
}
