package Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.dietary.NavigationBar;
import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {


    private Button log;
    private FirebaseAuth mAuth;
    private Button create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        mAuth = FirebaseAuth.getInstance();
        log = (Button)findViewById(R.id.login_start);
        create = (Button)findViewById(R.id.create_start);
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(StartActivity.this, NavigationBar.class));
            finish();
        }
        //TODO Auto logowanie ;)
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

}