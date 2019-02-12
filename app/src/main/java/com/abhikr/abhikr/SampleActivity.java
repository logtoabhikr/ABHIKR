package com.abhikr.abhikr;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.abhikr.abhikr.fragment.Address_location;
import com.abhikr.abhikr.fragment.Chat_MainFrag;
import com.abhikr.abhikr.fragment.HomeFragment;
import com.abhikr.abhikr.fragment.Product_loadbar;
import com.abhikr.abhikr.fragment.Sms_Verify;
import com.abhikr.abhikr.fragment.gerg_explist;
import com.abhikr.abhikr.menu.DrawerAdapter;
import com.abhikr.abhikr.menu.DrawerItem;
import com.abhikr.abhikr.menu.SimpleItem;
import com.abhikr.abhikr.menu.SpaceItem;
import com.abhikr.abhikr.ui.LoginActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class SampleActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ADDRESS = 1;
    private static final int POS_FASHION = 2;
    private static final int POS_ACCESSORIES = 3;
    private static final int POS_ELECTRONIC=4;
    private static final int POS_CART = 5;
    private static final int POS_LOGOUT=7;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private static final String TAG = "PhoneAuthActivity";
    //private FirebaseAuth mAuth;
    //firebase auth object
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
       final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            //getSupportActionBar().setTitle(R.string.app_name);
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);*/
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if the user is not logged in
                //that means current user will return null
                if(firebaseAuth.getCurrentUser() == null){
//            //closing this activity
//            finish();
//            //starting login activity
//            startActivity(new Intent(this, SignIn.class));
                    // After logout redirect user to Login Activity
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);

                    /*// Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

                    // Staring Login Activity
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(i,ActivityOptions.makeSceneTransitionAnimation(SampleActivity.this).toBundle());
                    }
                    finish();
                }
                else
                {
                    Log.d(TAG,"Signout:User");
                }
            }
        };


        //getting current user
//        user = mAuth.getCurrentUser();
         getSupportActionBar().setTitle("Welcome "+user.getEmail());
        MobileAds.initialize(this, getString(R.string.YOUR_ADMOB_APP_ID));
        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)// if true than automatically nav open show on runtime
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
/*.withMenuOpened(true) //Initial menu opened/closed state. Default == false
                .withMenuLocked(false) //If true, a user can't open or close the menu. Default == false.
                .withGravity(SlideGravity.LEFT) //If LEFT you can swipe a menu from left to right, if RIGHT - the direction is opposite.
                .withSavedState(savedInstanceState) //If you call the method, layout will restore its opened/closed state
                .inject();*/
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ADDRESS),
                createItemFor(POS_FASHION),
                createItemFor(POS_ACCESSORIES),
                createItemFor(POS_ELECTRONIC),
                createItemFor(POS_CART),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }

    @Override
    public void onItemSelected(int position) {
        if(position == POS_DASHBOARD)
        {
            //getSupportActionBar().setTitle(screenTitles[POS_DASHBOARD]);
            Chat_MainFrag homeFragment=new Chat_MainFrag();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,homeFragment,"abhi").commit();
            getSupportActionBar().setTitle(screenTitles[POS_DASHBOARD]);
        }
        if(position==POS_ADDRESS)
        {
            //startActivity(new Intent(SampleActivity.this,Main2Activity.class));
            Address_location fragment1=new Address_location();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fragment1,"abhi").commit();
            getSupportActionBar().setTitle(screenTitles[POS_ADDRESS]);

        }

        if(position==POS_FASHION)
        {
            //startActivity(new Intent(SampleActivity.this,Main2Activity.class));
            gerg_explist fragment=new gerg_explist();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fragment,"abhi").commit();
            getSupportActionBar().setTitle(screenTitles[POS_FASHION]);// for set titles
//            new SlidingRootNavBuilder(this)
//                    .wi
        }

        if(position==POS_ACCESSORIES)
        {
            Product_loadbar fram=new Product_loadbar();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fram,"abhi").commit();
            getSupportActionBar().setTitle(screenTitles[POS_ACCESSORIES]);
        }
        if (position == POS_ELECTRONIC) {
            Sms_Verify frag=new Sms_Verify();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,frag,"abhi").commit();
            getSupportActionBar().setTitle(screenTitles[POS_ELECTRONIC]);
        }
        if (position == POS_CART) {
            getSupportActionBar().setTitle(screenTitles[POS_CART]);
        }
        if (position == POS_LOGOUT) {
            //mAuth.signOut();
            FirebaseAuth.getInstance().signOut();

            //starting login activity
            //startActivity(new Intent(this, SignIn.class));
            //closing activity
            //finish();


        }
        //Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        //showFragment(selectedScreen);
        // above code is for set middle of text .

    }


    private void showFragment(Fragment fragment) {

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        // change icon to arrow drawable1
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable1.ic_arrow_back_black_24dp);

    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.hom)
        {    //FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //mAuth.signOut();
            //startActivity(new Intent(SampleActivity.this,SampleActivity.class));
            Intent a=new Intent(getApplicationContext(),EXP.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(a, ActivityOptions.makeSceneTransitionAnimation(SampleActivity.this).toBundle());
            }
        }
        if(id==R.id.user)
        {
            //setTitle(user.getEmail());
            getSupportActionBar().setTitle(user.getEmail());
        }
        if(id==R.id.logout)
        {            //logging out the user
            //mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            //closing activity
            //finish();
            //starting login activity
        }
        if(id==R.id.about_home)
        {
            Toast.makeText(this, "Abhichat version 1.0", Toast.LENGTH_LONG).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); removing for not showing error solution
        AlertDialog.Builder ald=new AlertDialog.Builder(SampleActivity.this);

        ald.setTitle("ABHIKR SAYS").setMessage("are you sure You want to exit").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SampleActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();
            }
        });
        ald.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SampleActivity.this, "Thanks for Visiting", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        AlertDialog alertDialog = ald.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuth!=null)
        {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


}

