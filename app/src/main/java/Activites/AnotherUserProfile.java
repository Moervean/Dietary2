package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Model.User;

public class AnotherUserProfile extends AppCompatActivity {
    private ImageView anotherPict;
    private TextView nickname;
    private ImageView diets;
    private ImageView exercises;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button diet;
    private Button exer;
    private String ID= null;
    @Inject
    Consts consts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_profile);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);


        anotherPict = (ImageView)findViewById(R.id.anotherProfile);
        nickname = (TextView)findViewById(R.id.anotherNick);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child(consts.users);
        mAuth = FirebaseAuth.getInstance();
        diet = (Button)findViewById(R.id.dietButtonAnother);
        exer = (Button)findViewById(R.id.exerciseButtonAnother);

        mUser = mAuth.getCurrentUser();
        final String s = getIntent().getStringExtra(consts.nickname);
        nickname.setText(s);


        mRef.orderByChild(consts.nickname).equalTo(s).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User us = snapshot.getValue(User.class);
                ID = snapshot.getKey();
                Picasso.get().load(us.getImage()).into(anotherPict);
                for(final DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(consts.priv)){
                        if(dataSnapshot.getValue().toString().equals(consts.noOne)){
                            diet.setVisibility(View.INVISIBLE);
                            exer.setVisibility(View.INVISIBLE);
                        }else if(dataSnapshot.getValue().toString().equals(consts.friend_request)){
                            diet.setVisibility(View.INVISIBLE);
                            exer.setVisibility(View.INVISIBLE);
                            mRef.child(ID).child(consts.coaches).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        if(dataSnapshot1.getValue().toString().equals(mUser.getUid().toString())){
                                            diet.setVisibility(View.VISIBLE);
                                            exer.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            mRef.child(ID).child(consts.proteges).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        if(dataSnapshot1.getValue().toString().equals(mUser.getUid().toString())){
                                            diet.setVisibility(View.VISIBLE);
                                            exer.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
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
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.orderByChild(consts.nickname).equalTo(s).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Intent intent = new Intent(AnotherUserProfile.this,AnotherUserDiet.class);
                        intent.putExtra(consts.ID,snapshot.getKey());
                        startActivity(intent);
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
        });
        exer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.orderByChild(consts.nickname).equalTo(s).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Intent intent = new Intent(AnotherUserProfile.this,AnotherExerActivity.class);
                        intent.putExtra(consts.ID,snapshot.getKey());
                        startActivity(intent);
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
        });



    }
}