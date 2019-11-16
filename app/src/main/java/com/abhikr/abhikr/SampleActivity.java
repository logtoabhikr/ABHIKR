package com.abhikr.abhikr;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abhikr.abhikr.fragment.Chat_MainFrag;
import com.abhikr.abhikr.fragment.Product_loadbar;
import com.abhikr.abhikr.fragment.Sms_Verify;
import com.abhikr.abhikr.menu.DrawerAdapter;
import com.abhikr.abhikr.menu.DrawerItem;
import com.abhikr.abhikr.menu.SimpleItem;
import com.abhikr.abhikr.menu.SpaceItem;
import com.abhikr.abhikr.ui.FriendsFragment;
import com.abhikr.abhikr.ui.GroupFragment;
import com.abhikr.abhikr.ui.LoginActivity;
import com.abhikr.abhikr.ui.UserProfileFragment;
import com.amazonaws.amplify.generated.graphql.CreateTodoMutation;
import com.amazonaws.amplify.generated.graphql.ListTodosQuery;
import com.amazonaws.amplify.generated.graphql.OnCreateTodoSubscription;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nonnull;

import type.CreateTodoInput;

public class SampleActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_FRIENDS = 1;
    private static final int POS_GROUP = 2;
    private static final int POS_PROFILE = 3;
    private static final int POS_PUSHFCM =4;
    private static final int POS_SHARE = 5;
    private static final int POS_LOGOUT=7;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private static final String TAG = "PhoneAuthActivity";
    //private FirebaseAuth mAuth;
    //firebase auth object
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AWSAppSyncClient mAWSAppSyncClient;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
       final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            //getSupportActionBar().setTitle(R.string.app_name);
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);*/
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        //user=FirebaseAuth.getInstance().getCurrentUser();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if the user is not logged in
                //that means current user will return null
                if(firebaseAuth.getCurrentUser() == null){
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    /*// Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

                    // Staring Login Activity
                        startActivity(i,ActivityOptions.makeSceneTransitionAnimation(SampleActivity.this).toBundle());
                    supportFinishAfterTransition();
                }
            }
        };


        //getting current user
     user = mAuth.getCurrentUser();
     if(user!=null)
     {
         // Obtain the FirebaseAnalytics instance.
         //mFirebaseAnalytics = FirebaseAnalytics.getInstance(SampleActivity.this);
         getSupportActionBar().setTitle("Welcome "+user.getEmail());
         MobileAds.initialize(this, getString(R.string.YOUR_ADMOB_APP_ID));
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
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_FRIENDS),
                createItemFor(POS_GROUP),
                createItemFor(POS_PROFILE),
                createItemFor(POS_PUSHFCM),
                createItemFor(POS_SHARE),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list =  findViewById(R.id.list);
        list.setNestedScrollingEnabled(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        //runMutation(); runQuery(); subscribe();// running aws app amplify single wala query and subscribe
    }
    public void runMutation(){
        CreateTodoInput createTodoInput = CreateTodoInput.builder().
                name("Use AppSync").
                description("Realtime and Offline").
                build();

        mAWSAppSyncClient.mutate(CreateTodoMutation.builder().input(createTodoInput).build())
                .enqueue(mutationCallback);
    }

    private GraphQLCall.Callback<CreateTodoMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateTodoMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTodoMutation.Data> response) {
            Log.i("Results", "Added Todo");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
    };
    public void runQuery(){
        mAWSAppSyncClient.query(ListTodosQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(todosCallback);
    }

    private GraphQLCall.Callback<ListTodosQuery.Data> todosCallback = new GraphQLCall.Callback<ListTodosQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTodosQuery.Data> response) {
            Log.i("Results", response.data().listTodos().items().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };
    private AppSyncSubscriptionCall subscriptionWatcher;

    private void subscribe(){
        OnCreateTodoSubscription subscription = OnCreateTodoSubscription.builder().build();
        subscriptionWatcher = mAWSAppSyncClient.subscribe(subscription);
        subscriptionWatcher.execute(subCallback);
    }

    private AppSyncSubscriptionCall.Callback subCallback = new AppSyncSubscriptionCall.Callback() {
        @Override
        public void onResponse(@Nonnull Response response) {
            Log.i("Response", response.data().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }

        @Override
        public void onCompleted() {
            Log.i("Completed", "Subscription completed");
        }
    };
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
        if(position== POS_FRIENDS)
        {
            FriendsFragment fragment1=new FriendsFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fragment1,"FRIEND").commit();
            getSupportActionBar().setTitle(screenTitles[POS_FRIENDS]);

        }
        if(position== POS_PROFILE)
        {
            //startActivity(new Intent(SampleActivity.this,Main2Activity.class));
            //Address_location fragment1=new Address_location();
            UserProfileFragment userProfileFragment=new UserProfileFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,userProfileFragment,"PROFILE").commit();
            getSupportActionBar().setTitle(screenTitles[POS_PROFILE]);
        }

        if(position== POS_GROUP)
        {
            //startActivity(new Intent(SampleActivity.this,Main2Activity.class));
            //gerg_explist fragment=new gerg_explist();
            GroupFragment fragment=new GroupFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fragment,"GROUP").commit();
            getSupportActionBar().setTitle(screenTitles[POS_GROUP]);// for set titles
//            new SlidingRootNavBuilder(this)
//                    .wi
        }
        if (position == POS_PUSHFCM) {
            Product_loadbar fram=new Product_loadbar();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,fram,"abhi").commit();
            getSupportActionBar().setTitle(screenTitles[POS_PUSHFCM]);
        }
        if (position == POS_SHARE) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hi, This is abhishek Kumar. I just created a app for secure personal/family chat application with some unique featur.Hope i will live soon! ";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AbhiKr Coming Soon");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
           /* Sms_Verify frag=new Sms_Verify();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container,frag,"SMS").commit();*/
            getSupportActionBar().setTitle(screenTitles[POS_SHARE]);
        }
        if (position == POS_LOGOUT) {
            //mAuth.signOut();
            ABHI_LOGOUT();
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
            Intent a=new Intent(getApplicationContext(),MainActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(a, ActivityOptions.makeSceneTransitionAnimation(SampleActivity.this).toBundle());
            }
        }
        if(id==R.id.user)
        {
            //setTitle(user.getEmail());
            /*if(user!=null)
            getSupportActionBar().setTitle(user.getEmail());*/
            Intent a=new Intent(getApplicationContext(),EXP.class);
                startActivity(a, ActivityOptions.makeSceneTransitionAnimation(SampleActivity.this).toBundle());
        }
        if(id==R.id.logout)
        {            //logging out the user
            //mAuth.signOut();
           ABHI_LOGOUT();

        }
        if(id==R.id.about_home)
        {
            Intent aaa=new Intent(getApplicationContext(),Phone.class);
            startActivity(aaa, ActivityOptions.makeSceneTransitionAnimation(SampleActivity.this).toBundle());
            Toast.makeText(this, "AbhiKr version 1.3", Toast.LENGTH_LONG).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void ABHI_LOGOUT() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_notify_group).setTitle("Abhikr Says : ").setMessage("Are you sure want to logout.")
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //FirebaseAuth.getInstance().signOut();
                        mAuth.signOut();
                        //closing activity
                        //finish();
                        //starting login activity
                    }
                }).setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SampleActivity.this, "Welcome back : "+user.getEmail(), Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); removing for not showing error solution
        MaterialAlertDialogBuilder ald=new MaterialAlertDialogBuilder(SampleActivity.this);

        ald.setIcon(R.mipmap.ic_launcher).setTitle("ABHIKR SAYS").setMessage("Are you sure You want to exit").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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
        }).setCancelable(true).show();

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

