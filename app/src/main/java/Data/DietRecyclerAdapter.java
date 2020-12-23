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

import Model.Diet;

public class DietRecyclerAdapter extends RecyclerView.Adapter<DietRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Diet> dietList;

    public DietRecyclerAdapter(Context context, List<Diet> dietList) {
        this.context = context;
        this.dietList = dietList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dietrow,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diet diet = dietList.get(position);


        holder.mealWarn.setText(diet.getUwagi());
        holder.mealDesc.setText(diet.getOpis());
        holder.weight.setText(diet.getWaga());
        holder.mealNumber.setText(context.getString(R.string.meal) + " " + (position+1));

    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView mealDesc;
        public TextView mealWarn;
        public TextView weight;
        public TextView mealNumber;

        public ViewHolder(@NonNull View view, Context ctxt) {
            super(view);
            context =ctxt;

            mealDesc = (TextView)view.findViewById(R.id.dietRowDesc);
            mealWarn = (TextView)view.findViewById(R.id.dietRowWarn);
            weight = (TextView)view.findViewById(R.id.dietRowWeight);
            mealNumber = (TextView)view.findViewById(R.id.mealnumber);
        }
    }
}
