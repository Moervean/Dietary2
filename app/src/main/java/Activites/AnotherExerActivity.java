package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

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

import javax.inject.Inject;

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Data.ExerRecyclerAdapter;
import Model.Workout;

public class AnotherExerActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private RecyclerView exerRV;
    private ExerRecyclerAdapter exerRecyclerAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private DatePickerDialog dialog;
    private FirebaseUser mUser;
    @Inject
    Consts consts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_exer);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);


        Locale.setDefault(Locale.ENGLISH);
        datePicker = (DatePicker)findViewById(R.id.datePickerAnotherExer);
        exerRV = (RecyclerView)findViewById(R.id.exerRVAnother);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dialog = new DatePickerDialog(this);
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(java.lang.System.currentTimeMillis()).getTime());
        String ID = getIntent().getStringExtra(consts.ID);
        mRef = mDatabase.getReference().child(consts.users).child(ID).child(consts.workout);
        exerRV.setHasFixedSize(true);
        exerRV.setLayoutManager(new LinearLayoutManager(this));
        String data = dateFormat.format(new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth()));

        mRef.child(data).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Workout> dietList = new LinkedList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Workout diet = dataSnapshot.getValue(Workout.class);

                    dietList.add(diet);
                }
                exerRecyclerAdapter = new ExerRecyclerAdapter(AnotherExerActivity.this,dietList);
                exerRV.setAdapter(exerRecyclerAdapter);
                exerRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String data = dateFormat.format(new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth()));

                mRef.child(data).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Workout> dietList = new LinkedList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Workout diet = dataSnapshot.getValue(Workout.class);

                            dietList.add(diet);
                        }
                        exerRecyclerAdapter = new ExerRecyclerAdapter(AnotherExerActivity.this,dietList);
                        exerRV.setAdapter(exerRecyclerAdapter);
                        exerRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}