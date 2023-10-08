package com.contrivancenexus.uscientist.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contrivancenexus.uscientist.Project;
import com.contrivancenexus.uscientist.ProjectAdapter;
import com.contrivancenexus.uscientist.VideoAct;
import com.contrivancenexus.uscientist.VideoAdapter;
import com.contrivancenexus.uscientist.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    VideoAdapter videoAdapter;
    DatabaseReference databaseReference;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        List<VideoAct> videoActList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        videoAdapter = new VideoAdapter(getContext(), videoActList);
        binding.MainViewPager.setAdapter(videoAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    VideoAct videoAct = dataSnapshot.getValue(VideoAct.class);
                    videoActList.add(videoAct);
                }
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}