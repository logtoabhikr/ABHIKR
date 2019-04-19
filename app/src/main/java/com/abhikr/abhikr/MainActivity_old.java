package com.abhikr.abhikr;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import dmax.dialog.SpotsDialog;

public class MainActivity_old extends AppCompatActivity {
    WebView abhi;
    ProgressDialog pg;
    FirebaseUser user;
    AlertDialog spotsdialog;
    private static final String TAG = "PhoneAuthActivity";
    //private FirebaseAuth mAuth;
    //firebase auth object
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
        spotsdialog=new SpotsDialog.Builder()
                .setContext(getApplicationContext())
                .setTheme(R.style.abhi)
                .build();
        //initializing firebase authentication object
        mAuth = FirebaseAuth.getInstance();
         mAuthStateListener=new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //if the user is not logged in
        //that means current user will return null
        if(mAuth.getCurrentUser() == null){
//            //closing this activity
//            finish();
//            //starting login activity
//            startActivity(new Intent(this, SignIn.class));
            // After logout redirect user to Login Activity
            Intent i = new Intent(MainActivity_old.this, SignIn.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            startActivity(i);
            finish();
        }
    }
};


        //getting current user
        user = mAuth.getCurrentUser();
        setTitle("Welcome "+user.getEmail());
//        mAuth = FirebaseAuth.getInstance();
//        if(mAuth!=null){
//            setTitle(mAuth.getCurrentUser().getPhoneNumber());
//        }
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        abhi = (WebView) findViewById(R.id.webview);
        pg=new ProgressDialog(MainActivity_old.this);
        //pg.setMessage("Loading ...");
        abhi.getSettings().setJavaScriptEnabled(true);
        WebSettings ak=abhi.getSettings();
        ak.setBuiltInZoomControls(false);
        ak.setLoadsImagesAutomatically(true);
        abhi.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        abhi.loadUrl("http://www.abhikr.com/");
        Toast.makeText(this, "welcome to abhishek kumar personal profile - ", Toast.LENGTH_SHORT).show();
        abhi.setWebViewClient(new abhikr());
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
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
    public class abhikr extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //pg.show();
            if(Appstatus.getInstance(MainActivity_old.this).isOnline())
            {
                Toast.makeText(MainActivity_old.this, "Nice !", Toast.LENGTH_SHORT).show();
                spotsdialog.show();
                //new SpotsDialog(MainActivity_old.this).show();
                /*if(pg.isShowing())
                {
                    pg.show();
                }*/
            }
            else
            {
                pg.setMessage("Connect with Internet to hide this window !!!! Loading cache file ");
                pg.show();
                Toast.makeText(MainActivity_old.this, "Connect with internet to hide dialog message", Toast.LENGTH_SHORT).show();

            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
                if (spotsdialog.isShowing())
                {
                    spotsdialog.dismiss();
                    spotsdialog=null;
                }

//            new SpotsDialog(MainActivity_old.this).dismiss();
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.user)
        {
            setTitle(user.getEmail());
        }
        if(id==R.id.logout)
        {            //logging out the user
            mAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, SignIn.class));
        }
        if(id==R.id.hom)
        {            //FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //mAuth.signOut();
            startActivity(new Intent(MainActivity_old.this,EXP.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed(); removing for not showing error solution
        AlertDialog.Builder ald=new AlertDialog.Builder(MainActivity_old.this);

        ald.setTitle("ABHIKR SAYS").setMessage("are you sure You want to exit").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity_old.this, "Welcome back", Toast.LENGTH_SHORT).show();
            }
        });
        ald.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity_old.this, "Thanks for Visiting", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        AlertDialog alertDialog = ald.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}
