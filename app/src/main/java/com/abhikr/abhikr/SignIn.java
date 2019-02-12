package com.abhikr.abhikr;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import dmax.dialog.SpotsDialog;

public class SignIn extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup,forgotpass;
        View sn;
    private static final String TAG = "Abhikr";
    private static final int RC_SIGN_IN = 9;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    /* firebase auth object */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //progress dialog
    private ProgressDialog progressDialog;
    BaseActivity ba;
    AlertDialog spotsdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.PartyTheme);
        setContentView(R.layout.activity_sign_in);
        spotsdialog=new SpotsDialog(SignIn.this, R.style.abhi);
        //getting firebase auth object
        mAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser() != null){
                    //close this activity
//            finish();
//            //opening profile activity
//            startActivity(new Intent(getApplicationContext(), MainActivity_old.class));
                    Intent i = new Intent(SignIn.this, SampleActivity.class);

                    // Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Staring Login Activity
                    startActivity(i);
                    finish();
                    spotsdialog.dismiss();

                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY
                , android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.CHANGE_NETWORK_STATE, android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.GET_ACCOUNTS, android.Manifest.permission.SYSTEM_ALERT_WINDOW};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        signInButton = (SignInButton) findViewById(R.id.gp);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);
        forgotpass= (TextView) findViewById(R.id.textViewforgotpass);
        sn=findViewById(R.id.fab);
        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        forgotpass.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                spotsdialog.show();
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    //method for user login
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            editTextEmail.setError("Please enter email");
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            editTextEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("please enter password");
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            editTextPassword.requestFocus();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog=new ProgressDialog(SignIn.this, R.style.MyTheme);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        progressDialog.show();
        //progressDialog.setMessage("Authenticating...");
//        progressDialog.setIndeterminate(true);
//        progressDialog.setContentView(R.layout.activity_google_sign_in);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ba.showProgressDialog();

        //logging in the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //ba.hideProgressDialog();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), SampleActivity.class));
                            spotsdialog.show();
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar
                                    .make(sn, "Email/Password Incorrect ! try again", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

                            snackbar.show();
                           /* Snackbar.make(sn, "Internet Connection not available ", Snackbar.LENGTH_LONG)
                                    .setAction("Plz Connect to Internet", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();*/
                            //Toast.makeText(SignIn.this, "Email/Password Incorrect ! try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
        if(view== forgotpass)
        {
            finish();
            startActivity(new Intent(this, ResetPass.class));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
// An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        //Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
        Toast toast=Toast.makeText(getApplicationContext(), "Google play services error...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 330);
        toast.show();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}