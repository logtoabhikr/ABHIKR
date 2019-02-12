package com.abhikr.abhikr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {

    private EditText inputEmail;

    private Button btnReset, btnBack;

    private FirebaseAuth auth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_pass);

        inputEmail = (EditText) findViewById(R.id.email);

        btnReset = (Button) findViewById(R.id.btn_reset_password);

        btnBack = (Button) findViewById(R.id.btn_back);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPass.this,SignIn.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    AlertDialog.Builder abhi=new AlertDialog.Builder(ResetPass.this);
                                    abhi.setMessage("We have sent you password reset link to your register email ! check your email");
                                    abhi.setPositiveButton("Open Email", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                            startActivity(intent);
                                        }
                                    });
                                    abhi.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    AlertDialog alert=abhi.create();
                                    alert.show();
                                    Toast.makeText(ResetPass.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPass.this, "Invalid Email ! Enter your correct Email", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}