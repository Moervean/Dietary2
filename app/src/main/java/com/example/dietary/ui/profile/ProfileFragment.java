package com.example.dietary.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import Activites.CoachesList;
import Activites.AddDietActivity;
import Activites.ExerciseActivity;
import Activites.ProtegesList;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private ImageView navButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private AppBarConfiguration mAppBarConfiguration;
    private Button coaches;
    private Button proteges;
    private Button diet;
    private Button exercises;
    private DatabaseReference myRef;
    private final static int GALERY_CODE = 1;
    private Uri resultUri = null;
    private StorageTask uploadTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.activity_main_screen, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        proteges = (Button)root.findViewById(R.id.protegesButtonProfile);
        exercises = (Button)root.findViewById(R.id.exerciseButtonProfile);
        coaches = (Button)root.findViewById(R.id.coachButtonProfile);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePicutes").child(user.getUid());
        // navButton = (ImageView) findViewById(R.id.naviButton);


        mDatabase = FirebaseDatabase.getInstance();
        diet = (Button)root.findViewById(R.id.dietButtonProfile);
        mRef = mDatabase.getReference().child(mAuth.getCurrentUser().getUid());

        exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ExerciseActivity.class));
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddDietActivity.class));
            }
        });
        coaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), CoachesList.class));
            }
        });
        proteges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProtegesList.class));
            }
        });


        return root;
    }
}
