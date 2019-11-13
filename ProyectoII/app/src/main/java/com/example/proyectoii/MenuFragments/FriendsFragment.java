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
import com.example.proyectoii.Utils.RecyclerViewFriendRequestAdapter;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,container,false);
        recyclerView = view.findViewById(R.id.recyclerFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add("jsfhbdshfhbhfbbfjkns");

        }

        RecyclerViewFriendRequestAdapter adapter = new RecyclerViewFriendRequestAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }
}
