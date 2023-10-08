package com.contrivancenexus.uscientist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.UUID;

public class AddVideoActivity extends AppCompatActivity {
    StorageReference storageReference;
    DatabaseReference databaseReference;
    LinearProgressIndicator progressIndicator;
    Uri video;
    private TextInputEditText editVideoName;
    private TextInputEditText editVideoDescription;
    Button btnSelectVideo;
    ImageView uploadVideo;
    Button btnSaveVideo;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    uploadVideo.setEnabled(true);
                    video = result.getData().getData();
                    Glide.with(AddVideoActivity.this).load(video).into(uploadVideo);
                }
            } else {
                Toast.makeText(AddVideoActivity.this, "Please select a video", Toast.LENGTH_SHORT).show();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_add_video);
        btnSelectVideo = findViewById(R.id.btnChooseVideo);
        btnSaveVideo = findViewById(R.id.btnSaveVideo);
        editVideoDescription = findViewById(R.id.editVideoDescription);
        editVideoName = findViewById(R.id.editVideoName);
        progressIndicator = findViewById(R.id.process);
        uploadVideo = findViewById(R.id.uploadVideoImageView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("UploadedVideos").push();
        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                activityResultLauncher.launch(intent);
            }
        });
        btnSaveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideo(video);
            }
        });
    }

    private void uploadVideo(Uri uri) {
        StorageReference reference = storageReference.child(UUID.randomUUID().toString());
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String videoTitle = editVideoName.getText().toString();
                        String videoDesc = editVideoDescription.getText().toString();
                        String videoURL = uri.toString();
                        VideoAct video = new VideoAct();
                        video.setVideoTitle(videoTitle);
                        video.setVideoDesc(videoDesc);
                        video.setVideoURL(videoURL);
                        databaseReference.setValue(video);
                    }
                });
                Toast.makeText(AddVideoActivity.this, "Video uploaded successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddVideoActivity.this, "Failed to upload video", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressIndicator.setVisibility(View.VISIBLE);
                progressIndicator.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
                progressIndicator.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
            }
        });
    }
}