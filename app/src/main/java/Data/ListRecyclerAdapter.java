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
import androidx.recyclerview.widget.RecyclerView;


import com.example.dietary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activites.AnotherUserProfile;
import Model.Uzytkownik;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Uzytkownik> listauzytkownikow;

    public ListRecyclerAdapter(Context context, List<Uzytkownik> lista) {
        this.context = context;
        listauzytkownikow = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Uzytkownik uzytkownik = listauzytkownikow.get(position);
        String imageUrl = null;
        holder.nickname.setText(uzytkownik.getNickname());
        imageUrl = uzytkownik.getImage();
        //TODO: default URl
        Picasso.get().load(imageUrl).into(holder.profilePicture);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnotherUserProfile.class);
                intent.putExtra("nickname",uzytkownik.getNickname());
                context.startActivity(intent);

            }
        });
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("Remove Freind")
                        .setMessage("Jesteś pewny, że chcesz usunąć tą osobę?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"Tak",Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"Nie",Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return listauzytkownikow.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePicture;
        public TextView nickname;
        public ImageView add;
        public ImageView send;
        public ImageView report;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            profilePicture = (ImageView) itemView.findViewById(R.id.listProfilePicture);
            nickname = (TextView) itemView.findViewById(R.id.listNickTV);
            add = (ImageView) itemView.findViewById(R.id.addPerson);
            report = (ImageView) itemView.findViewById(R.id.removePerson);

        }
    }
}
