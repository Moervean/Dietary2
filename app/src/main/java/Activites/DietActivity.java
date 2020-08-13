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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import Data.DietRecyclerAdapter;
import Model.Diet;

public class DietActivity extends AppCompatActivity {
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
    private TextView wieghtTV;
    private RadioGroup radioGroup;


    private CheckBox weightCheck;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet2);


        Locale.setDefault(Locale.ENGLISH);
        mealDesc = (EditText) findViewById(R.id.exerciseDescription);
        mealWarn = (EditText) findViewById(R.id.exerciseWarnings);
        mealUpload = (Button) findViewById(R.id.uploadExercise);
        weight = (EditText) findViewById(R.id.weight);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        wieghtTV = (TextView)findViewById(R.id.weightTV);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);

        list = (Button)findViewById(R.id.dietList);
        mUser = mAuth.getCurrentUser();
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(java.lang.System.currentTimeMillis()).getTime());
        mRef = mDatabase.getReference().child("users").child(mUser.getUid()).child("dieta").child(formatedDate);





        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietActivity.this,DietShow.class));
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
                        String posilek = String.valueOf(c) + " posilek";
                        mRef.child(posilek).child("opis").setValue(desc);
                        mRef.child(posilek).child("uwagi").setValue(warn);
                        mRef.child(posilek).child("waga").setValue(wei);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}