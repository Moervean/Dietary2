package com.example.dietary.ui.setProfile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dietary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.inject.Inject;

import Activites.PassChangeActivity;
import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;

import static android.app.Activity.RESULT_OK;

public class SetProfileFragment extends Fragment {
    private TextView nickname;
    private TextView email;
    private ImageView profilepicture;
    private TextView passReset;
    private FirebaseDatabase mDat;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public setProfileViewModel setprofileViewModel;
    private final static int GALERY_CODE = 1;
    private StorageReference mStorage;
    private Uri resultUri = null;
    private Button all;
    private Button friends;
    private Button noone;
    @Inject
    Consts consts;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        setprofileViewModel = ViewModelProviders.of(this).get(setProfileViewModel.class);
        View root = inflater.inflate(R.layout.set_profile, container, false);
        all = (Button)root.findViewById(R.id.All);
        friends = (Button)root.findViewById(R.id.friends);
        noone = (Button)root.findViewById(R.id.noone);
        nickname = (TextView) root.findViewById(R.id.nicknameProfileTV);
        email = (TextView) root.findViewById(R.id.emailProfileTV);
        passReset = (TextView) root.findViewById(R.id.passRes);
        profilepicture = (ImageView) root.findViewById(R.id.setProfilePicture);
        mDat = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = mDat.getReference().child(consts.users).child(mUser.getUid());
        mStorage = FirebaseStorage.getInstance().getReference().child(consts.profile_pictures);
        email.setText(mUser.getEmail());
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                friends.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                noone.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                mRef.child(consts.priv).setValue(consts.all);
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                friends.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                noone.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                mRef.child(consts.priv).setValue(consts.friends);
            }
        });
        noone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                friends.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                noone.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                mRef.child(consts.priv).setValue(consts.noOne);
            }
        });
        mRef.child(consts.priv).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals(consts.all)){
                    all.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friends.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                    noone.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                }else if(snapshot.getValue().toString().equals(consts.friends)){
                    all.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                    friends.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    noone.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                }
                else{
                    all.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                    friends.setBackground(getResources().getDrawable(R.drawable.buttonshapewhite));
                    noone.setBackground(getResources().getDrawable(R.drawable.buttonshape));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PassChangeActivity.class));
            }
        });
        mRef.child(consts.nickname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nickname.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child(consts.image).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.getValue().toString()).into(profilepicture);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALERY_CODE);
            }
        });


        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();
            CropImage.activity(mImageUri).setCropShape(CropImageView.CropShape.OVAL)
                    .setGuidelines(CropImageView.Guidelines.ON).start( getContext(), this);



        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                if(resultUri!=null) {
                    final StorageReference imagePath = mStorage.child(resultUri.getLastPathSegment());
                    final DatabaseReference currentUserDb = mRef;
                    final String myUri[] = new String[1];
                    imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        myUri[0] = task.getResult().toString();
                                        Picasso.get().load(myUri[0]).into(profilepicture);
                                        currentUserDb.child(consts.image).setValue(myUri[0]);
                                    }
                                }
                            });

                        }
                    });
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
