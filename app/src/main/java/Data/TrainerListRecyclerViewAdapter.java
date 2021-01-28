package Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import Activites.AnotherUserProfile;
import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerListRecyclerViewAdapter extends RecyclerView.Adapter<TrainerListRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<User> userList;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    @Inject
    Consts consts;


    public TrainerListRecyclerViewAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trainer_row,parent,false);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        return new MyViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final User us  = userList.get(position);
        String imageUrl;

        holder.nickname.setText(us.getNickname());
        imageUrl = us.getImage();

        if(imageUrl.equals(consts._default)){
            holder.profilePicture.setBackground(context.getResources().getDrawable(R.drawable.basic_photo));
        }else {
            Picasso.get().load(imageUrl).into(holder.profilePicture);
        }
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnotherUserProfile.class);
                intent.putExtra(consts.nickname, us.getNickname());
                context.startActivity(intent);
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.removeFriend))
                        .setMessage(context.getResources().getString(R.string.userRemoveConfirm))
                        .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRef.child(mUser.getUid()).child(consts.coaches).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue().equals(us.getNickname())){
                                           snapshot.getRef().setValue(null);
                                        }

                                        mRef.orderByChild(consts.nickname).equalTo(us.getNickname()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    dataSnapshot.child(consts.proteges).child(mUser.getUid()).getRef().setValue(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        userList.remove(us);
                                        TrainerListRecyclerViewAdapter.this.notifyItemRemoved(position);
                                        TrainerListRecyclerViewAdapter.this.notifyItemRangeChanged(position,userList.size());
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
                        })
                        .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private ImageView remove;
        private CircleImageView profilePicture;
        private TextView nickname;
        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            profile = (ImageView) itemView.findViewById(R.id.trainerProfile);
            remove = (ImageView) itemView.findViewById(R.id.trainerRemove);
            profilePicture = (CircleImageView) itemView.findViewById(R.id.trainerProfilePicture);
            nickname = (TextView) itemView.findViewById(R.id.trainerNickname);

            mRef = FirebaseDatabase.getInstance().getReference().child(consts.users);
            mUser = FirebaseAuth.getInstance().getCurrentUser();


        }
    }
}
