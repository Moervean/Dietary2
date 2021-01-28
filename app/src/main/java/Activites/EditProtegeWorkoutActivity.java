package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.dietary.R;
import com.google.firebase.database.ChildEventListener;
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

import Dagger.DaggerDaggerConstsComponent;
import Data.AddWorkoutDialog;
import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Data.ExerRecyclerAdapter;
import Model.Workout;

public class EditProtegeWorkoutActivity extends AppCompatActivity {

    private RecyclerView exerciseList;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String ID;
    private String nickname;
    private DatePicker datePicker;
    private String date;
    private Button addWorkout;
    private AddWorkoutDialog addWorkoutDialog;
    private ProgressDialog progressBar;
    private String path;
    public static final String KEY_PATH="path";
    @Inject
    Consts consts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_protege_workout);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        Locale.setDefault(Locale.ENGLISH);
        datePicker = (DatePicker) findViewById(R.id.editProtegeWorkoutDataPicker);
        exerciseList = (RecyclerView) findViewById(R.id.editProtegeWorkoutRV);
        layoutManager = new LinearLayoutManager(this);
        exerciseList.setLayoutManager(layoutManager);
        exerciseList.setHasFixedSize(true);
        addWorkout = (Button) findViewById(R.id.addWorkoutToProtege);

        progressBar = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child(consts.users);

        nickname = getIntent().getStringExtra(consts.nickname);

        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        date = dateFormat.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));


        mRef.orderByChild(consts.nickname).equalTo(nickname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.show();
                ID= snapshot.getKey();
                searchWorkouts();
                
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                date = dateFormat.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));
                searchWorkouts();
            }
        });
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(path!=null && path!="")
                addWorkoutDialog = new AddWorkoutDialog(path);
                addWorkoutDialog.show(getSupportFragmentManager(),"Dialog Fragment");
            }
        });



    }

    private void searchWorkouts() {

        if (date != null && ID != null) {
            mRef.child(ID).child(consts.workout).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Workout> workoutList = new LinkedList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        workoutList.add(dataSnapshot.getValue(Workout.class));
                    }
                    exerciseList.setAdapter(new ExerRecyclerAdapter(EditProtegeWorkoutActivity.this,
                            workoutList));
                    progressBar.dismiss();
                    path = consts.users +  "/" + ID + "/" + consts.workout +"/"+date;
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}