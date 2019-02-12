package com.abhikr.abhikr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class EXP extends Activity implements View.OnClickListener {

private ExpandableLayout expandableLayout0;
private ExpandableLayout expandableLayout1;

        ImageView abhikr;
    WebView abhi;
    ProgressDialog pg;
    TextView expend_button;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    AlertDialog spotsdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);
        spotsdialog=new SpotsDialog(EXP.this, R.style.abhi);

        expandableLayout0 = (ExpandableLayout) findViewById(R.id.expandable_layout_0);
        expandableLayout1 = (ExpandableLayout) findViewById(R.id.expandable_layout_1);
        expend_button= (TextView) findViewById(R.id.expand_button);
        abhikr= (ImageView) findViewById(R.id.ak);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
         Intent i=new Intent(EXP.this,SignIn.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
        user=firebaseAuth.getCurrentUser();
        expend_button.setText(user.getEmail()+" ! Touch Here ");
        abhikr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity_old.class);
                startActivity(i);
            }
        });
        abhi = (WebView) findViewById(R.id.webview);
        pg=new ProgressDialog(EXP.this);
        //pg.setMessage("Loading ...");
        abhi.getSettings().setJavaScriptEnabled(true);
        WebSettings ak=abhi.getSettings();
        ak.setBuiltInZoomControls(false);
        ak.setLoadsImagesAutomatically(true);
        abhi.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        abhi.loadUrl("http://www.abhikr.com/");
        Toast.makeText(this, "To load user view click on top - ", Toast.LENGTH_SHORT).show();
        abhi.setWebViewClient(new abhikr1());

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

        findViewById(R.id.expand_button).setOnClickListener(this);


    }
    public class abhikr1 extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //pg.show();
            if(Appstatus.getInstance(EXP.this).isOnline())
            {
                Toast.makeText(EXP.this, "Internet detected ! Go ahead", Toast.LENGTH_SHORT).show();
                spotsdialog.show();
            }
            else
            {
                pg.setMessage("Connect with Internet to hide this window !!!! Loading cache file ");
                pg.show();
                Toast.makeText(EXP.this, "Connect with internet to hide dialog message", Toast.LENGTH_SHORT).show();

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
            if (pg.isShowing())
                {
                    spotsdialog.dismiss();
                    spotsdialog=null;
                }
            super.onPageFinished(view, url);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.phone)
        {
         Intent i=new Intent(EXP.this, MainActivity_old.class);
                 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        if(id==R.id.user)
        {
            expend_button.setText(user.getEmail()+" ! Touch Here ");
        }
        if(id==R.id.logout)
        {
         firebaseAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

