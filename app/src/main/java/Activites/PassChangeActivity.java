package Activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class PassChangeActivity extends AppCompatActivity {
    private EditText oldPass;
    private EditText newPass;
    private EditText newPassConf;
    private Button confirmButt;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_change);
        oldPass = (EditText)findViewById(R.id.OldPass);
        newPass = (EditText)findViewById(R.id.newPass);
        newPassConf = (EditText)findViewById(R.id.newPassConf);
        confirmButt = (Button)findViewById(R.id.confPassButt);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        confirmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Aktualizuje Hasło");
                mProgress.show();
                if(!TextUtils.isEmpty(oldPass.getText().toString())
                && !TextUtils.isEmpty(newPass.getText().toString())
                && !TextUtils.isEmpty(newPassConf.getText().toString())){

                    if(newPass.getText().toString().equals(newPassConf.getText().toString())){
                        if(newPass.getText().toString().length()>=6)
                        {
                        //TODO Zmiana hasła
                        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(),oldPass.getText().toString());
                        mAuth.getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mAuth.getCurrentUser().updatePassword(newPass.getText().toString());
                                Toast.makeText(PassChangeActivity.this,"Zaktualizowano hasło",Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                                finishActivity(1);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PassChangeActivity.this,"Stare hasło nie jest poprawne",Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        });}else{
                            Toast.makeText(PassChangeActivity.this,"Podane hasło jest za krótkie (min 6 znaków)",Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    }
                    else{
                        Toast.makeText(PassChangeActivity.this,"Hasła nie są identyczne",Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }

                }
                else{
                    Toast.makeText(PassChangeActivity.this,"Żadne z pól nie może być puste",Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }
        });
    }
}
