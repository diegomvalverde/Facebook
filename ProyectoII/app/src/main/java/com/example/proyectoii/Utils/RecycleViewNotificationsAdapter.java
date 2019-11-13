package com.example.proyectoii.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecycleViewNotificationsAdapter extends RecyclerView.Adapter<RecycleViewNotificationsAdapter.ViewHolderNotifications> {
    private ArrayList<String> notifications;

    public RecycleViewNotificationsAdapter(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public RecycleViewNotificationsAdapter.ViewHolderNotifications onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, null,  false);
        return new ViewHolderNotifications(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewNotificationsAdapter.ViewHolderNotifications holder, int position) {
        holder.asignarDatos(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolderNotifications extends RecyclerView.ViewHolder {
        TextView noti;
        public ViewHolderNotifications(@NonNull View itemView) {
            super(itemView);
            noti = itemView.findViewById(R.id.notiTxt);
        }

        public void asignarDatos(String s) {
            noti.setText(s);
        }
    }
}
