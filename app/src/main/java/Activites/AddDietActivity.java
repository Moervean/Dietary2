package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.Locale;

import javax.inject.Inject;

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;

public class AddDietActivity extends AppCompatActivity {
    private EditText mealDesc;
    private EditText mealWarn;
    private Button mealUpload;
    private EditText weight;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button add;
    private Button list;
    @Inject
    Consts consts;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.getInfo:
                startActivity(new Intent(
                        AddDietActivity.this,
                        InformationActivity.class
                ));

        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        Locale.setDefault(Locale.ENGLISH);
        mealDesc = (EditText) findViewById(R.id.exerciseDescription);
        mealWarn = (EditText) findViewById(R.id.exerciseWarnings);
        mealUpload = (Button) findViewById(R.id.uploadExercise);
        weight = (EditText) findViewById(R.id.weight);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        list = (Button)findViewById(R.id.dietList);
        mUser = mAuth.getCurrentUser();
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(java.lang.System.currentTimeMillis()).getTime());
        mRef = mDatabase.getReference().child(consts.users).child(mUser.getUid()).child(consts.diet).child(formatedDate);





        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddDietActivity.this,DietShow.class));
                finish();
            }
        });


        mealUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc= mealDesc.getText().toString();
                final String warn = mealWarn.getText().toString();
                final String wei = weight.getText().toString();

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long c = snapshot.getChildrenCount();
                        String dinner = String.valueOf(c) + " " + consts.dinner;
                        mRef.child(dinner).child(consts.desc).setValue(desc);
                        mRef.child(dinner).child(consts.warn).setValue(warn);
                        mRef.child(dinner).child(consts.weight).setValue(wei);
                        mealDesc.setText("");
                        mealWarn.setText("");
                        weight.setText("");
                        Toast.makeText(AddDietActivity.this,getString(R.string.meal_added),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}