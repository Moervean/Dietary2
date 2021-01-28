package Data;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.dietary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import Dagger.AppModule;
import Dagger.Consts;
import Dagger.DaggerConstsComponent;
import Dagger.DaggerDaggerConstsComponent;
import Model.Workout;

public class AddWorkoutDialog extends DialogFragment {
    private ConstraintLayout constraintLayout;
    private int width;
    private int height;
    private String path;
    private FirebaseDatabase mDatabase;
    private Button saveWorkout;
    private EditText workoutDesc;
    private String desc;
    private String[] paths;
    @Inject
    Consts consts;

    public AddWorkoutDialog(String path) {
        this.path=path;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        DaggerConstsComponent daggerConstsComponent = DaggerDaggerConstsComponent
                .builder()
                .appModule(new AppModule())
                .build();
        daggerConstsComponent.inject(this);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_workout_dialog,null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        constraintLayout = view.findViewById(R.id.workoutDialogLayout);
        constraintLayout.setMinHeight(height);
        constraintLayout.setMinWidth(width);
        mDatabase = FirebaseDatabase.getInstance();
        saveWorkout = (Button)view.findViewById(R.id.protegeUploadWorkout);
        workoutDesc = (EditText) view.findViewById(R.id.protegeWorkoutDescription);
        saveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desc = workoutDesc.getText().toString();
                if(!TextUtils.isEmpty(desc))
                SaveToDatabase();
                else
                    Toast.makeText(getContext(),getString(R.string.blankWorkoutError),Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        paths = path.split("/");

        return builder.create();

    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(width,height);
    }
    private void SaveToDatabase(){


        mDatabase.getReference().child(paths[0]).child(paths[1]).child(paths[2]).child(paths[3]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Workout workout = new Workout(desc,"");
                snapshot.getRef().child(consts.workout + snapshot.getChildrenCount()).setValue(workout);
                workoutDesc.setText("");
                Toast.makeText(getContext(),getString(R.string.addWorkout),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}