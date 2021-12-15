package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import ENTIDAD.AdaptadorConteoInventario;
import ENTIDAD.AdaptadorInventarioCierre;
import ENTIDAD.InventarioModelo;
import ENTIDAD.ProductosSap;
import ENTIDAD.URLS;

public class ConteoInventario extends AppCompatActivity {
    private ArrayList<InventarioModelo> listado;
    private RecyclerView recyclerViewProductosConteo;
    private Button btnConteo;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> cantidadConteo;
    private ArrayList<ProductosSap> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteo_inventario);
        initComponents();
        consultaInventarioAlmacen();
        consultaProductosSap();

    }

    private void initComponents(){
        listado = new ArrayList<>();
        lista = new ArrayList<>();
        cantidadConteo = new ArrayList<>();
        recyclerViewProductosConteo = (RecyclerView) findViewById(R.id.lvConteoInventario);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewProductosConteo.setNestedScrollingEnabled(false);
        recyclerViewProductosConteo.setHasFixedSize(true);
        recyclerViewProductosConteo.setLayoutManager(mLayoutManager);

        btnConteo = (Button)findViewById(R.id.btnConteo);

        btnConteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidadConteo.clear();
                JSONObject object = new JSONObject();
                for(InventarioModelo modelo : AdaptadorInventarioCierre.listado){
                    try {
                        object.put("producto", modelo.getProducto());
                        object.put("cantidad", modelo.getCantidadconteo()*modelo.getConversion());
                        object.put("idproducto", modelo.getIdproducto());
                        object.put("idsubproducto", modelo.getIdsubproducto());
                        for(ProductosSap sap : lista){
                            if(modelo.getCodigo_sap().equals(sap.getProducto())){
                                object.put("cantidadsap", sap.getCantidad());
                            }
                        }
                        cantidadConteo.add(String.valueOf(object));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String json = String.valueOf(cantidadConteo);
                insertarConteo(json);

                Log.d("conteoinventario", ""+cantidadConteo);
            }
        });
    }

    private void consultaInventarioAlmacen(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    progressDialog.dismiss();
                }else{
                    response = response.replace("][",",");
                    try {
                        JSONArray ja = new JSONArray(response);
                        for(int i = 0; i<ja.length(); i+=7){
                            listado.add(new InventarioModelo(ja.getString(i), ja.getInt(i+1), ja.getInt(i+2), ja.getString(i+3), ja.getInt(i+4), ja.getInt(i+5), ja.getInt(i+6)));
                        }

                        AdaptadorInventarioCierre adapter = new AdaptadorInventarioCierre(ConteoInventario.this, listado);
                        recyclerViewProductosConteo.setAdapter(adapter);
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt","consultaInventarioAlmacen");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void insertarConteo(final String json){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Enviando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ConteoInventario", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("response").equals("OK")){
                        AlertDialog.Builder dialogo = new AlertDialog.Builder(ConteoInventario.this);
                        dialogo.setTitle("Atención");
                        dialogo.setMessage("La información se mandó correctamente");
                        dialogo.setCancelable(false);
                        dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(ConteoInventario.this, principal.class);
                                startActivity(intent);
                            }
                        });
                        dialogo.show();
                    }else{
                        AlertDialog.Builder dialogo = new AlertDialog.Builder(ConteoInventario.this);
                        dialogo.setTitle("Atención");
                        dialogo.setMessage("La información no se mandó correctamente");
                        dialogo.setCancelable(false);
                        dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialogo.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "insertarConteoInventario");
                params.put("usuario", login.usuario);
                params.put("json", json);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void consultaProductosSap(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray keys = obj.names();
                    String key;
                    int value;

                    for(int i = 0; i<keys.length(); i++){
                        key = keys.getString(i);
                        value = obj.getInt(key);

                        lista.add(new ProductosSap(key, value));
                    }

                    Log.d("sap", ""+lista);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt","getCantidadProducto");


                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
