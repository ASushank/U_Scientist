package com.contrivancenexus.uscientist.ui.foryou;

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
import com.contrivancenexus.uscientist.databinding.FragmentForYouBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForYouFragment extends Fragment {
    private FragmentForYouBinding binding;
    DatabaseReference databaseReference;
    ProjectAdapter projectAdapter;
    ArrayList<Project> list;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForYouBinding.inflate(inflater, container, false);
        recyclerView = binding.vehicleRecyclerView;
        databaseReference = FirebaseDatabase.getInstance().getReference("Projects");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        projectAdapter = new ProjectAdapter(getContext(), list);
        recyclerView.setAdapter(projectAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Project project = dataSnapshot.getValue(Project.class);
                    list.add(project);
                }
                projectAdapter.notifyDataSetChanged();
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