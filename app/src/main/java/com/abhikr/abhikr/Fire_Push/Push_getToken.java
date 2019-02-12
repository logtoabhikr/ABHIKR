package com.abhikr.abhikr.Fire_Push;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abhikr.abhikr.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Push_getToken extends Fragment implements View.OnClickListener{

    //defining views
    private Button buttonSendPush;
    private Button buttonRegister;
    private EditText editTextEmail;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getting views from xml
        View abhi=inflater.inflate(R.layout.fragment_push_get_token, container, false);
        editTextEmail = (EditText) abhi.findViewById(R.id.editTextEmail);
        buttonRegister = (Button) abhi.findViewById(R.id.buttonRegister);
        buttonSendPush = (Button) abhi.findViewById(R.id.buttonSendNotification);

        //adding listener to view
        buttonRegister.setOnClickListener(this);
        buttonSendPush.setOnClickListener(this);
        return abhi;
    }
    //storing token to mysql server
    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
        final String email = editTextEmail.getText().toString();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            sendTokenToServer();
        }

        //starting send notification activity
        if (view == buttonSendPush) {
            Push_sendNotification abhi=new Push_sendNotification();
            FragmentManager manager=getFragmentManager();
            manager.beginTransaction().replace(R.id.container,abhi,"abhi").addToBackStack(null).commit();

        }
    }
}