package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import ENTIDAD.AdaptadorPedidos;
import ENTIDAD.AdaptadorSpinner;
import ENTIDAD.Pedidos;
import ENTIDAD.ProductoPedidos;
import ENTIDAD.URLS;

public class VerificadorPedidos extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private ListView lvPedidos;
    private ArrayList<Pedidos> listadoPedidos;
    private ArrayList<ProductoPedidos> productoPedidos;
    private ArrayList<Pedidos> pedidosVerificados;
    private TextView txtTitulo, txtNoPedido, txtErrorNotFound;
    private Button btnVerificarPedido;
    private String idpedido = "";
    private Spinner spPedidosVerificados;
    private boolean isDetalle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificador_pedidos);
        initComponents();
        consultaPedidosFaltantes();
        //pedidosVerificados();

    }

    private void initComponents(){
        lvPedidos = (ListView)findViewById(R.id.lvPedidos);
        txtTitulo = (TextView)findViewById(R.id.txtTituloVerificador);
        txtNoPedido = (TextView)findViewById(R.id.txtIdPedido);
        btnVerificarPedido = (Button)findViewById(R.id.btnVerificarPedido);
        spPedidosVerificados = (Spinner)findViewById(R.id.spPedidosVerificados);
        txtErrorNotFound = (TextView)findViewById(R.id.txtErroNotFound);
        spPedidosVerificados.setOnItemSelectedListener(this);

        txtNoPedido.setVisibility(View.GONE);
        btnVerificarPedido.setVisibility(View.GONE);

        lvPedidos.setOnItemClickListener(this);
        listadoPedidos = new ArrayList<>();
        productoPedidos = new ArrayList<>();
        pedidosVerificados = new ArrayList<>();
        txtTitulo.setText("Pedidos en preparación");

        btnVerificarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void consultaPedidosFaltantes(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VerificadorPedidos", response);
                if(response.isEmpty()){
                    lvPedidos.setVisibility(View.GONE);
                    txtErrorNotFound.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }else{
                    txtErrorNotFound.setVisibility(View.GONE);
                    lvPedidos.setVisibility(View.VISIBLE);
                    response = response.replace("][",",");
                    try {
                        JSONArray ja = new JSONArray(response);
                        for(int i = 0; i<ja.length(); i+=5){
                            listadoPedidos.add(new Pedidos(ja.getInt(i), ja.getString(i+1), ja.getInt(i+2), ja.getString(i+3), ja.getString(i+4)));
                        }
                        AdaptadorPedidos adapter = new AdaptadorPedidos(listadoPedidos, getApplicationContext(),"VERIFICADO");
                        lvPedidos.setAdapter(adapter);
                        progressDialog.dismiss();
                        txtErrorNotFound.setVisibility(View.GONE);
                        isDetalle = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
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
                params.put("opt", "consultaPedidosFaltantes");

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(isDetalle == false){
            Pedidos pedido = this.listadoPedidos.get(position);
            this.idpedido = String.valueOf(pedido.getIdpedido());
            Intent intent = new Intent(VerificadorPedidos.this, MostrarDetalleProductos.class);
            intent.putExtra("idpedido", this.idpedido);
            intent.putExtra("opt", "consultaDetallePedidoFaltante");
            intent.putExtra("titulo", "Número de pedido para verificar: ");
            intent.putExtra("optActualizar", "verificarPedido");
            intent.putExtra("destino","");
            intent.putExtra("cadenaProductos","");
            intent.putExtra("idpedidotexto", String.valueOf(pedido.getIdpedidofinal()));
            startActivity(intent);
            //consultaDetallePedidoFaltante(String.valueOf(pedido.getIdpedido()));
        }


    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if(spPedidosVerificados.getSelectedItem().toString().equals("Elige un pedido")){
           // Toast.makeText(getApplicationContext(), "Elige un pedido, este no es un pedido válido", Toast.LENGTH_LONG).show();
        }else{
            productoPedidos.clear();
            Intent intent = new Intent(VerificadorPedidos.this, MostrarDetalleProductos.class);
            intent.putExtra("idpedido", spPedidosVerificados.getSelectedItem().toString());
            intent.putExtra("opt", "consultaDetallePedidoFaltante");
            intent.putExtra("titulo", "Número de pedido para verificar: ");
            intent.putExtra("optActualizar", "verificarPedido");
            intent.putExtra("destino","");
            intent.putExtra("cadenaProductos","");
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}