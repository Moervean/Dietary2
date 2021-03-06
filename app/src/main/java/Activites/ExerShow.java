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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ExerShow extends AppCompatActivity {

    private RecyclerView rV;
    private ExerRecyclerAdapter exerRecyclerAdapter;
    private DatePickerDialog dialog;
    private Button addView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatePicker picker;
    @Inject
    Consts consts;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mapView:
                startActivity(new Intent(
                        ExerShow.this,
                        MapsActivity.class
                ));

        }
        return super.onOptionsItemSelected(item);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exer_show);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        Locale.setDefault(Locale.ENGLISH);
        addView = (Button)findViewById(R.id.addExer);
        picker = (DatePicker)findViewById(R.id.datePicker1);
        rV = (RecyclerView)findViewById(R.id.exerRV);
        addView = (Button)findViewById(R.id.addExer);
        dialog = new DatePickerDialog(this);
        rV.setHasFixedSize(true);
        rV.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        mRef = mDatabase.getReference().child(consts.users).child(mUser.getUid()).child(consts.workout);

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExerShow.this,ExerciseActivity.class));
                finish();
            }
        });
        String date = dateFormat.format(new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth()));

        mRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Workout> workoutsList = new LinkedList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Workout workout = dataSnapshot.getValue(Workout.class);

                    workoutsList.add(workout);
                }

                exerRecyclerAdapter = new ExerRecyclerAdapter(ExerShow.this, workoutsList);
                rV.setAdapter(exerRecyclerAdapter);

                exerRecyclerAdapter.notifyDataSetChanged();


            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = dateFormat.format(new Date(year - 1900, monthOfYear, dayOfMonth));
                mRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Workout> workoutList = new LinkedList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Workout diet = dataSnapshot.getValue(Workout.class);

                            workoutList.add(diet);
                        }

                        exerRecyclerAdapter = new ExerRecyclerAdapter(ExerShow.this, workoutList);
                        rV.setAdapter(exerRecyclerAdapter);

                        exerRecyclerAdapter.notifyDataSetChanged();


                    }@Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}