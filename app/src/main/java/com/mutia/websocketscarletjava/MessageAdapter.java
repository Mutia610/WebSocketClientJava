package com.mutia.websocketscarletjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private List<MessegeResponse> items;

    public MessageAdapter(){
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ComponentItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessegeResponse data = items.get(position);

        ((ComponentItem) holder).tvMessage.setText(data.getPrice() +" - "+ data.getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MessegeResponse item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void setData(List<MessegeResponse> newData) {
        if (items != null) {
            items.addAll(newData);

            notifyDataSetChanged();
            // notifyItemInserted( getData().size() -1 );
        } else {
            items = newData;
        }
    }

    private static class ComponentItem extends RecyclerView.ViewHolder {
        TextView tvMessage;

        ComponentItem(View view) {
            super(view);
            this.tvMessage = itemView.findViewById(R.id.tv_message_receiver);
        }
    }
}
