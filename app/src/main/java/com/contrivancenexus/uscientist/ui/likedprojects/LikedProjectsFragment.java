package com.contrivancenexus.uscientist.ui.likedprojects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contrivancenexus.uscientist.LikedProject;
import com.contrivancenexus.uscientist.LikedProjectAdapter;
import com.contrivancenexus.uscientist.Project;
import com.contrivancenexus.uscientist.ProjectAdapter;
import com.contrivancenexus.uscientist.databinding.FragmentHomeBinding;
import com.contrivancenexus.uscientist.databinding.FragmentLikedProjectsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikedProjectsFragment extends Fragment {
    private FragmentLikedProjectsBinding binding;
    DatabaseReference databaseReference;
    LikedProjectAdapter likedProjectAdapter;
    ArrayList<LikedProject> list;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLikedProjectsBinding.inflate(inflater, container, false);
        recyclerView = binding.likedRecyclerView;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("LikedProjects");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        likedProjectAdapter = new LikedProjectAdapter(getContext(), list);
        recyclerView.setAdapter(likedProjectAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    LikedProject project = dataSnapshot.getValue(LikedProject.class);
                    list.add(project);
                }
                likedProjectAdapter.notifyDataSetChanged();
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