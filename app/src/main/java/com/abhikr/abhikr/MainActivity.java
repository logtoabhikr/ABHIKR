package com.abhikr.abhikr;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.abhikr.abhikr.data.StaticConfig;
import com.abhikr.abhikr.projects.WorkStation;
import com.abhikr.abhikr.service.ServiceUtils;
import com.abhikr.abhikr.ui.FriendsFragment;
import com.abhikr.abhikr.ui.GroupFragment;
import com.abhikr.abhikr.ui.LoginActivity;
import com.abhikr.abhikr.ui.UserProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private TabLayout tabLayout;
    public static String STR_FRIEND_FRAGMENT = "FRIEND";
    public static String STR_GROUP_FRAGMENT = "GROUP";
    public static String STR_INFO_FRAGMENT = "INFO";
    int[] tabIcons = {
            R.drawable.ic_tab_person,
            R.drawable.ic_tab_group,
            R.drawable.ic_tab_infor
    };
    private FloatingActionButton floatButton;
    private ViewPagerAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        ServiceUtils.stopServiceFriendChat(getApplicationContext(), false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final Toolbar toolbar =  findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            //getSupportActionBar().setTitle(R.string.app_name);
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);*/
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                } else {
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finishAfterTransition();
                    // MainActivity.this.finish();
                }
                // ...
            }
        };
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        floatButton =  findViewById(R.id.fabmainatv);
        final ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs_main);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
          /*      Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        //Toast.makeText(MainActivity.this, "Loading coupons..", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Loading coupons..");
                        break;
                    case 1:
                        //Toast.makeText(MainActivity.this, "Loading deals..", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Loading deals..");
                        break;
                    case 2:
                        //Toast.makeText(MainActivity.this, "Loading coupons & deals by store..", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Loading coupons & deals by store..");
                        break;
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        try {
            setupTabIcons();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void setupTabIcons() {

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);
        /*TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tablay, null);
        tabOne.setText(STR_FRIEND_FRAGMENT);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_person, 0, 0);
        tabOne.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Objects.requireNonNull(tabLayout.getTabAt(0)).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tablay, null);
        tabTwo.setText(STR_GROUP_FRAGMENT);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_group, 0, 0);
        //tabTwo.setTextColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(tabLayout.getTabAt(1)).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tablay, null);
        tabThree.setText(STR_INFO_FRAGMENT);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_infor, 0, 0);
        tabThree.setTextColor(getResources().getColor(android.R.color.black));
        Objects.requireNonNull(tabLayout.getTabAt(2)).setCustomView(tabThree);*/
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FriendsFragment(), STR_FRIEND_FRAGMENT);
        adapter.addFrag(new GroupFragment(), STR_GROUP_FRAGMENT);
        adapter.addFrag(new UserProfileFragment(), STR_INFO_FRAGMENT);
        floatButton.setOnClickListener(((FriendsFragment) adapter.getItem(0)).onClickFloatButton.getInstance(this));
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(3);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        //adapter.notifyDataSetChanged(); if u choose 1 it will again load only one tab at a time( by deafult its take 1 as limit value)
        //ConnectionAbhikr.getInstance(getApplicationContext()).ABhiToast(String.valueOf(limit));
        viewPager.setOffscreenPageLimit(limit);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ServiceUtils.stopServiceFriendChat(getApplicationContext(), false);
                if (adapter.getItem(position) instanceof FriendsFragment) {
                    floatButton.setVisibility(View.VISIBLE);
                    floatButton.setOnClickListener(((FriendsFragment) adapter.getItem(position)).onClickFloatButton.getInstance(MainActivity.this));
                    floatButton.setImageResource(R.drawable.plus);
                } else if (adapter.getItem(position) instanceof GroupFragment) {
                    floatButton.setVisibility(View.VISIBLE);
                    floatButton.setOnClickListener(((GroupFragment) adapter.getItem(position)).onClickFloatButton.getInstance(MainActivity.this));
                    floatButton.setImageResource(R.drawable.ic_float_add_group);
                } else {
                    floatButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
//            if (data.getStringExtra(STR_EXTRA_ACTION).equals(LoginActivity.STR_EXTRA_ACTION_LOGIN)) {
//                authUtils.signIn(data.getStringExtra(STR_EXTRA_USERNAME), data.getStringExtra(STR_EXTRA_PASSWORD));
//            } else if (data.getStringExtra(STR_EXTRA_ACTION).equals(RegisterActivity.STR_EXTRA_ACTION_REGISTER)) {
//                authUtils.createUser(data.getStringExtra(STR_EXTRA_USERNAME), data.getStringExtra(STR_EXTRA_PASSWORD));
//            }else if(data.getStringExtra(STR_EXTRA_ACTION).equals(LoginActivity.STR_EXTRA_ACTION_RESET)){
//                authUtils.resetPassword(data.getStringExtra(STR_EXTRA_USERNAME));
//            }
//        } else if (resultCode == RESULT_CANCELED) {
//            this.finish();
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Toast.makeText(this, getString(R.string.app_name)+" Version "+BuildConfig.VERSION_NAME, Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.home_call)
        {
            supportFinishAfterTransition();
        }
        if(id==R.id.about_home)
        {
            startActivity(new Intent(MainActivity.this, WorkStation.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        ServiceUtils.startServiceFriendChat(getApplicationContext());
        super.onDestroy();
    }
    // abhi_internet checking end here
    //fragmentpageradaptor loads all fragment in memory stack but fragmentstatepageradaptor works with heavy data fragment like images
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
           /* switch (position) {
                case 0:
                    return new Coupon_Fragment();
                case 1:
                    return new Deal_fragment();
                case 2:
                    return new Store_fragment();
                default:
                    return null;
            }*/
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            //return null;// this icon only not text
            return mFragmentTitleList.get(position); // custom text with above icon
        }
    }
}