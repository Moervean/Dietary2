package Data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dietary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Uzytkownik;

public class AddTrainerRecyclerAdapter extends RecyclerView.Adapter<AddTrainerRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Uzytkownik> userList;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private Button confirmAdd;
    private Button declineAdd;


    public AddTrainerRecyclerAdapter(Context context, List<Uzytkownik> userList) {
        this.context = context;
        this.userList = userList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_add_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Uzytkownik trainer = userList.get(position);

        holder.trainerNick.setText(trainer.getNickname());
        Picasso.get().load(trainer.getImage()).into(holder.addTrainer);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("users").orderByChild("nickname").equalTo(userList.get(position).getNickname()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        //TODO Wyslanie Potiwerdzenia zaproszenia


                        String s = snapshot.getKey();
                        DatabaseReference podAdd = mRef.child(mAuth.getCurrentUser().getUid()).child("podopieczni");
                        podAdd.push().setValue(s);

                        DatabaseReference coachAdd = mRef.child(s).child("trenerzy");
                        coachAdd.push().setValue(mAuth.getCurrentUser().getUid());

                        DatabaseReference removeRequest = FirebaseDatabase.getInstance().getReference().child("Friends_request").child(mUser.getUid()).child(s);
                        removeRequest.setValue(null);
                        userList.remove(position);

                        //TODO test jak są 2+ osoby
                        AddTrainerRecyclerAdapter.this.notifyItemRemoved(position);
                        AddTrainerRecyclerAdapter.this.notifyItemRangeChanged(position,userList.size());
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

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView addTrainer;
        private TextView trainerNick;
        private ImageView accept;
        private ImageView decline;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            addTrainer = (ImageView)itemView.findViewById(R.id.addTrainerImage);
            trainerNick = (TextView)itemView.findViewById(R.id.addTrainerNick);
            accept = (ImageView)itemView.findViewById(R.id.addTrainerAccept);
            decline = (ImageView)itemView.findViewById(R.id.addTreinerDecline);
            mDatabase = FirebaseDatabase.getInstance();

            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            mRef = mDatabase.getReference().child("users");

        }
    }
}
