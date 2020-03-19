package shafi.sbf.com.foodjinni.register;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import shafi.sbf.com.foodjinni.LocationActivity;
import shafi.sbf.com.foodjinni.MainActivity;
import shafi.sbf.com.foodjinni.MapsActivity;
import shafi.sbf.com.foodjinni.R;


public class LogIn extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email,input_password;
    TextView btnSignup,btnForgotPass;
    LinearLayout login_activity;
    String emailm,passwordm;

    private FirebaseAuth auth;

    private ProgressDialog mProgress;

    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (!checkPermissionGranted()) {
            askForPermission();

        }

        //View
        btnLogin = (Button)findViewById(R.id.login_btn_login);
        input_email = (EditText)findViewById(R.id.login_email);
        input_password = (EditText)findViewById(R.id.login_password);
        btnSignup = (TextView)findViewById(R.id.login_btn_signup);
        btnForgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
        login_activity= (LinearLayout) findViewById(R.id.login_activity);

        emailm = input_email.getText().toString();
        passwordm = input_password.getText().toString();

        btnSignup.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        mProgress = new ProgressDialog(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check already session , if ok-> DashBoard
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LogIn.this, LocationActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_forgot_password)
        {
            startActivity(new Intent(LogIn.this,ForgotPassword.class));

        }
        else if(view.getId() == R.id.login_btn_signup)
        {
            startActivity(new Intent(LogIn.this,SignUp.class));

        }
        else if(view.getId() == R.id.login_btn_login)
        {
                mProgress.setMessage("Loading....");
                mProgress.show();
                loginUser(input_email.getText().toString(), input_password.getText().toString());
        }

    }

    private void loginUser(final String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if(password.length() < 6)
                            {
                                Snackbar snackBar = Snackbar.make(login_activity,"Password length must be over 6",Snackbar.LENGTH_SHORT);
                                snackBar.show();
                                mProgress.dismiss();
                            }
                        }
                        else{
                            mProgress.dismiss();
                            Intent intent = new Intent(LogIn.this, LocationActivity.class);
                           // startActivity(new Intent(LogIn.this,Home.class));
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        finish();
                    }
                });
    }



    //**************** Shafi -> Runtime Permission Check ****************//
    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermissionGranted()) {
            askForPermission();
        }
        if (!checkInternetConnection()) {

            Toast.makeText(this,"please check internet connection",Toast.LENGTH_SHORT).show();
        } else {


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED
                        /*&& grantResults[5] == PackageManager.PERMISSION_GRANTED
                        && grantResults[6] == PackageManager.PERMISSION_GRANTED
                        && grantResults[7] == PackageManager.PERMISSION_GRANTED*/
                ) {

                    return;

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private Boolean checkInternetConnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

    private boolean checkPermissionGranted() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

       /* if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }*/

/*        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void askForPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CALL_PHONE,
                        /*Manifest.permission.CAMERA,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE,
                      Manifest.permission.READ_EXTERNAL_STORAGE,*/
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                },
                MY_PERMISSIONS_REQUEST);
    }

    //************************Shafi -> Runtime Permission Check ******************//

    // Check The Edit Fields Are Empty Or Not
//    private boolean checkIsEmpty(){
//        if (emailm.isEmpty()){
//            Snackbar snackBar = Snackbar.make(login_activity,"Email is mandatory",Snackbar.LENGTH_SHORT);
//            snackBar.show();return false;
//        }else if (passwordm.isEmpty()){
//            Snackbar snackBar = Snackbar.make(login_activity,"Password is mandatory",Snackbar.LENGTH_SHORT);
//            snackBar.show();return false;
//        }else if (emailm.isEmpty()& passwordm.isEmpty()){
//            Snackbar snackBar = Snackbar.make(login_activity,"Email & Password is mandatory",Snackbar.LENGTH_SHORT);
//            snackBar.show();return false;
//        }else return true;
//    }
}
