package com.abhikr.abhikr.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abhikr.abhikr.Appstatus;
import com.abhikr.abhikr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {
    WebView abhi;
    ProgressDialog pg;
    FirebaseUser user;
    AlertDialog spotsdialog;
    private static final String TAG = "HomeFragmentActivity";
    //private FirebaseAuth mAuth;
    //firebase auth object
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View abhiview=inflater.inflate(R.layout.fragment_home, null);
        spotsdialog=new SpotsDialog.Builder()
                .setContext(getActivity())
                .setTheme(R.style.abhi)
                .build();
        //initializing firebase authentication object
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if the user is not logged in
                //that means current user will return null
                if(firebaseAuth.getCurrentUser() == null){
                    /*Intent i = new Intent(getContext(), SignIn.class);

                    // Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Staring Login Activity
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(i,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }*/
                    Toast.makeText(getActivity(), "User SigendIn", Toast.LENGTH_SHORT).show();



                    //finish();
                }
            }
        };


        //getting current user
        user = mAuth.getCurrentUser();
        //setTitle("Welcome "+user.getEmail());
//        mAuth = FirebaseAuth.getInstance();
//        if(mAuth!=null){
//            setTitle(mAuth.getCurrentUser().getPhoneNumber());
//        }
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE};

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        abhi = (WebView) abhiview.findViewById(R.id.webview);
        mSwipeRefreshLayout=abhiview.findViewById(R.id.swiperefresh);
        pg=new ProgressDialog(getActivity());
        //pg.setMessage("Loading ...");
        abhi.getSettings().setJavaScriptEnabled(true);
        WebSettings ak=abhi.getSettings();
        ak.setBuiltInZoomControls(false);
        ak.setLoadsImagesAutomatically(true);
        abhi.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        abhi.loadUrl("http://www.abhikr.com/");
        Toast.makeText(getActivity(), "welcome to abhishek kumar personal profile - ", Toast.LENGTH_SHORT).show();
        abhi.setWebViewClient(new abhikr());// not to make client with class.clientnme
        /*mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                R.color.colorPrimaryDark);*/
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_colors));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                abhi.loadUrl("http://www.abhikr.com/");
            }
        });
        return abhiview;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuth!=null)
       mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStart() {
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
            if(!Appstatus.getInstance(requireActivity()).isOnline())
            {
                pg.setMessage("Connect with Internet to hide this window !!!! Loading cache file ");
                pg.show();
                //spotsdialog.show();
                //new SpotsDialog(MainActivity_old.this).show();
                /*if(pg.isShowing())
                {
                    pg.show();
                }*/
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
            mSwipeRefreshLayout.setRefreshing(false);
           /* if (spotsdialog.isShowing())
            {
                spotsdialog.dismiss();
                spotsdialog=null;
            }*/

//            new SpotsDialog(MainActivity_old.this).dismiss();
            super.onPageFinished(view, url);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.user)
        {
            //setTitle(user.getEmail());
        }
        if(id==R.id.logout)
        {            //logging out the user
            mAuth.signOut();
            //closing activity
            //finish();
            //starting login activity
            startActivity(new Intent(getActivity(), HomeFragment.this));
        }
        if(id==R.id.phone)
        {    //FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //mAuth.signOut();
            startActivity(new Intent(getActivity(),Home.class));
        }

        return super.onOptionsItemSelected(item);
    }*/

/*    @Override
    public void onBackPressed() {
        // super.onBackPressed(); removing for not showing error solution
        AlertDialog.Builder ald=new AlertDialog.Builder(getActivity());

        ald.setTitle("ABHIKR SAYS").setMessage("are you sure You want to exit").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Welcome back", Toast.LENGTH_SHORT).show();
            }
        });
        ald.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Thanks for Visiting", Toast.LENGTH_SHORT).show();
                //finish();

            }
        });
        AlertDialog alertDialog = ald.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }*/

}

