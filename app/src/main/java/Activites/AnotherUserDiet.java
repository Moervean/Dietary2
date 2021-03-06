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
import Data.DietRecyclerAdapter;
import Model.Diet;

public class AnotherUserDiet extends AppCompatActivity {
    private DatePicker datePicker;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DietRecyclerAdapter dietRecyclerAdapter;
    private DatePickerDialog dialog;
    private RecyclerView dietRV;
    @Inject
    Consts consts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_diet);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);


        Locale.setDefault(Locale.ENGLISH);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dialog = new DatePickerDialog(AnotherUserDiet.this);
        dietRV = (RecyclerView)findViewById(R.id.dietRVAnother);
        datePicker = (DatePicker)findViewById(R.id.datePickerAnother);
        mUser = mAuth.getCurrentUser();
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        final String id = getIntent().getStringExtra(consts.ID);
        mRef = mDatabase.getReference().child(consts.users).child(id).child(consts.diet);
        dietRV.setHasFixedSize(true);
        dietRV.setLayoutManager(new LinearLayoutManager(this));
        String date = dateFormat.format(new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth()));

        mRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> nazwy  = new LinkedList<>();
                List<Diet>dietList = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diet diet = dataSnapshot.getValue(Diet.class);
                    dietList.add(diet);
                }

                dietRecyclerAdapter = new DietRecyclerAdapter(AnotherUserDiet.this, dietList);
                dietRV.setAdapter(dietRecyclerAdapter);
                dietRecyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = dateFormat.format(new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth()));
                mRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Diet>dietList = new LinkedList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Diet diet = dataSnapshot.getValue(Diet.class);
                            dietList.add(diet);
                        }

                        dietRecyclerAdapter = new DietRecyclerAdapter(AnotherUserDiet.this, dietList);
                        dietRV.setAdapter(dietRecyclerAdapter);
                        dietRecyclerAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}