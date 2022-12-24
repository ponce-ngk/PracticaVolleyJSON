package com.kdpr.practicavolleyjson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.ProductoAdapter;
import Models.Producto;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerProduct;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerProduct = (RecyclerView) findViewById(R.id.recyclerVolley);
        recyclerProduct.setHasFixedSize(true);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerProduct.setItemAnimator(new DefaultItemAnimator());
        // ...
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        String url ="https://api.uealecpeterson.net/public/productos/search";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Producto> productos = new ArrayList<Producto> ();
                        try {
                            JSONObject JSONlista =  new JSONObject(response);
                            JSONArray JSONlistaProducto= JSONlista.getJSONArray("productos");
                            //JSONArray JSONlistaProducto=  new JSONArray(response);
                            productos = Producto.JsonObjectsBuild(JSONlistaProducto);
                            int resId = R.anim.layout_animation_down_to_up;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(),
                                    resId);
                            recyclerProduct.setLayoutAnimation(animation);
                            ProductoAdapter adaptadorproducto = new ProductoAdapter(getApplicationContext(), productos);
                            recyclerProduct.setAdapter(adaptadorproducto);
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZHVzciI6OSwiZW1haWwiOiJjemFtYnJhbm9AdXRlcS5lZHUuZWMiLCJpYXQiOjE2NzE1OTU4MDYsImV4cCI6MTY3MTk1NTgwNn0.ne3Hq-vVrD3Wmh18RXhrLizxlCv7mwkB3B9mrrZBWX4");
                return param;
            }
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("fuente","1");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}