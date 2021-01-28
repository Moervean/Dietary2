package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.dietary.R;
import com.google.android.gms.ads.AdRequest;
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

import javax.inject.Inject;

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;

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
    private EditText passConfirm;
    private int i = 0;
    private EditText nickname;
    private final static int GALERY_CODE = 1;
    private Uri resultUri = null;
    @Inject
    Consts consts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        email = (EditText) findViewById(R.id.emailEt);
        pass = (EditText) findViewById(R.id.passEt2);
        image = (ImageView) findViewById(R.id.profilePicture);
        nickname = (EditText) findViewById(R.id.nicknameET);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child(consts.users);
        accCreate = (Button) findViewById(R.id.createButton);
        mStorage = FirebaseStorage.getInstance().getReference().child(consts.profile_pictures);
        mProgress = new ProgressDialog(this);
        passConfirm = (EditText)findViewById(R.id.passConfirmET);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

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


                mProgress.setMessage(getResources().getString(R.string.creatingAccount));
                mProgress.show();
                final String nicknameString = nickname.getText().toString().trim();

                final String pas = pass.getText().toString();
                final String passConf = passConfirm.getText().toString();

                if (!TextUtils.isEmpty(nicknameString) && !TextUtils.isEmpty(pas)) {
                    if(pas.equals(passConf)) {


                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //TODO Zapisanie maila do bazy i sprawdzanie czy istnieje

                                List<String> nazwy = new LinkedList<String>();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child(consts.nickname).getValue() != null)
                                        nazwy.add(dataSnapshot.child(consts.nickname).getValue().toString());


                                }

                                for (String nazwa : nazwy) {
                                    if (nazwa.equalsIgnoreCase(nicknameString)) {
                                        Toast.makeText(CreateAccountActivity.this, getString(R.string.usernameReserved), Toast.LENGTH_SHORT).show();
                                        mProgress.dismiss();
                                        i = 0;
                                        break;

                                    } else {

                                        i++;
                                    }
                                }


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

                                                final FirebaseUser us = mAuth.getCurrentUser();

                                                us.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Map<String, String> user = new HashMap<>();
                                                                    if (authResult != null) {



                                                                        Toast.makeText(CreateAccountActivity.this, getResources().getString(R.string.emailVerificationGood) + email.getText().toString().trim(), Toast.LENGTH_LONG).show();
                                                                        final DatabaseReference currentUserDb = mRef.child(mAuth.getCurrentUser().getUid());
                                                                        if (resultUri != null) {

                                                                            final String[] myUri = new String[1];
                                                                            final StorageReference imagePath = mStorage.child(resultUri.getLastPathSegment());
                                                                            imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                    imagePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                myUri[0] = task.getResult().toString();
                                                                                                String nick = nicknameString.substring(0,1).toUpperCase() + nicknameString.substring(1).toLowerCase();
                                                                                                currentUserDb.child(consts.nickname).setValue(nick);
                                                                                                currentUserDb.child(consts.image).setValue(myUri[0]);
                                                                                                currentUserDb.child(consts.priv).setValue(consts.all);
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                }
                                                                            });


                                                                        } else {
                                                                            String nick = nicknameString.substring(0,1).toUpperCase() + nicknameString.substring(1).toLowerCase();
                                                                            user.put(consts.nickname, nick);
                                                                            user.put(consts.image, consts._default);
                                                                            user.put(consts.priv,consts.all);
                                                                            currentUserDb.setValue(user);
                                                                        }


                                                                        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                                        mProgress.dismiss();
                                                                        i = 0;
                                                                        startActivity(intent);
                                                                        finish();


                                                                    }
                                                                } else {
                                                                    mProgress.dismiss();
                                                                    i = 0;
                                                                    String myString = getResources().getString(R.string.emailVerificationError);
                                                                    Toast.makeText(CreateAccountActivity.this, myString, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CreateAccountActivity.this, getString(R.string.emailInBase), Toast.LENGTH_LONG).show();
                                                mProgress.dismiss();
                                                i = 0;
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
                    }else{
                        Toast.makeText(CreateAccountActivity.this, getString(R.string.passEqualsError), Toast.LENGTH_LONG).show();
                        i = 0;
                        mProgress.dismiss();
                    }
                } else {
                    Toast.makeText(CreateAccountActivity.this, getString(R.string.nicknameError), Toast.LENGTH_LONG).show();
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