package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import Data.ProtegeListRecyclerAdapter;
import Model.User;

public class ProtegesList extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private RecyclerView recyclerView;
    private ProtegeListRecyclerAdapter protegeListRecyclerAdapter;
    private List<User> userList;
    private FirebaseAuth mAuth;
    private int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proteges_list);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("users").child(mUser.getUid()).child("podopieczni");
        mDatabaseReference.keepSynced(true);

        i=0;
        userList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.protegesRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
    }
    @Override
    protected void onStart() {

        super.onStart();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> nazwy = new LinkedList<String>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("NAZWYY",dataSnapshot.getValue().toString());
                    nazwy.add(dataSnapshot.getValue().toString());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
                for(String nazwa : nazwy){

                    reference.orderByChild("nickname").equalTo(nazwa).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            User us = snapshot.getValue(User.class);


                            userList.add(us);
                            if(userList.size()==nazwy.size() ) {
                                protegeListRecyclerAdapter = new ProtegeListRecyclerAdapter(ProtegesList.this, userList);
                                recyclerView.setAdapter(protegeListRecyclerAdapter);
                                protegeListRecyclerAdapter.notifyDataSetChanged();
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
    public interface MyCallback {
        void onCallback(List<User> value);
    }
    public void readData(final MyCallback myCallback, String nazwa) {
        FirebaseDatabase.getInstance().getReference().child("users").orderByKey().equalTo(nazwa).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> usList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    User us = snapshot.getValue(User.class);
                    usList.add(us);
                }

                myCallback.onCallback(usList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}