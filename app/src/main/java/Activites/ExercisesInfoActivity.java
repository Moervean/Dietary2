package Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dietary.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Data.ExerciseInfoListRecyclerViewAdapter;
import Model.Exercise;

public class ExercisesInfoActivity extends AppCompatActivity {


    private RecyclerView exercisesList;
    private RecyclerView.LayoutManager layoutManager;
    private ExerciseInfoListRecyclerViewAdapter adapter;
    private List<Exercise> exerciseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_info);

        exerciseList = new LinkedList<>();
        SetExercises();

        layoutManager = new LinearLayoutManager(this);
        exercisesList = (RecyclerView)findViewById(R.id.exercisesListRV);
        adapter = new ExerciseInfoListRecyclerViewAdapter(exerciseList,this);
        exercisesList.setLayoutManager(layoutManager);
        exercisesList.setHasFixedSize(true);
        exercisesList.setAdapter(adapter);
    }
    void SetExercises(){
        exerciseList.add(new Exercise(getResources().getString(R.string.deadlift)
                ,getResources().getString(R.string.deadliftDesc)
                ,getResources().getString(R.string.deadliftURL)));

        exerciseList.add(new Exercise(getResources().getString(R.string.squats)
                ,getResources().getString(R.string.squatsDesc)
                ,getResources().getString(R.string.squatsURL)));

        exerciseList.add(new Exercise(getResources().getString(R.string.benchpress)
                ,getResources().getString(R.string.benchpressDesc)
                ,getResources().getString(R.string.benchpressURL)));

        exerciseList.add(new Exercise(getResources().getString(R.string.ohp)
                ,getResources().getString(R.string.ohpDesc)
                ,getResources().getString(R.string.ohpURL)));
    }
}