package Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dietary.NavigationBar;
import com.example.dietary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button login;
    private TextView createAcc;
    private EditText email;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        login = (Button) findViewById(R.id.loginLog);
        pass = (EditText) findViewById(R.id.passLog);
        email = (EditText) findViewById(R.id.emailLog);
//

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();


                if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(password)) {
                    mAuth.signInWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            final FirebaseUser us = mAuth.getCurrentUser();
                            if (us.isEmailVerified()) {
                                Intent intent = new Intent(LoginActivity.this, NavigationBar.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.noEmailVerification), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.loginError), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.blankEmailAndPassError), Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}