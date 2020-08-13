package Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.Uzytkownik;

public class SearchActivity extends AppCompatActivity {
    private EditText nickname;
    private Button searchStart;
    private ImageView profilePicture;
    private ImageButton mail;
    private TextView nicknameView;
    private ImageButton add;
    private ImageButton report;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // TODO Informacja o braku użytkowika w bazie
        nickname = (EditText) findViewById(R.id.enterNickSearch);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        mRef= mDatabase.getReference().child("users");



        searchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchActivity.this,"Wcisniety",Toast.LENGTH_LONG).show();
                String nazwa = nickname.getText().toString();


                mRef.orderByChild("nickname").startAt(nazwa).endAt(nazwa + "\uf8ff").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        Uzytkownik us = snapshot.getValue(Uzytkownik.class);

                        nicknameView.setText(us.getNickname());
                        if(us.getImage() != "default")
                            Picasso.get().load(us.getImage()).into(profilePicture);
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwa = nickname.getText().toString();


                mRef.orderByChild("nickname").equalTo(nazwa).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        //TODO Sprawdzenie czy jeśli wyślemy dwa zaproszenia do kogoś jedno nie wyzeruje wszystkiego
                        Uzytkownik us = snapshot.getValue(Uzytkownik.class);

                        String s = snapshot.getKey();
                        DatabaseReference SendRequest = FirebaseDatabase.getInstance().getReference().child("Friends_request").child(s).child(mAuth.getCurrentUser().getUid());
                        SendRequest.push().setValue("Request_Send");
//                        DatabaseReference podAdd = mRef.child(mAuth.getCurrentUser().getUid()).child("podopieczni");
//                        podAdd.push().setValue(s);
//
//                        DatabaseReference coachAdd = mRef.child(s).child("trenerzy");
//                        coachAdd.push().setValue(mAuth.getCurrentUser().getUid());



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