package com.example.asus.iotproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
   /* public String URLSERVICEMEdecin= "http://172.18.3.115:3000/getData";
    public  String URLSERVICLight= "http://172.18.18.87:3000/light";
    public  String URLSERVICLedon= "http://172.18.3.115:3000/ledon";
    public  String URLSERVICLedoff= "http://172.18.3.115:3000/ledoff";*/
   TextView tvlum,tvtemp;
    Switch ledonoff;
    RadioGroup rb_role;
    CheckBox check;
    Button btnon,btnoff,btnonfb,btnoffb,btnseuillum,btnseuiltemp;
    EditText seuiltemp,seuillum;
    public String urllum = "http://192.168.1.100:3000/lumiere";
    public String urltemp = "http://192.168.1.100:3000/temperature";
    public String urlbtnon = "http://192.168.1.100:3000/on";
    public String urlbtnoff = "http://192.168.1.100:3000/off";
    public String urlbtnonb = "http://192.168.1.100:3000/onbuzzer";
    public String urlbtnoffb = "http://192.168.1.100:3000/offbuzzer";
    public String urlseuillum = "http://192.168.1.100:3000/seuil_lum?data=";
    public String urlseuiltemp = "http://192.168.1.100:3000/seuil?data=";
    String etat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvlum = (TextView)findViewById(R.id.tv_lum);
        tvtemp=(TextView)findViewById(R.id.tv_temp);
        btnon = (Button)findViewById(R.id.btn_on);
        btnoff = (Button)findViewById(R.id.btn_off);
        btnonfb = (Button)findViewById(R.id.btn_onb);
        btnoffb = (Button)findViewById(R.id.btn_offb);
        btnseuillum = (Button)findViewById(R.id.btnseuilum);
        btnseuiltemp = (Button)findViewById(R.id.btnseuitemp);
        seuillum = (EditText)findViewById(R.id.seuillum);
        seuiltemp = (EditText)findViewById(R.id.seuiltemp);
        ledonoff = (Switch)findViewById(R.id.ledonoff);
        rb_role = (RadioGroup) findViewById(R.id.rb_role);
        check = (CheckBox)findViewById(R.id.check);
        GetLum();
        GetTemp();
        ledonoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    getledon();
                   Toast.makeText(MainActivity.this,"on",Toast.LENGTH_LONG).show();

                }
                else
                {
                    getledoff();
                    Toast.makeText(MainActivity.this,"off",Toast.LENGTH_LONG).show();
                }
            }


        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean on = ((CheckBox) v).isChecked();
                if(on)
                {
                    getledon();
                    Toast.makeText(MainActivity.this,"check on",Toast.LENGTH_LONG).show();

                }
                else
                {
                    getledoff();
                    Toast.makeText(MainActivity.this,"check off",Toast.LENGTH_LONG).show();
                }
            }


        });

        btnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getledon();
            }
        });
        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getledoff();
            }
        });
        btnonfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getbuzzeron();
            }
        });
        btnoffb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getbuzzeroff();
            }
        });
        btnseuillum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Funcseuillum();
            }
        });

        rb_role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etat = ((RadioButton) findViewById(rb_role.getCheckedRadioButtonId())).getText().toString();
                // checkedId is the RadioButton selected
                if (etat.equals("on")){
                    getledon();
                    Toast.makeText(MainActivity.this,"check on",Toast.LENGTH_LONG).show();

                }
                if (etat.equals("off")){
                    getledoff();
                    Toast.makeText(MainActivity.this,"check off",Toast.LENGTH_LONG).show();

                }
            }
        });
    }



    public void GetLum() {
        final String REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        StringRequest strReq = new StringRequest(Request.Method.GET, urllum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(REQUEST_TAG, response.toString());

                    JSONObject array = null;
                    try {
                        array = new JSONObject(response);
                        Resultat res = new Resultat();
                        res.setLum(array.getDouble("val"));
                        tvlum.setText(String.valueOf(res.getLum()));

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
                //   System.out.print("***********Noooooooooooo********");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(strReq);

    }
    public void GetTemp() {
        final String REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        // final List<Medecin> medecinsforSpinner = new ArrayList<>();
        int id = 1;
        // String urluser = urlselectuser + id;
        StringRequest strReq = new StringRequest(Request.Method.GET, urltemp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(REQUEST_TAG, response.toString());

                JSONObject array = null;
                try {
                    array = new JSONObject(response);
                    Resultat res = new Resultat();
                    res.setTemp(array.getDouble("val"));
                    tvtemp.setText(String.valueOf(res.getTemp()));

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
                //   System.out.print("***********Noooooooooooo********");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(strReq);

    }


    public void getledon (){
        final String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        StringRequest strReq = new StringRequest(Request.Method.GET , urlbtnon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
            }
        })
                ;
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);


    }
    public void getledoff (){
        final String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        StringRequest strReq = new StringRequest(Request.Method.GET , urlbtnoff, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
            }
        })
                ;
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }
    public void getbuzzeron(){
        final String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        StringRequest strReq = new StringRequest(Request.Method.GET , urlbtnonb, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
            }
        })
                ;
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }
    public void getbuzzeroff (){
        final String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        StringRequest strReq = new StringRequest(Request.Method.GET , urlbtnoffb, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
            }
        })
                ;
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);


    }
    public void Funcseuillum (){
        final String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        String url = urlseuillum+seuillum.getText();

        StringRequest strReq = new StringRequest(Request.Method.GET , url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
            }
        })
                ;
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);


    }

}
