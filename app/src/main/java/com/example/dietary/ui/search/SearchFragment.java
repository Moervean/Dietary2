package com.example.dietary.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import Activites.addPersonActivity;
import Data.AddTrainerRecyclerAdapter;
import Data.SearchAddRecyclerAdapter;
import Model.Uzytkownik;

public class SearchFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private EditText nickname;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private RecyclerView recyclerView;
    private SearchAddRecyclerAdapter listRecyclerAdapter;
    private List<Uzytkownik> userList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View view = inflater.inflate(R.layout.activity_search,container,false);
        nickname = (EditText) view.findViewById(R.id.enterNickSearch);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        mRef= mDatabase.getReference().child("users");
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
                listRecyclerAdapter = new SearchAddRecyclerAdapter(getContext(), userList);
                recyclerView.setAdapter(listRecyclerAdapter);
                listRecyclerAdapter.notifyDataSetChanged();
                mRef.orderByChild("nickname").startAt(nickname.getText().toString()).endAt(nickname.getText().toString() + "\uf8ff").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        Uzytkownik us = snapshot.getValue(Uzytkownik.class);

                        userList.add(us);

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

//        searchStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nazwa = nickname.getText().toString();
//
//                mRef.orderByChild("nickname").startAt(nazwa).endAt(nazwa + "\uf8ff").addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        Uzytkownik us = snapshot.getValue(Uzytkownik.class);
//                        nicknameView.setText(us.getNickname());
//                        if(us.getImage() != "default")
//                            Picasso.get().load(us.getImage()).into(profilePicture);
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

        return view;
    }
}
