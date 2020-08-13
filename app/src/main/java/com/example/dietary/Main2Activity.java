package com.example.dietary;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.rpc.context.AttributeContext;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.HashMap;


import Activites.SearchActivity;
import Activites.StartActivity;
import Model.Uzytkownik;

public class Main2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView navButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private Button coaches;
    private Button proteges;
    private ImageView profilePic;
    private Button diet;
    private Button exercises;
    private DatabaseReference myRef;
    private final static int GALERY_CODE = 1;
    private Uri resultUri = null;
    private StorageTask uploadTask;
    private TextView nickname;
    private TextView signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,R.id.nav_slideshow,R.id.nav_search)
                .setDrawerLayout(drawer)
                .build();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        proteges = (Button)findViewById(R.id.protegesButtonProfile);
        exercises = (Button)findViewById(R.id.exerciseButtonProfile);
        coaches = (Button)findViewById(R.id.coachButtonProfile);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePictures").child(user.getUid());
        mDatabase = FirebaseDatabase.getInstance();
        diet = (Button)findViewById(R.id.dietButtonProfile);
        signout = (TextView)findViewById(R.id.logoutNavTextView);
        mRef = mDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        profilePic = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profileNav);
        nickname = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navNick);

        mRef.child("nickname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nickname.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Main2Activity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALERY_CODE && resultCode == RESULT_OK) {
//            Uri mImageUri = data.getData();
////            CropImage.activity(mImageUri)
////                    .setAspectRatio(1,1)
////                    .setGuidelines(CropImageView.Guidelines.ON)
////                    .start(this);
//            resultUri = mImageUri;
//         uploadImage();
//
//        }
////        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////            if (resultCode == RESULT_OK) {
////                resultUri = result.getUri();
////
////
////
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                Exception error = result.getError();
////            }
////        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main2, menu);
//        menu.findItem(R.id.addUserTool).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                startActivity(new Intent(Main2Activity.this, SearchActivity.class));
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mRef.child("image");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("default")) {
                    Picasso.get().load(snapshot.getValue().toString()).into(profilePic);


                } else {
                    profilePic.setBackground(getDrawable(R.drawable.basic_photo));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("Uploading");
        pd.show();

        if (resultUri != null) {
            final StorageReference fileReference = mStorageRef;
            uploadTask = fileReference.putFile(resultUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mURL = downloadUri.toString();

                        mRef = FirebaseDatabase.getInstance().getReference().child(mAuth.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", mURL);
                        mRef.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}