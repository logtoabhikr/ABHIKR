package com.abhikr.abhikr.menu;

import android.os.Bundle;
import android.widget.TextView;

import com.abhikr.abhikr.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
