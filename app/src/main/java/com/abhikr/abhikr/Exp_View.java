package com.abhikr.abhikr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class Exp_View extends Fragment implements View.OnClickListener {

private ExpandableLayout expandableLayout0;
private ExpandableLayout expandableLayout1;
        ImageView abhi;

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exp__view, container, false);

        expandableLayout0 = (ExpandableLayout) rootView.findViewById(R.id.expandable_layout_0);
        expandableLayout1 = (ExpandableLayout) rootView.findViewById(R.id.expandable_layout_1);
        abhi= (ImageView) rootView.findViewById(R.id.ak);
        abhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent i=new Intent(getContext(),MainActivity_old.class);
                        startActivity(i);
                }
        });

        expandableLayout0.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
@Override
public void onExpansionUpdate(float expansionFraction, int state) {
        Log.d("ExpandableLayout0", "State: " + state);
        }
        });

        expandableLayout1.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
@Override
public void onExpansionUpdate(float expansionFraction, int state) {
        Log.d("ExpandableLayout1", "State: " + state);
        }
        });

        rootView.findViewById(R.id.expand_button).setOnClickListener(this);

        return rootView;
        }

@Override
public void onClick(View view) {
        if (expandableLayout0.isExpanded()) {
        expandableLayout0.collapse();
        } else if (expandableLayout1.isExpanded()) {
        expandableLayout1.collapse();
        } else {
        expandableLayout0.expand();
        expandableLayout1.expand();
        }
        }
        }
