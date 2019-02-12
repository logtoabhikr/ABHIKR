package com.abhikr.abhikr.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.abhikr.abhikr.Config;
import com.abhikr.abhikr.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Sms_Verify extends Fragment implements View.OnClickListener {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextConfirmOtp;

    private AppCompatButton buttonRegister;
    private AppCompatButton buttonConfirm;
    private AppCompatButton buttonCancel;

    //Volley RequestQueue
    private RequestQueue requestQueue;

    //String variables to hold username password and phone
    private String username;
    private String password;
    private String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View abhi = inflater.inflate(R.layout.fragment_sms__verify, container, false);
        editTextUsername = (EditText) abhi.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) abhi.findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) abhi.findViewById(R.id.editTextPhone);

        buttonRegister = (AppCompatButton) abhi.findViewById(R.id.buttonRegister);

        //Initializing the RequestQueue
        requestQueue = Volley.newRequestQueue(getActivity());

        //Adding a listener to button
        buttonRegister.setOnClickListener(this);
        return abhi;
    }
    //This method would confirm the otp
    private void confirmOtp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(getActivity());
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        buttonCancel = (AppCompatButton) confirmDialog.findViewById(R.id.buttonCancel);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               alertDialog.dismiss();
            }
        });
        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(getActivity(), "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();
                if(TextUtils.isEmpty(otp))
                {
                    editTextConfirmOtp.setError("Should not be Empty");
                    editTextConfirmOtp.requestFocus();
                }
                else if(otp.length()!=6)
                {
                    editTextConfirmOtp.setError("Should be 6 digit");
                    editTextConfirmOtp.requestFocus();
                }

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                //if the server response is success
                                if(response.equalsIgnoreCase("success")){
                                    //dismissing the progressbar

                                   /* if(loading.isShowing())
                                    {
                                        loading.cancel();
                                        loading.dismiss();
                                    }*/

                                    //Starting a new activity

                                    //startActivity(new Intent(getContext(), Success.class));
                                    //Success abhi=new Success();
                                    HomeFragment abhi=new HomeFragment();
                                    FragmentManager manager=getFragmentManager();
                                    manager.beginTransaction().replace(R.id.container,abhi,"test").addToBackStack(null).commit();
                                }
                                else{
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(getActivity(),"Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_USERNAME, username);
                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }


    //this method will register the user
    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Registering", "Please wait...", false, false);


        //Getting user data
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();

        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);

                            //If it is success
                            if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                                //Asking user to confirm otp
                                confirmOtp();
                            }
                            else{
                                //If not successful user may already have registered
                                Toast.makeText(getActivity(), ""+jsonResponse.getString(Config.TAG_RESPONSE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_USERNAME, username);
                params.put(Config.KEY_PASSWORD, password);
                params.put(Config.KEY_PHONE, phone);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    @Override
    public void onClick(View v) {
//Calling register method on register button click

     String u=editTextUsername.getText().toString();
     String pa=editTextPassword.getText().toString();
     String ph=editTextPhone.getText().toString();

        if(TextUtils.isEmpty(u))
        {
            editTextUsername.setError("Enter email ");
            editTextUsername.requestFocus();
            return;
        }
       else if(TextUtils.isEmpty(pa))
        {
            editTextPassword.setError("Enter password ");
            return;
        }

      else if(TextUtils.isEmpty(ph))
        {
            editTextPhone.setError("Enter phone number ");
            editTextPhone.requestFocus();
            return;
        }
       else if(ph.length()!=10)
        {
            editTextPhone.setError("Should be 10 digit");
            editTextPhone.requestFocus();
            return;
        }
        else
        {
            register();
        }




    }
   /* private String getMyPhoneNO()
    {


        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }
    private String getTenDigitPhoneNumber()
    {
        String s = getMyPhoneNO();

        return s != null && s.length() > 3 ? s.substring(3) : null;
    }*/


}



