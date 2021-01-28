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
import Data.DietRecyclerAdapter;
import Model.Diet;

public class DietShow extends AppCompatActivity {
    private DietRecyclerAdapter dietRecyclerAdapter;
    private DatePickerDialog dialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button add;
    private RecyclerView dietRV;
    private DatePicker datePicker;
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
                        DietShow.this,
                        InformationActivity.class
                ));

        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_show);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        Locale.setDefault(Locale.ENGLISH);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        add= (Button)findViewById(R.id.addDiet);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dietRV = (RecyclerView)findViewById(R.id.dietRV);
        dialog = new DatePickerDialog(DietShow.this);
        mRef = mDatabase.getReference().child(consts.users).child(mUser.getUid()).child(consts.diet);
        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        dietRV.setHasFixedSize(true);
        dietRV.setLayoutManager(new LinearLayoutManager(this));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietShow.this, AddDietActivity.class));
                finish();
            }
        });

        String date = dateFormat.format(new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth()));
        mRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Diet>dietList = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Diet diet = dataSnapshot.getValue(Diet.class);
                    dietList.add(diet);
                }

                dietRecyclerAdapter = new DietRecyclerAdapter(DietShow.this, dietList);
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

                        dietRecyclerAdapter = new DietRecyclerAdapter(DietShow.this, dietList);
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