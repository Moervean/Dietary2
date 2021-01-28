package Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dietary.R;

import Data.InfoDialog;

public class InformationActivity extends AppCompatActivity {
    private Button macrosInfo;
    private Button dietInfo;
    private Button execisesInfo;
    private Button workoutInfo;
    private InfoDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        macrosInfo = (Button) findViewById(R.id.macrosInfo);
        dietInfo = (Button) findViewById(R.id.dietInfo);
        execisesInfo  = (Button) findViewById(R.id.exercisesInfo);
        workoutInfo = (Button) findViewById(R.id.workoutInfo);
        infoDialog = new InfoDialog();

        macrosInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.settext(getString(R.string.macrosInfo));
                infoDialog.show(getSupportFragmentManager(),getString(R.string.macros));

            }
        });
        dietInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.settext(getString(R.string.dietInfo));
                infoDialog.show(getSupportFragmentManager(),getString(R.string.diet_basics));
            }
        });
        execisesInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InformationActivity.this,ExercisesInfoActivity.class));
            }
        });
        workoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.settext(getString(R.string.workoutInfo));
                infoDialog.show(getSupportFragmentManager(),getString(R.string.workoutBasics));
            }
        });


    }
}