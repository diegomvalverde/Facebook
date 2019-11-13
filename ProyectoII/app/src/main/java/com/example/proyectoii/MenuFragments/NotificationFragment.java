package com.example.proyectoii.MenuFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.R;
import com.example.proyectoii.Utils.RecycleViewNotificationsAdapter;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
     RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);

        recyclerView = view.findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add("jsfhbdshfhbhfbbfjkns");

        }

        RecycleViewNotificationsAdapter adapter = new RecycleViewNotificationsAdapter(strings);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
