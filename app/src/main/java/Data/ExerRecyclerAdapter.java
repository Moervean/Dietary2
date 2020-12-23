package Data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dietary.R;

import java.util.List;

import Model.Workout;

public class ExerRecyclerAdapter extends RecyclerView.Adapter<ExerRecyclerAdapter.ViewHolder> {
    private List<Workout> exerList;
    private Context context;

    public ExerRecyclerAdapter( Context context,List<Workout> exerList) {
        this.exerList = exerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exer_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Workout tren = exerList.get(position);
        holder.exernumber.setText(position + 1 + " trening");
        holder.exerWarn.setText(tren.getUwagi());
        holder.exerDesc.setText(tren.getOpis());
        Log.d("TRENING",tren.getOpis());

    }

    @Override
    public int getItemCount() {
        return exerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exerDesc;
        private TextView exerWarn;
        private TextView exernumber;

        public ViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;

            exerDesc = (TextView)view.findViewById(R.id.exerRowDesc);
            exerWarn = (TextView)view.findViewById(R.id.exerRowWarn);
            exernumber = (TextView)view.findViewById(R.id.exernumber);

        }
    }
}
