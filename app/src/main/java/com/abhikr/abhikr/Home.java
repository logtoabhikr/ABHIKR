package com.abhikr.abhikr;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhikr.abhikr.data.FriendDB;
import com.abhikr.abhikr.data.GroupDB;
import com.abhikr.abhikr.firepush.NotificationBase;
import com.abhikr.abhikr.firepush.NotificationVO;
import com.abhikr.abhikr.fragment.AbhiKrFragment;
import com.abhikr.abhikr.fragment.Chat_MainFrag;
import com.abhikr.abhikr.menu.DrawerAdapter;
import com.abhikr.abhikr.menu.DrawerItem;
import com.abhikr.abhikr.menu.SimpleItem;
import com.abhikr.abhikr.menu.SpaceItem;
import com.abhikr.abhikr.projects.WorkStation;
import com.abhikr.abhikr.service.AppCrash;
import com.abhikr.abhikr.service.ServiceUtils;
import com.abhikr.abhikr.ui.FriendsFragment;
import com.abhikr.abhikr.ui.GroupFragment;
import com.abhikr.abhikr.ui.LoginActivity;
import com.abhikr.abhikr.ui.UserProfileFragment;
import com.abhikr.tictactoe.TicTacToeHome;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.Objects;

public class Home extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_ABHIKR=0;
    private static final int POS_DASHBOARD = 1;
    private static final int POS_FRIENDS = 2;
    private static final int POS_GROUP = 3;
    private static final int POS_PROFILE = 4;
    private static final int POS_WORKSTATION =5;
    private static final int POS_SHARE = 6;
    private static final int POS_LOGOUT=8;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private static final String TAG = Home.class.getSimpleName();
    //private FirebaseAuth mAuth;
    //firebase auth object
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    int counter=0;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        FirebaseAppIndex.getInstance().update(new Indexable.Builder().setName(getString(R.string.app_name)).setUrl("https://www.abhikr.com/").build());
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (AppCrash.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_sample);
        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setTitle(R.string.app_name);
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);*/
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        //user=FirebaseAuth.getInstance().getCurrentUser();
        mAuthStateListener = firebaseAuth -> {
            //if the user is not logged in
            //that means current user will return null
            if (firebaseAuth.getCurrentUser() == null) {
                Intent i = new Intent(Home.this, LoginActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
                supportFinishAfterTransition();
            }
        };
        //getting current user
        user = mAuth.getCurrentUser();
        if (user != null) {
            // Obtain the FirebaseAnalytics instance.
            //mFirebaseAnalytics = FirebaseAnalytics.getInstance(Home.this);
            getSupportActionBar().setTitle("Welcome " + user.getEmail());
            //MobileAds.initialize(this, getString(R.string.YOUR_ADMOB_APP_ID));
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            try {
                    //Sets the user ID property.
                    mFirebaseAnalytics.setUserId(user.getUid());
                    //Sets a user property to a given value.
                    mFirebaseAnalytics.setUserProperty("ABHIKRHome", user.getUid());
            /*trace = FirebasePerformance.getInstance().newTrace("DCT_Home");
            trace.start();Integer.parseInt(user.getUid())*/
                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 89);
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, user.getDisplayName());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    //Logs an app event.
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

                    //Sets whether analytics collection is enabled for this app on this device.
                    mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

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
                createItemFor(POS_ABHIKR).setChecked(true),
                createItemFor(POS_DASHBOARD),
                createItemFor(POS_FRIENDS),
                createItemFor(POS_GROUP),
                createItemFor(POS_PROFILE),
                createItemFor(POS_WORKSTATION),
                createItemFor(POS_SHARE),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_ABHIKR);
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
    public Action getIndexApiAction() {
        return Actions.newView("ABHIKR Home", "https://www.abhikr.com/");
    }

    @Override
    public void onItemSelected(int position) {
        if(position==POS_ABHIKR)
        {
            AbhiKrFragment abhiKrFragment=new AbhiKrFragment();
            ABHIKRCall(abhiKrFragment,false);
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_ABHIKR]);
           /* FragmentManager fragmentManager = getSupportFragmentManager();
            while (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStackImmediate();
            }*/
        }
        if(position == POS_DASHBOARD)
        {
            //getSupportActionBar().setTitle(screenTitles[POS_DASHBOARD]);
            Chat_MainFrag homeFragment=new Chat_MainFrag();
            ABHIKRCall(homeFragment,false);
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_DASHBOARD]);
        }
        if(position== POS_FRIENDS)
        {
            FriendsFragment fragment1=new FriendsFragment();
            ABHIKRCall(fragment1,false);
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_FRIENDS]);

        }
        if(position== POS_PROFILE)
        {
            //startActivity(new Intent(Home.this,Main2Activity.class));
            //Address_location fragment1=new Address_location();
            UserProfileFragment userProfileFragment=new UserProfileFragment();
            ABHIKRCall(userProfileFragment,false);
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_PROFILE]);
        }
        if(position== POS_GROUP)
        {
            //startActivity(new Intent(Home.this,Main2Activity.class));
            //gerg_explist fragment=new gerg_explist();
            GroupFragment fragment=new GroupFragment();
            ABHIKRCall(fragment,false);
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_GROUP]);// for set titles
//            new SlidingRootNavBuilder(this)
//                    .wi
        }
        if (position == POS_WORKSTATION) {
            startActivity(new Intent(this,WorkStation.class),ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            /*Product_loadbar fram=new Product_loadbar();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fram,"abhi").commit();*/
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_WORKSTATION]);
        }
        if (position == POS_SHARE) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hi, This is abhishek Kumar. I just created an android app to present my portfolio with add on simple text messaging with " +
                    "family & friends. i hope you will love, like and enjoy this app features - download link given below\n\n"+"https://abhikr.page.link/akma";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AbhiKr - Portfolio");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
           /* Sms_Verify frag=new Sms_Verify();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,frag,"SMS").commit();*/
            Objects.requireNonNull(getSupportActionBar()).setTitle(screenTitles[POS_SHARE]);
        }
        if (position == POS_LOGOUT) {
            //mAuth.signOut();
            ABHI_LOGOUT();
        }
        //Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        //showFragment(selectedScreen);
        // above code is for set middle of text .
        //createAndShowNotification("ABHIKR","This is my personal Portifolio to represent my work with simple chat feature...");
    }

    private void createAndShowNotification(String title,String msg) {

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(msg);
        //notificationVO.setIconUrl(""); // passing nothing to display without image
        notificationVO.setAction("activity");
        notificationVO.setActionDestination("Cashback");

        Intent resultIntent = new Intent(getApplicationContext(), Home.class);

        NotificationBase notificationUtils = new NotificationBase(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
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
                .withTextTint(color(R.color.colorPrimary))
                .withSelectedIconTint(color(R.color.colorSecondary))
                .withSelectedTextTint(color(R.color.colorSecondryVarient));
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

    public void ABHIKRCall(androidx.fragment.app.Fragment fragment, boolean isHorizontalAnim)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!(fragment instanceof AbhiKrFragment)) {
            if (!isHorizontalAnim) {
                transaction.setCustomAnimations(R.anim.simple_grow,
                        R.anim.simple_shrink, android.R.anim.fade_in,
                        android.R.anim.fade_out);
            } else {
                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out, R.anim.simple_grow,
                        R.anim.simple_shrink);
            }
        }
        transaction.replace(R.id.container, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
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
        {
         startActivity(new Intent(Home.this,MainActivity.class),
                 ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
        }
        if(id==R.id.user)
        {
            //setTitle(user.getEmail());
            /*if(user!=null)
            getSupportActionBar().setTitle(user.getEmail());*/
           /* Intent a=new Intent(getApplicationContext(),EXP.class);
                startActivity(a, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());*/
           counter++;
            //if(counter%2==0)// this will not work coz variable state loss on restart
            if(AppCrash.getInstance().isNightModeEnabled())
            {
                AppCrash.getInstance().setIsNightModeEnabled(false);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);

                Toast.makeText(this, "Night mode off: "+counter, Toast.LENGTH_SHORT).show();
            }
            else
            {
                AppCrash.getInstance().setIsNightModeEnabled(true);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                Toast.makeText(this, "Night mode ON: "+counter, Toast.LENGTH_SHORT).show();
            }
        }
        if(id==R.id.logout)
        {            //logging out the user
            //mAuth.signOut();
           ABHI_LOGOUT();

        }
        if(id==R.id.about_home)
        {
            Intent aaa=new Intent(getApplicationContext(), WorkStation.class);
            startActivity(aaa, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            //Toast.makeText(this, "AbhiKr version 1.3", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.lockdown)
        {
            Intent aaa=new Intent(getApplicationContext(), TicTacToeHome.class);
            startActivity(aaa, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ABHI_LOGOUT() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_notify_group).setTitle("Abhikr Says : ").setMessage("Are you sure want to logout.")
                .setPositiveButton(getString(android.R.string.yes), (dialog, which) -> {
                    //FirebaseAuth.getInstance().signOut();
                    mAuth.signOut();
                    FriendDB.getInstance(Home.this).dropDB();
                    GroupDB.getInstance(Home.this).dropDB();
                    ServiceUtils.stopServiceFriendChat(Home.this, true);
                }).setNegativeButton(getString(android.R.string.no), (dialog, which) -> Toast.makeText(Home.this, "Welcome back : "+user.getEmail(), Toast.LENGTH_SHORT).show()).show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            // super.onBackPressed(); removing for not showing error solution
            MaterialAlertDialogBuilder ald=new MaterialAlertDialogBuilder(Home.this);

            ald.setIcon(R.mipmap.ic_launcher).setTitle("ABHIKR SAYS").setMessage("Are you sure You want to exit").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Home.this, "Welcome back", Toast.LENGTH_SHORT).show();
                }
            });
            ald.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Home.this, "Thanks for Visiting", Toast.LENGTH_SHORT).show();
                    finishAfterTransition();

                }
            }).setCancelable(true).show();
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        if(mAuth!=null)
        {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}

