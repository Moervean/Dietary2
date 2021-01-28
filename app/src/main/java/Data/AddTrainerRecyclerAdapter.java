package Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Model.User;

public class AddTrainerRecyclerAdapter extends RecyclerView.Adapter<AddTrainerRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<User> userList;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    @Inject
    Consts consts;


    public AddTrainerRecyclerAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_add_row,parent,false);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final User trainer = userList.get(position);

        holder.trainerNick.setText(trainer.getNickname());
        Picasso.get().load(trainer.getImage()).into(holder.addTrainer);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.orderByChild(consts.nickname).equalTo(userList.get(position).getNickname()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        String s = snapshot.getKey();
                        for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                            if(dataSnapshot.getKey().equals(consts.nickname))
                                s=dataSnapshot.getValue(String.class);
                        }
                        DatabaseReference podAdd = mRef.child(mAuth.getCurrentUser().getUid()).child(consts.proteges);
                        podAdd.child(snapshot.getKey()).setValue(s);

                        final DatabaseReference coachAdd = mRef.child(snapshot.getKey()).child(consts.coaches);
                        final String trainerID = snapshot.getKey();

                        mRef.child(mAuth.getCurrentUser().getUid()).child(consts.nickname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String trainerNickname = snapshot.getValue(String.class);

                                coachAdd.child(mAuth.getCurrentUser().getUid()).setValue(trainerNickname);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        DatabaseReference removeRequest = FirebaseDatabase.getInstance().getReference().child(consts.friend_request).child(mUser.getUid()).child(trainerID);
                        removeRequest.setValue(null);
                        userList.remove(position);

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
            mRef = mDatabase.getReference().child(consts.users);

        }
    }
}
