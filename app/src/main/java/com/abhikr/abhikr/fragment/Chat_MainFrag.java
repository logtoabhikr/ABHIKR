package com.abhikr.abhikr.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.abhikr.abhikr.R;
import com.abhikr.abhikr.data.StaticConfig;
import com.abhikr.abhikr.service.ServiceUtils;
import com.abhikr.abhikr.ui.FriendsFragment;
import com.abhikr.abhikr.ui.GroupFragment;
import com.abhikr.abhikr.ui.UserProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Chat_MainFrag extends Fragment {

    private static String TAG = "ChatActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private TabLayout tabLayout;
    private static String STR_FRIEND_FRAGMENT = "FRIEND";
    private static String STR_GROUP_FRAGMENT = "GROUP";
    private static String STR_INFO_FRAGMENT = "INFO";
    private FloatingActionButton floatButton;
    private int[] tabIcons = {
            R.drawable.ic_tab_person,
            R.drawable.ic_tab_group,
            R.drawable.ic_tab_infor
    };
    private Chat_MainFrag.ViewPagerAdapter adapter;
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        ServiceUtils.stopServiceFriendChat(requireContext(), false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat__main, container, false);
        /*final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            //getSupportActionBar().setTitle(R.string.app_name);
            *//*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);*//*
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                StaticConfig.UID = user.getUid();
            } else {
                //getActivity().finish();
                // User is signed in
                //startActivity(new Intent(getContext(), LoginActivity.class));
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        };
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        floatButton =  view.findViewById(R.id.fabchatmain);
        final ViewPager viewPager =view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorIndivateTab));
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
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Chat_MainFrag.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new FriendsFragment(), STR_FRIEND_FRAGMENT);
        adapter.addFrag(new GroupFragment(), STR_GROUP_FRAGMENT);
        adapter.addFrag(new UserProfileFragment(), STR_INFO_FRAGMENT);
        floatButton.setOnClickListener(((FriendsFragment) adapter.getItem(0)).onClickFloatButton.getInstance(getActivity()));
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
                ServiceUtils.stopServiceFriendChat(requireContext(), false);
                if (adapter.getItem(position) instanceof FriendsFragment) {
                    floatButton.setVisibility(View.VISIBLE);
                    floatButton.setOnClickListener(((FriendsFragment) adapter.getItem(position)).onClickFloatButton.getInstance(getContext()));
                    floatButton.setImageResource(R.drawable.plus);
                } else if (adapter.getItem(position) instanceof GroupFragment) {
                    floatButton.setVisibility(View.VISIBLE);
                    floatButton.setOnClickListener(((GroupFragment) adapter.getItem(position)).onClickFloatButton.getInstance(getContext()));
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

    /**
     * Adapter hien thi tab
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
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

            // return null to display only the icon
            //return null;
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        ServiceUtils.startServiceFriendChat(getContext());
        super.onDestroy();
    }
}
