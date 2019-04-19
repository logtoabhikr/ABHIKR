package com.abhikr.abhikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhikr.abhikr.R;

import androidx.fragment.app.Fragment;


public class Success extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View abhi=inflater.inflate(R.layout.fragment_success, container, false);


        return abhi;
    }

}
