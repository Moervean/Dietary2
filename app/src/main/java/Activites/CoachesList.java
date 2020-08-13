package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Data.ListRecyclerAdapter;
import Model.Uzytkownik;

public class CoachesList extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private RecyclerView recyclerView;
    private ListRecyclerAdapter listRecyclerAdapter;
    private List<Uzytkownik> userList;
    private FirebaseAuth mAuth;
    private int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_list);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("users").child(mUser.getUid()).child("trenerzy");
        mDatabaseReference.keepSynced(true);

        i=0;
        userList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.coachesRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {

        super.onStart();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> nazwy = new LinkedList<String>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    nazwy.add(dataSnapshot.getValue().toString());
                    Log.d("NAZWA", dataSnapshot.getKey());
                    Log.d("NAZWA", dataSnapshot.getValue().toString());


                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
                for(String nazwa : nazwy){

                    reference.orderByKey().equalTo(nazwa).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Uzytkownik us = snapshot.getValue(Uzytkownik.class);


                            userList.add(us);
                            if(userList.size()==nazwy.size() ) {
                                listRecyclerAdapter = new ListRecyclerAdapter(CoachesList.this, userList);
                                recyclerView.setAdapter(listRecyclerAdapter);
                                listRecyclerAdapter.notifyDataSetChanged();
                            }
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}