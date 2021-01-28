package com.example.dietary.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Data.SearchAddRecyclerAdapter;
import Model.User;

public class SearchFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private EditText nickname;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private RecyclerView recyclerView;
    private SearchAddRecyclerAdapter listRecyclerAdapter;
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

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View view = inflater.inflate(R.layout.activity_search,container,false);
        nickname = (EditText) view.findViewById(R.id.enterNickSearch);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance();
        mRef= mDatabase.getReference().child(consts.users);
        recyclerView = (RecyclerView)view.findViewById(R.id.searchRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new LinkedList<>();


        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userList = new LinkedList<>();
                final List<String> friendsList = new LinkedList<>();
                listRecyclerAdapter = new SearchAddRecyclerAdapter(getContext(), userList);
                recyclerView.setAdapter(listRecyclerAdapter);
                listRecyclerAdapter.notifyDataSetChanged();
                String nick;



                if(!TextUtils.isEmpty(nickname.getText().toString())) {
                    nick = nickname.getText().toString().substring(0, 1).toUpperCase() + nickname.getText().toString().substring(1).toLowerCase();
                }
                else{
                    nick = "";
                }


                mRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.child(consts.coaches).getChildren()){
                            friendsList.add(dataSnapshot.getValue(String.class));
                        }
                        for(DataSnapshot dataSnapshot : snapshot.child(consts.proteges).getChildren()){
                            friendsList.add(dataSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                mRef.orderByChild(consts.nickname).startAt(nick).endAt(nick + "\uf8ff").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        User us = snapshot.getValue(User.class);

                        int check=0;
                        if(snapshot.getKey().equals(mUser.getUid())){

                        }else {
                            for(String s : friendsList){
                                if(s.equals(snapshot.child(consts.nickname).getValue(String.class))) {
                                    check=1;
                                    break;
                                }


                            }
                            if(check==0)
                            userList.add(us);

                        }
                        listRecyclerAdapter = new SearchAddRecyclerAdapter(getContext(), userList);
                        recyclerView.setAdapter(listRecyclerAdapter);
                        listRecyclerAdapter.notifyDataSetChanged();
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
}
