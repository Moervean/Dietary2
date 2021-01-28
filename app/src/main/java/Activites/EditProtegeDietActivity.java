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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import Data.AddMealDialog;
import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Data.DietRecyclerAdapter;
import Model.Diet;

public class EditProtegeDietActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private RecyclerView mealsList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DietRecyclerAdapter dietRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String nickname;
    private List<Diet> dietList;
    private String ID;
    String date;
    private Button addMealButton;
    private AddMealDialog addMealDialog;
    private String path;
    private ProgressDialog progressBar;
    @Inject
    Consts consts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_protege_diet);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        Locale.setDefault(Locale.ENGLISH);
        datePicker = (DatePicker) findViewById(R.id.editProtegeDietDatePicker);
        mealsList = (RecyclerView) findViewById(R.id.editProtegeDietRV);
        layoutManager = new LinearLayoutManager(this);
        mealsList.setLayoutManager(layoutManager);
        mealsList.setHasFixedSize(true);
        addMealButton = (Button) findViewById(R.id.addMealToProtegeButton);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressBar = new ProgressDialog(this);

        final java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        nickname = getIntent().getStringExtra(consts.nickname);
        mRef = mDatabase.getReference().child(consts.users);
        date = dateFormat.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));

        mRef.orderByChild(consts.nickname).equalTo(nickname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                progressBar.show();
                ID=snapshot.getKey();
                searchDiets();


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
                searchDiets();
            }
        });
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealDialog = new AddMealDialog(path);
                addMealDialog.show(getSupportFragmentManager(),"Dialog Fragment");
            }
        });



    }

    void searchDiets(){

        mRef.child(ID).child(consts.diet).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Diet> dietList = new LinkedList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Diet diet = dataSnapshot.getValue(Diet.class);

                    dietList.add(diet);
                }

                mealsList.setAdapter(new DietRecyclerAdapter(EditProtegeDietActivity.this,dietList));
                progressBar.dismiss();
                path = consts.users + "/" + ID + "/" + consts.diet + "/" + date;

            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}