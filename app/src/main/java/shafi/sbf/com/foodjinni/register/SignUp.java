package shafi.sbf.com.foodjinni.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shafi.sbf.com.foodjinni.LocationActivity;
import shafi.sbf.com.foodjinni.MainActivity;
import shafi.sbf.com.foodjinni.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    TextView btnLogin;
    RadioGroup Gender;
    EditText input_email,input_pass,Input_name,Input_number;
    LinearLayout activity_sign_up;

    String blood,gender;
    DatabaseReference databaseDonners;

    private FirebaseAuth auth;
    private FirebaseUser user;
    Snackbar snackbar;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View

        btnLogin = (TextView)findViewById(R.id.signup_btn_login);
        input_email = (EditText)findViewById(R.id.signup_email);
        input_pass = (EditText)findViewById(R.id.signup_password);
        Input_name= (EditText) findViewById(R.id.signup_name);
        Input_number= (EditText) findViewById(R.id.signup_number);
        activity_sign_up = (LinearLayout) findViewById(R.id.activity_sign_up);

        mProgress = new ProgressDialog(this);



        databaseDonners= FirebaseDatabase.getInstance().getReference("User");



        Gender= (RadioGroup) findViewById(R.id.gender);

        Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                if(rb.getText().toString().equals("Male")){
                    gender=rb.getText().toString();
                }
                else {
                    gender="Female";
                }
            }

        });


        btnLogin.setOnClickListener(this);


        //Init Firebase
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signup_btn_login){
            startActivity(new Intent(SignUp.this,LogIn.class));
            finish();
        }
    }

    public void singup(View view) {
        mProgress.setMessage("Loading....");
        mProgress.show();
        final String email=input_email.getText().toString();
        final String password=input_pass.getText().toString();
        final String name=Input_name.getText().toString();
        final String number=Input_number.getText().toString();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful())
                        {
                            if(password.length() < 6)
                            {
                                mProgress.dismiss();
                                Snackbar snackBar = Snackbar.make(activity_sign_up,"Password length must be over 6",Snackbar.LENGTH_SHORT);
                                snackBar.show();

                            }else {
                                mProgress.dismiss();
                                snackbar = Snackbar.make(activity_sign_up, "Error: " + task.getException(), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                        else{
                            user = auth.getCurrentUser();
                            String id = user.getUid();

                            User user=new User(id,name,number,email,password,gender);
                            databaseDonners.child(id).child("details").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgress.dismiss();
                                    Toast.makeText(SignUp.this, ""+FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

                                    Toast.makeText(SignUp.this, "Register success!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUp.this, LocationActivity.class));
                                    finish();
                                }
                            });

                        }
                    }
                });

    }
}
