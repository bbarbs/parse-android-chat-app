package com.avocado.chatapp.ui.chat.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avocado.chatapp.R;
import com.avocado.chatapp.model.Chat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ChatAdapter.class.getSimpleName();

    private static final int TYPE_CURRENT_USER = 1;
    private static final int TYPE_OTHER_USER = 2;

    private List<Chat> chats;
    private String username;

    public ChatAdapter(String username, List<Chat> chats) {
        this.chats = new ArrayList<>(chats);
        this.username = username;
    }

    public void setChats(List<Chat> chats) {
        this.chats = new ArrayList<>(chats);
    }

    public void addNewChat(Chat chat) {
        this.chats.add(0, chat);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_CURRENT_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_current, parent, false);
            return new CurrentUserViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
            return new OtherUserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position) {
        Chat chat = chats.get(position);
        if (getItemViewType(position) == TYPE_CURRENT_USER) {
            ((CurrentUserViewHolder) viewHolder).setChat(chat);
        } else {
            ((OtherUserViewHolder) viewHolder).setChat(chat);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(username, chats.get(position).getUsername())) {
            return TYPE_CURRENT_USER;
        } else {
            return TYPE_OTHER_USER;
        }
    }

    class CurrentUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_item_username)
        TextView usernameItem;

        @BindView(R.id.textview_item_message)
        TextView messageItem;

        CurrentUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setChat(Chat chat) {
            usernameItem.setText(chat.getUsername());
            messageItem.setText(chat.getMessage());
        }
    }

    class OtherUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_item_username)
        TextView usernameItem;

        @BindView(R.id.textview_item_message)
        TextView messageItem;

        OtherUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setChat(Chat chat) {
            usernameItem.setText(chat.getUsername());
            messageItem.setText(chat.getMessage());
        }
    }
}
