package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;


import Dagger.DaggerDaggerConstsComponent;
import Data.AddTrainerRecyclerAdapter;
import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Model.User;

public class AddPersonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private AddTrainerRecyclerAdapter listRecyclerAdapter;
    private List<User> userList;
    @Inject
    Consts consts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        recyclerView = (RecyclerView)findViewById(R.id.addPersonsRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userList = new LinkedList<>();
        mRef = FirebaseDatabase.getInstance().getReference().child(consts.friend_request).child(mUser.getUid());

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List <String> nazwy = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    nazwy.add(dataSnapshot.getKey());

                }
                for(String nazwa : nazwy){
                    FirebaseDatabase.getInstance().getReference().child(consts.users).orderByKey().equalTo(nazwa).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            User us = snapshot.getValue(User.class);


                            userList.add(us);
                            if(userList.size()==nazwy.size() ) {
                                listRecyclerAdapter = new AddTrainerRecyclerAdapter(AddPersonActivity.this, userList);
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