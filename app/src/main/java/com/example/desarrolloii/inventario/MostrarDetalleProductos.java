package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import ENTIDAD.AdaptadorLvPedidos;
import ENTIDAD.AdapterPaqueterasSp;
import ENTIDAD.PaqueterasModelo;
import ENTIDAD.ProductoPedidos;
import ENTIDAD.URLS;

public class MostrarDetalleProductos extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private ArrayList<ProductoPedidos> listaProductos;
    private ArrayList<String> cadenaProductos;
    private ArrayList<PaqueterasModelo> paqueterasModelos;
    private ListView lvProductos;
    private Spinner spPaquetera;
    private String pedido = "", opt = "", titulo = "", optActualizar = "", destino = "", idpedidotitulo = "", paquetera = "", paqueterafinal = "", canal="";
    private Button btnCambiarEstatus;
    private TextView txtTitulo, txtNoPedido, txtPaquetera;
    private static String TAG = "MostrarDetalleProductos";
    AdapterPaqueterasSp adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_detalle_productos);
        initComponents();
        getIntentString();
        consultaPaqueteras();

    }

    private void initComponents(){
        listaProductos = new ArrayList<>();
        lvProductos = (ListView)findViewById(R.id.lvDetalleProductos);
        btnCambiarEstatus = (Button)findViewById(R.id.btnCambiarEstatus);
        txtTitulo = (TextView)findViewById(R.id.txtTituloDetalleProductos);
        txtNoPedido = (TextView)findViewById(R.id.txtNoPedidoGral);
        btnCambiarEstatus = (Button)findViewById(R.id.btnCambiarEstatus);
        spPaquetera = (Spinner)findViewById(R.id.spPaqueteras);
        txtPaquetera = (TextView)findViewById(R.id.txtPaquetera);
        btnCambiarEstatus.setOnClickListener(this);
        cadenaProductos = new ArrayList<>();
        paqueterasModelos = new ArrayList<>();
        spPaquetera.setOnItemSelectedListener(this);

    }

    private void getIntentString(){
        pedido = getIntent().getStringExtra("idpedido");
        opt = getIntent().getStringExtra("opt");
        titulo = getIntent().getStringExtra("titulo");
        optActualizar = getIntent().getStringExtra("optActualizar");
        idpedidotitulo = getIntent().getStringExtra("idpedidotexto");

        if(pedido.equals("") && opt.equals("") && titulo.equals("") && optActualizar.equals("") && idpedidotitulo.equals("")){
            Toast.makeText(this, "Intenta de nuevo por favor.", Toast.LENGTH_LONG).show();
        }else{
            txtTitulo.setText(titulo);
            txtNoPedido.setText(idpedidotitulo);
            consultaDetalleProductos(pedido, opt);
        }

        if(opt.equals("consultaDetallePedidoEnvio")){
            spPaquetera.setVisibility(View.VISIBLE);
            txtPaquetera.setVisibility(View.VISIBLE);
        }else{
            spPaquetera.setVisibility(View.GONE);
            txtPaquetera.setVisibility(View.GONE);
        }

        if(optActualizar.equals("actualizarEstatusEnvio")){
            paquetera = getIntent().getStringExtra("paquetera");
        }

        if(optActualizar.equals("actualizarEstatusEmplaye") || optActualizar.equals("actualizarEstatusEnvio")){
            canal = getIntent().getStringExtra("canal");
        }
    }

    private void consultaDetalleProductos(final String pedido, final String opt){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                Log.d(TAG, response);
                try {
                    JSONArray ja = new JSONArray(response);
                    for(int i = 0; i<ja.length(); i+=5){
                        listaProductos.add(new ProductoPedidos(ja.getString(i), ja.getInt(i+1), ja.getString(i+2),ja.getString(i+3), ja.getInt(i+4)));
                    }

                    AdaptadorLvPedidos adapter = new AdaptadorLvPedidos(listaProductos, getApplicationContext());
                    lvProductos.setAdapter(adapter);

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
                params.put("opt", opt);
                params.put("idpedido", pedido);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }

    private void actualizarEstatus(final String pedido, final String opt, final String productos, final String destino){
        btnCambiarEstatus.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Enviando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try{
                    JSONObject object = new JSONObject(response);
                    if(object.getString("response").equals("OK")){
                        mostrarDialogo(MostrarDetalleProductos.this, "La información se envió correctamente", object.getString("response"));
                        canal = "";

                    }else{
                        mostrarDialogo(MostrarDetalleProductos.this, "La información no se envió correctamente"+object.getString("response"), object.getString("response"));

                    }
                    btnCambiarEstatus.setEnabled(true);
                    progressDialog.dismiss();
                }catch (JSONException e){
                    e.printStackTrace();
                    btnCambiarEstatus.setEnabled(true);
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    btnCambiarEstatus.setEnabled(true);
                    progressDialog.dismiss();
                }
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                btnCambiarEstatus.setEnabled(true);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("opt", opt);
                if(opt.equals("consultaDetallePedidoEnvio") || optActualizar.equals("actualizarEstatusEnvio")){

                    params.put("paquetera", paqueterafinal);
                    Log.d("Paquetera", paqueterafinal);
                }
                if(opt.equals("consultaDetallePedidoEmplaye") || opt.equals("actualizarEstatusEmplaye") || opt.equals("consultaDetallePedidoEnvio") || opt.equals("actualizarEstatusEnvio")){
                    params.put("canal", canal);
                }

                params.put("idpedido", pedido);
                params.put("productos", productos);
                params.put("destino", destino);
                params.put("usuario", login.usuario);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void consultaPaqueteras(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                response = response.replace("][",",");
                try {
                    JSONArray ja = new JSONArray(response);
                    paqueterasModelos.add(new PaqueterasModelo(0,"NINGUNO"));
                    for (int i = 0; i<ja.length(); i+=2){
                        paqueterasModelos.add(new PaqueterasModelo(ja.getInt(i), ja.getString(i+1)));
                    }
                    adapter = new AdapterPaqueterasSp(getApplicationContext(), android.R.layout.simple_list_item_1, paqueterasModelos);
                    spPaquetera.setAdapter(adapter);

                    for(int i = 0; i<paqueterasModelos.size(); i++){
                        PaqueterasModelo modelo = paqueterasModelos.get(i);
                        if(modelo.getPaquetera().equals(paquetera) || modelo.getPaquetera().equals("null")){
                            spPaquetera.setSelection(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "consultaPaqueteras");

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCambiarEstatus){
            cadenaProductos.clear();

            for(ProductoPedidos modelo : listaProductos){
                cadenaProductos.add(modelo.getCodigo_sap()+":"+modelo.getCantidad());
                destino = modelo.getTransito_sap();
            }

            if(destino.equals("")){
                Toast.makeText(getApplicationContext(), "Intenta de nuevo por favor ", Toast.LENGTH_LONG).show();
            }else{
                String resultado = TextUtils.join(";",cadenaProductos);

                btnCambiarEstatus.setEnabled(false);
                //actualizarEstatus(pedido, optActualizar, resultado+";", destino);
                consultaEstatusPedido(pedido, optActualizar, resultado, destino);

                Log.d(TAG, resultado+" "+pedido+" "+optActualizar+" "+destino+" "+opt+" "+paquetera+" "+paqueterafinal+" "+canal);
            }
        }
    }

    public void mostrarDialogo(Context context, String mensaje, final String response){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
        dialogo.setTitle("ATENCIÓN");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(response.equals("OK") || response.equals("CANCELADO") || response.equals("SOLICITUD CANCELACION")){
                    Intent intent = new Intent(MostrarDetalleProductos.this, principal.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
        dialogo.show();

    }


    private void consultaEstatusPedido(final String pedido, final String opt, final String productos, final String destino){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONArray ja = new JSONArray(response);
                    if(ja.getString(0).equals("CANCELADO") || ja.getString(0).equals("SOLICITUD CANCELACION")){
                        mostrarDialogo(getApplicationContext(), "No se puede dar seguimiento a este pedido por que está cancelado", ja.getString(0));
                    }else{
                        actualizarEstatus(pedido, opt, productos+";", destino);
                        //Toast.makeText(getApplicationContext(), "Si se puede mandar la info", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "consultaEstatusPedido");
                params.put("idpedido", pedido);


                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PaqueterasModelo modelo = this.paqueterasModelos.get(position);
        paqueterafinal = modelo.getPaquetera();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        paqueterafinal = paquetera;
    }
}
