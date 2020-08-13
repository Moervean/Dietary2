package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import Data.ExerRecyclerAdapter;
import Model.Trening;

public class ExerciseActivity extends AppCompatActivity {
    private EditText mealDesc;
    private EditText mealWarn;
    private Button mealUpload;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Button exerListView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Locale.setDefault(Locale.ENGLISH);
        mealDesc = (EditText) findViewById(R.id.exerciseDescription);
        mealWarn = (EditText) findViewById(R.id.exerciseWarnings);
        mealUpload = (Button) findViewById(R.id.uploadExercise);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        exerListView = (Button)findViewById(R.id.showExerList);
        mUser = mAuth.getCurrentUser();
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(java.lang.System.currentTimeMillis()).getTime());
        mRef = mDatabase.getReference().child("users").child(mUser.getUid()).child("trening").child(formatedDate);



        exerListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExerciseActivity.this,ExerShow.class));
                finish();
            }
        });

        mealUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = mealDesc.getText().toString();
                final String warn = mealWarn.getText().toString();

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long c = snapshot.getChildrenCount();
                        String posilek = String.valueOf(c) + " trening";
                        mRef.child(posilek).child("opis").setValue(desc);
                        mRef.child(posilek).child("uwagi").setValue(warn);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}