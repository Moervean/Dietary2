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
import android.widget.Toast;

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
import Activites.EditProtegeDietActivity;
import Activites.EditProtegeWorkoutActivity;
import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProtegeListRecyclerAdapter extends RecyclerView.Adapter<ProtegeListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<User> userList;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    @Inject
    Consts consts;

    public ProtegeListRecyclerAdapter(Context context, List<User> lista) {
        this.context = context;
        userList = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.protege_row, parent, false);

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final User user = userList.get(position);
        String imageUrl;

        holder.nickname.setText(user.getNickname());
        imageUrl = user.getImage();

        if(imageUrl.equals(consts._default)){
            holder.profilePicture.setBackground(context.getResources().getDrawable(R.drawable.basic_photo));
        }else {
            Picasso.get().load(imageUrl).into(holder.profilePicture);
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnotherUserProfile.class);
                intent.putExtra(consts.nickname, user.getNickname());
                context.startActivity(intent);

            }
        });
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.removeFriend))
                        .setMessage(context.getResources().getString(R.string.userRemoveConfirm))
                        .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRef.child(consts.users).child(mUser.getUid()).child(consts.proteges).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.getValue().equals(user.getNickname())){
                                            snapshot.getRef().setValue(null);
                                        }

                                        mRef.child(consts.users).orderByChild(consts.nickname).equalTo(user.getNickname()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    dataSnapshot.child(consts.coaches).child(mUser.getUid()).getRef().setValue(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        userList.remove(user);
                                        ProtegeListRecyclerAdapter.this.notifyItemRemoved(position);
                                        ProtegeListRecyclerAdapter.this.notifyItemRangeChanged(position,userList.size());
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
                        Toast.makeText(context,context.getResources().getString(R.string.no),Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });


        holder.diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProtegeDietActivity.class);
                intent.putExtra(consts.nickname, user.getNickname());
                context.startActivity(intent);
            }
        });

        holder.workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProtegeWorkoutActivity.class);
                intent.putExtra(consts.nickname, user.getNickname());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profilePicture;
        public TextView nickname;
        public ImageView add;
        public ImageView report;
        private ImageView diet;
        private ImageView workout;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            profilePicture = (CircleImageView) itemView.findViewById(R.id.listProfilePicture);
            nickname = (TextView) itemView.findViewById(R.id.listNickTV);
            add = (ImageView) itemView.findViewById(R.id.addPerson);
            report = (ImageView) itemView.findViewById(R.id.removePerson);
            diet = (ImageView) itemView.findViewById(R.id.protegeDiet);
            workout = (ImageView) itemView.findViewById(R.id.protegeWorkout);

            mRef = FirebaseDatabase.getInstance().getReference();
            mUser = FirebaseAuth.getInstance().getCurrentUser();

        }
    }
}
