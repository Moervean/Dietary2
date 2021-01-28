package com.example.dietary.ui.invites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Data.AddTrainerRecyclerAdapter;
import Dagger.AppModule;
import Dagger.Consts;
import Model.User;

public class InvitesFragment extends Fragment {

    private InvitesViewModel invitesViewModel;
    private RecyclerView recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private AddTrainerRecyclerAdapter listRecyclerAdapter;
    private List<User> userList;
    @Inject
    Consts consts;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        invitesViewModel =
                ViewModelProviders.of(this).get(InvitesViewModel.class);
        View root = inflater.inflate(R.layout.activity_add_person, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.addPersonsRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabase= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userList = new LinkedList<>();
        mRef = mDatabase.getReference().child(consts.friend_request).child(mUser.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List <String> names = new LinkedList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    names.add(dataSnapshot.getKey());

                }
                for(String name : names){
                    mDatabase.getReference().child(consts.users).orderByKey().equalTo(name).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            User us = snapshot.getValue(User.class);


                            userList.add(us);
                            if(userList.size()==names.size() ) {
                                listRecyclerAdapter = new AddTrainerRecyclerAdapter(getContext(), userList);
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

return root;
    }
}
