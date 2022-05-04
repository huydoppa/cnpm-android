package com.example.myapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.HomeActivity;
import com.example.myapp.LoginActivity;
import com.example.myapp.Model.Comment;
import com.example.myapp.Model.User;
import com.example.myapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private List<Comment> list;
    private Context context;

    public void setData(List<Comment> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = list.get(position);
        User user = getUser(comment.getIdUser());
        holder.tvComment.setText(comment.getContent());
        holder.tvUserName.setText(user.getName());
    }

    private User getUser(String idUser) {
        for(User user: HomeActivity.userList){
            if(user.getIdUser().equals(idUser)){
                return user;
            }
        }
        return new User();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvUserName, tvComment;
            ImageView imgUser;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvCommentUser);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }
}
