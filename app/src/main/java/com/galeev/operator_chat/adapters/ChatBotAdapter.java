package com.galeev.operator_chat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galeev.operator_chat.R;
import com.galeev.operator_chat.models.ChatMessage;

import java.util.List;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.ChatViewHolder> {
    private List<ChatMessage> messages;
    private static final int VIEW_TYPE_USER_MESSAGE = 1;
    private static final int VIEW_TYPE_BOT_MESSAGE = 2;
    private OnItemClickListener onItemClickListener;
    public ChatBotAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BOT_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_send_message, parent, false);
        }
        return new ChatViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);
        holder.bind(chatMessage.getMessageText());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }
    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = messages.get(position);
        if ("bot".equals(chatMessage.getMessageType())) {
            return VIEW_TYPE_BOT_MESSAGE;
        } else {
            return VIEW_TYPE_USER_MESSAGE;
        }
    }
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textMessage);
        }
        public void bind(String message) {
            messageText.setText(message);
        }
    }
}