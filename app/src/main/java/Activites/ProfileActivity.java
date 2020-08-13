package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dietary.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import Model.Uzytkownik;

public class ProfileActivity extends AppCompatActivity {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        proteges = (Button)findViewById(R.id.protegesButtonProfile);
        exercises = (Button)findViewById(R.id.exerciseButtonProfile);
        coaches = (Button)findViewById(R.id.coachButtonProfile);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePicutes").child(user.getUid());
       // navButton = (ImageView) findViewById(R.id.naviButton);


        mDatabase = FirebaseDatabase.getInstance();
        diet = (Button)findViewById(R.id.dietButtonProfile);
        mRef = mDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid());

        exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,ExerciseActivity.class));
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,DietActivity.class));
            }
        });
        coaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,CoachesList.class));
            }
        });
        proteges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,ProtegesList.class));
            }
        });




    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_signout:
//                mAuth.signOut();
//                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                finish();
//                break;
//            case R.id.search:
//                //TODO: Wyszukiwanie użytkownika
//                startActivity(new Intent(ProfileActivity.this,SearchActivity.class));
//                break;
//            case R.id.profileOptions:
//                startActivity(new Intent(ProfileActivity.this,addPersonActivity.class));
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();
//            CropImage.activity(mImageUri)
//                    .setAspectRatio(1,1)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this);
            resultUri = mImageUri;
            uploadImage();

        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                resultUri = result.getUri();
//
//
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child(user.getUid());
        myRef = mRef.child("image");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Uzytkownik uzytkownik = snapshot.getValue(Uzytkownik.class);
                if (uzytkownik.getImage() != "default") {


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
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
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });


        } else {
            Toast.makeText(ProfileActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}