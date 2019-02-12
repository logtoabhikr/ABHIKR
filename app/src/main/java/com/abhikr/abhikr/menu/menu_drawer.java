package com.abhikr.abhikr.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.abhikr.abhikr.R;

/**
 * Created by ak on 11/26/17.
 */

public class menu_drawer extends AppCompatActivity{
    TextView username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_left_drawer);
        /*username=findViewById(R.id.username);
        username.setText("hello abhi");*/

    }
}
