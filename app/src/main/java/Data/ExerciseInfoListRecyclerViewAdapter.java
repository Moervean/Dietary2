package Data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietary.R;

import java.util.ArrayList;
import java.util.List;

import Model.Exercise;

public class ExerciseInfoListRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseInfoListRecyclerViewAdapter.MyViewHolder> {
    private List<Exercise>exerciseList;
    private Context context;

    public ExerciseInfoListRecyclerViewAdapter(List<Exercise>exerciseList,Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_info_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Exercise exercise = exerciseList.get(position);
        holder.exerName.setText(exercise.getName());
        holder.exerDesc.setText(exercise.getDesc());
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(exercise.getUrl())));
            }
        });


    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView exerName;
        private TextView exerDesc;
        private Button url;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            exerDesc = (TextView)itemView.findViewById(R.id.exerciseDesc);
            exerName = (TextView)itemView.findViewById(R.id.exerciseName);
            url = (Button)itemView.findViewById(R.id.exerciseVideo);
        }
    }
}
