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
import Model.Diet;

public class AddMealDialog extends DialogFragment {
    private ConstraintLayout constraintLayout;
    private int width;
    private int height;
    private String path;
    private String[] paths;
    private FirebaseDatabase mDatabase;
    private EditText mealDescription;
    private Button uploadMeal;
    private String desc;
    @Inject
    Consts consts;

    public AddMealDialog(String path) {
        this.path = path;
        paths = path.split("/");
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
        View view = inflater.inflate(R.layout.add_diet_dialog,null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        mDatabase = FirebaseDatabase.getInstance();
        constraintLayout = view.findViewById(R.id.dietDialogLayout);
        constraintLayout.setMinHeight(height);
        constraintLayout.setMinWidth(width);
        mealDescription = (EditText)view.findViewById(R.id.mealToProtegeDescription);
        uploadMeal = (Button)view.findViewById(R.id.uploadMealToProtege);
        uploadMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desc = mealDescription.getText().toString();
                if(!TextUtils.isEmpty(desc)) {
                    SaveToDatabase();

                }
                else
                    Toast.makeText(getContext(),getString(R.string.blankDietError),Toast.LENGTH_SHORT).show();

            }
        });
        builder.setView(view);

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
                Diet diet = new Diet(desc,"","");
                snapshot.getRef().child(consts.dinner  + snapshot.getChildrenCount()).setValue(diet);
                mealDescription.setText("");
                Toast.makeText(getContext(),getString(R.string.meal_added),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
