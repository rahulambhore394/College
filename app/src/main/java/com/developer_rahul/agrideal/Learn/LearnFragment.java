package com.developer_rahul.agrideal.Learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer_rahul.agrideal.R;

import java.util.ArrayList;
import java.util.List;

public class LearnFragment extends Fragment {

    private RecyclerView recyclerView;
    private LearnAdapter learnAdapter;
    private List<LearnItem> learnItemList;
//    private YouTubePlayerView youTubePlayerView;
//    private YouTubePlayer youTubePlayer;

    private Button btnOrganic, btnCropRotation, btnSchemes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
//        youTubePlayerView = view.findViewById(R.id.youtubePlayerView);
        btnOrganic = view.findViewById(R.id.btnOrganic);
        btnCropRotation = view.findViewById(R.id.btnCropRotation);
        btnSchemes = view.findViewById(R.id.btnSchemes);

//        getLifecycle().addObserver(youTubePlayerView);

        setupRecyclerView();
//        setupYouTubePlayer();

//        btnOrganic.setOnClickListener(v -> loadVideo("94oG36x0hTQ"));  // Example Organic Farming video ID
//        btnCropRotation.setOnClickListener(v -> loadVideo("xR7VozU5QXU")); // Example Crop Rotation video ID
//        btnSchemes.setOnClickListener(v -> loadVideo("L9gEc1fAXa8"));  // Example Government Schemes video ID

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        learnItemList = new ArrayList<>();

        learnItemList.add(new LearnItem("Organic Farming Benefits", "Organic farming improves soil health and reduces pollution."));
        learnItemList.add(new LearnItem("Crop Rotation", "Crop rotation enhances soil fertility and reduces pests."));
        learnItemList.add(new LearnItem("Government Schemes", "Check out PM-KISAN and other farmer support schemes."));

        learnAdapter = new LearnAdapter(learnItemList);
        recyclerView.setAdapter(learnAdapter);
    }

//    private void setupYouTubePlayer() {
//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer player) {
//                youTubePlayer = player;
//            }
//        });
//    }
//
//    private void loadVideo(String videoId) {
//        if (youTubePlayer != null) {
//            youTubePlayer.loadVideo(videoId, 0);
//        }
//    /}
}
