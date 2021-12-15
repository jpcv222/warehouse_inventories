package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ENTIDAD.AdaptadorLvPedidos;
import ENTIDAD.AdaptadorPedidos;
import ENTIDAD.Pedidos;
import ENTIDAD.ProductoPedidos;
import ENTIDAD.URLS;

public class EnviaPedido extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lvPedidoEnvio;
    private ArrayList<Pedidos> listaPedidosEnvio, cadenaProductos;
    private TextView txtNotFound;
    private ArrayList<ProductoPedidos> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envia_pedido);
        initComponents();
        consultaPedidosEnvio();
    }


    private void initComponents(){
        lvPedidoEnvio = (ListView)findViewById(R.id.lvEnvioPedidos);
        txtNotFound = (TextView)findViewById(R.id.txtNotFoundEnvia);
        lvPedidoEnvio.setOnItemClickListener(this);
        listaPedidosEnvio = new ArrayList<>();
        listaProductos = new ArrayList<>();
        cadenaProductos = new ArrayList<>();
    }

    private void consultaPedidosEnvio(){
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
                    txtNotFound.setVisibility(View.VISIBLE);
                    lvPedidoEnvio.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }else{
                    response = response.replace("][",",");

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i< jsonArray.length(); i+=5){
                            listaPedidosEnvio.add(new Pedidos(jsonArray.getInt(i), jsonArray.getString(i+1), jsonArray.getInt(i+2), jsonArray.getString(i+3), jsonArray.getString(i+4)));
                        }

                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listaPedidosEnvio);
                        AdaptadorPedidos adapter = new AdaptadorPedidos(listaPedidosEnvio, EnviaPedido.this,"ENVIADO");
                        lvPedidoEnvio.setAdapter(adapter);
                        lvPedidoEnvio.setVisibility(View.VISIBLE);
                        txtNotFound.setVisibility(View.GONE);
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
                params.put("opt", "consultaPedidosProceso");


                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

        Pedidos pedidos = this.listaPedidosEnvio.get(position);
        Intent intent = new Intent(EnviaPedido.this, MostrarDetalleProductos.class);
        intent.putExtra("opt", "consultaDetallePedidoEnvio");
        intent.putExtra("idpedido", String.valueOf(pedidos.getIdpedido()));
        intent.putExtra("titulo", "Número de pedido para envío: ");
        intent.putExtra("optActualizar", "actualizarEstatusEnvio");
        intent.putExtra("idpedidotexto", String.valueOf(pedidos.getIdpedidofinal()));
        intent.putExtra("paquetera", pedidos.getPaquetera());
        intent.putExtra("canal", pedidos.getCanal());
        startActivity(intent);

    }


}
