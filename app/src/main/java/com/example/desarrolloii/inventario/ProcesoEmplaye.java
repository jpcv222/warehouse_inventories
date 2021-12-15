package com.example.desarrolloii.inventario;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import ENTIDAD.Adaptador;
import ENTIDAD.AdaptadorLvPedidos;
import ENTIDAD.AdaptadorPedidos;
import ENTIDAD.Pedidos;
import ENTIDAD.ProductoPedidos;
import ENTIDAD.URLS;

public class ProcesoEmplaye extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lvProcesoEmplaye;
    private ArrayList<Pedidos> listaPedidos, cadenaProductos;
    private ArrayList<ProductoPedidos> listaProductos;
    private TextView txtErrorNotFound;
    private String destino = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_emplaye);
        initComponents();
        consultaPedidosToEmplaye();
    }

    private void initComponents(){
        lvProcesoEmplaye = (ListView)findViewById(R.id.lvProcesoEmplaye);
        txtErrorNotFound = (TextView)findViewById(R.id.txtNotFoundEmplaye);
        lvProcesoEmplaye.setOnItemClickListener(this);

        listaPedidos = new ArrayList<>();
        listaProductos = new ArrayList<>();
        cadenaProductos = new ArrayList<>();
    }

    private void consultaPedidosToEmplaye(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    txtErrorNotFound.setVisibility(View.VISIBLE);
                    lvProcesoEmplaye.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }else{
                    response = response.replace("][",",");
                    try {
                        JSONArray ja = new JSONArray(response);
                        for(int i = 0; i<ja.length(); i+=5){
                            listaPedidos.add(new Pedidos(ja.getInt(i), ja.getString(i+1), ja.getInt(i+2), ja.getString(i+3), ja.getString(i+4)));
                        }

                        AdaptadorPedidos adapter = new AdaptadorPedidos(listaPedidos, ProcesoEmplaye.this,"EMPLAYE");
                        lvProcesoEmplaye.setAdapter(adapter);
                        lvProcesoEmplaye.setVisibility(View.VISIBLE);
                        txtErrorNotFound.setVisibility(View.GONE);
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
                params.put("opt","pedidosVerificados");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Pedidos pedidos = this.listaPedidos.get(i);
        String pedido = String.valueOf(pedidos.getIdpedido());

        Intent intent = new Intent(ProcesoEmplaye.this, MostrarDetalleProductos.class);
        intent.putExtra("idpedido", pedido);
        intent.putExtra("opt", "consultaDetallePedidoEmplaye");
        intent.putExtra("titulo", "Número de pedido para emplaye: ");
        intent.putExtra("optActualizar", "actualizarEstatusEmplaye");
        intent.putExtra("idpedidotexto", String.valueOf(pedidos.getIdpedidofinal()));
        intent.putExtra("canal", pedidos.getCanal());

        startActivity(intent);



    }

    private void consultaDetalleProductoEmplaye(final ListView lvProcesoEmplaye, final AlertDialog alertDialog, final String idpedido){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información");
        progressDialog.setCancelable(false);
        progressDialog.show();
        listaProductos.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                Log.d("ProcesoEmplaye", response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for(int i = 0; i<ja.length(); i+=5){
                        listaProductos.add(new ProductoPedidos(ja.getString(i), ja.getInt(i+1), ja.getString(i+2),ja.getString(i+3), ja.getInt(i+4)));
                    }

                    AdaptadorLvPedidos adapter = new AdaptadorLvPedidos(listaProductos, getApplicationContext());
                    lvProcesoEmplaye.setAdapter(adapter);
                    alertDialog.show();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "consultaDetallePedidoEmplaye");
                params.put("idpedido", idpedido);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void actualizarEstatus(final String idpedido, final String cadenaProductos, final String destino){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Enviando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("response").equals("OK")){
                        Toast.makeText(getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_LONG).show();
                        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ProcesoEmplaye.this);
                        dialog.setTitle("Atención");
                        dialog.setMessage("Se actualizó correctamente");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(ProcesoEmplaye.this, principal.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }else{
                        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ProcesoEmplaye.this);
                        dialog.setTitle("Atención");
                        dialog.setMessage("No se pudo actualizar correctamente, "+obj.getString("response"));
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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
                params.put("opt", "actualizarEstatusEmplaye");
                params.put("idpedido", idpedido);
                params.put("productos", cadenaProductos+";");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
