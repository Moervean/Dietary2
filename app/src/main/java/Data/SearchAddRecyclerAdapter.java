package Data;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
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
import com.google.firebase.database.snapshot.ChildKey;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import Model.User;
import io.perfmark.Link;

public class SearchAddRecyclerAdapter extends RecyclerView.Adapter<SearchAddRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<User> list;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private ProgressDialog mProgressBar;

    public SearchAddRecyclerAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchAddRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchrow,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAddRecyclerAdapter.ViewHolder holder, final int position) {

        final User us = list.get(position);
        mProgressBar = new ProgressDialog(context);
        mProgressBar.show();
        holder.trainerNick.setText(us.getNickname());
        if(!us.getImage().equals("default")) {
            Picasso.get().load(us.getImage()).into(holder.addTrainer);
        }else{
            holder.addTrainer.setBackground(context.getResources().getDrawable(R.drawable.basic_photo));
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwa = us.getNickname();


                mRef.orderByChild("nickname").equalTo(nazwa).addChildEventListener(new ChildEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        //TODO Sprawdzenie czy jeśli wyślemy dwa zaproszenia do kogoś jedno nie wyzeruje wszystkiego

                        notifyDataSetChanged();
                        String s = snapshot.getKey();
                        final DatabaseReference SendRequest = FirebaseDatabase.getInstance().getReference().child("Friends_request").child(s).child(mAuth.getCurrentUser().getUid());

                        SendRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.hasChildren()){
                                    SendRequest.push().setValue("Request_Send");
                                    Toast.makeText(context,context.getString(R.string.userAdded),Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        list.remove(us);
                        SearchAddRecyclerAdapter.this.notifyItemRemoved(position);
                        SearchAddRecyclerAdapter.this.notifyItemRangeChanged(position,list.size());

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
        mProgressBar.dismiss();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView addTrainer;
        private TextView trainerNick;
        private ImageView add;
        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;
            addTrainer = (ImageView)itemView.findViewById(R.id.addSearchImage);
            trainerNick = (TextView)itemView.findViewById(R.id.addSearchNick);
            add = (ImageView)itemView.findViewById(R.id.addSearchAccept);
            mDatabase = FirebaseDatabase.getInstance();

            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            mRef = mDatabase.getReference().child("users");
        }
    }
}
