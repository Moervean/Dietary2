package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.dietary.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    private EditText email;
    private StorageReference mStorage;
    private EditText pass;
    private ImageView image;
    private Button accCreate;
    private ProgressDialog mProgress;
    private int i = 0;
    public int secure = 1;
    private EditText nickname;
    private AdView mAdView;
    private final static int GALERY_CODE = 1;
    private Uri resultUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email = (EditText) findViewById(R.id.emailEt);
        pass = (EditText) findViewById(R.id.passEt2);
        image = (ImageView) findViewById(R.id.profilePicture);
        nickname = (EditText) findViewById(R.id.nicknameET);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("users");
        accCreate = (Button) findViewById(R.id.createButton);
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile_Pictures");
        mProgress = new ProgressDialog(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALERY_CODE);
            }
        });
        accCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //TODO: Sprawdzenie jak zapisują się zdjęcia do bazy
                mProgress.setMessage("Tworzę konto");
                mProgress.show();
                final String nicknameString = nickname.getText().toString().trim();

                if (!TextUtils.isEmpty(nicknameString)) {


                    Log.d("XDDDDDDD","NONO");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //TODO Zapisanie maila do bazy i sprawdzanie czy istnieje

                            List<String> nazwy = new LinkedList<String>();
                            Log.d("XDDDDDDD",String.valueOf(i));
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if(dataSnapshot.child("nickname").getValue()!=null)
                                nazwy.add(dataSnapshot.child("nickname").getValue().toString());

                                Log.d("XDDDDDDD",String.valueOf(i));

                            }

                                for (String nazwa : nazwy) {
                                    Log.d("XDDDDDDD",String.valueOf(i));
                                if (nazwa.equalsIgnoreCase(nicknameString)) {
                                    Toast.makeText(CreateAccountActivity.this, "Nazwa użytkownika jest już zajęta", Toast.LENGTH_SHORT).show();
                                    mProgress.dismiss();
                                    i = 0;
                                    Log.d("XDDDDDDD",String.valueOf(i));
                                    break;

                                } else {

                                    i++;
                                    Log.d("XDDDDDDDi",String.valueOf(i));
                                }}


                                if (i == nazwy.size()) {
                                    if (pass.getText().toString().trim().length() < 6) {
                                        mProgress.dismiss();
                                        Toast.makeText(CreateAccountActivity.this, R.string.passError, Toast.LENGTH_LONG).show();
                                        i = 0;

                                    } else if (!TextUtils.isEmpty(email.getText().toString().trim())
                                            && !TextUtils.isEmpty(pass.getText().toString().trim())) {

                                        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(final AuthResult authResult) {
                                                Log.d("XDDDDDDDnazwy","Działą");
                                                final FirebaseUser us = mAuth.getCurrentUser();

                                                us.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Map<String,String> uzytkownik = new HashMap<>();
                                                                    if (authResult != null) {




                                                                        String myString = getResources().getString(R.string.emailVerificationGood);
                                                                        Toast.makeText(CreateAccountActivity.this, myString + email.getText().toString().trim(), Toast.LENGTH_LONG).show();
                                                                        String userid = mAuth.getCurrentUser().getUid();
                                                                        final DatabaseReference currentUserDb = mRef.child(userid);
                                                                        if (resultUri != null) {
                                                                            Log.d("DZIALA","DZIALAM");
                                                                            final String[] myUri = new String[1];
                                                                            final StorageReference imagePath = mStorage.child(resultUri.getLastPathSegment());
                                                                            imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                    imagePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                myUri[0] = task.getResult().toString();
                                                                                                currentUserDb.child("nickname").setValue(nicknameString);
                                                                                                currentUserDb.child("image").setValue(myUri[0]);
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                }
                                                                            });


                                                                        } else {
                                                                            uzytkownik.put("nickname",nicknameString);
                                                                            uzytkownik.put("image","default");
                                                                            currentUserDb.setValue(uzytkownik);
                                                                        }


                                                                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                                        mProgress.dismiss();
                                                                        i = 0;
                                                                        startActivity(intent);
                                                                        finish();


                                                                    }
                                                                } else {
                                                                    mProgress.dismiss();
                                                                    i=0;
                                                                    String myString = getResources().getString(R.string.emailVerificationError);
                                                                    Toast.makeText(CreateAccountActivity.this, myString, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CreateAccountActivity.this, "Email jest już w bazie", Toast.LENGTH_LONG).show();
                                                mProgress.dismiss();
                                                i=0;
                                            }
                                        });


                                    } else {
                                        mProgress.dismiss();
                                        Toast.makeText(CreateAccountActivity.this, R.string.blankError, Toast.LENGTH_LONG).show();
                                        i = 0;
                                    }
                                }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Pole nazwa użytkownika nie może być puste", Toast.LENGTH_LONG).show();
                    i = 0;
                    mProgress.dismiss();
                }


            }

        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Uri mImageUri = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            CropImage.activity(mImageUri).setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1).setMaxCropResultSize(400, 400)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                image.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}