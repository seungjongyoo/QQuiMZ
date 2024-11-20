package com.foo.realqquimz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final Context context;
    private List<User> friends;

    public FriendAdapter(List<User> friends, Context context) {
        this.friends = new ArrayList<>(friends); // 리스트 복사
        this.context = context;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User user = friends.get(position);
        holder.tokenTextView.setText(user.getToken());
        holder.pointTextView.setText("Points: " + user.getPoint());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    // 데이터 업데이트 메서드 추가
    public void setData(List<User> newFriends) {
        friends.clear();
        friends.addAll(newFriends);
        notifyDataSetChanged();
    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tokenTextView;
        TextView pointTextView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            tokenTextView = itemView.findViewById(R.id.friend_token);
            pointTextView = itemView.findViewById(R.id.friend_point);
        }
    }
}
