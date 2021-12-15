package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ENTIDAD.AdaptadorLvProductos;
import ENTIDAD.AdaptadorPyP;
import ENTIDAD.ProductosPyP;
import ENTIDAD.URLS;

public class PandP extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    ArrayList<ProductosPyP> listado;
    Button btnIngresarProductosPyP;
    RecyclerView.LayoutManager layoutManager;
    ListView lvProductos;
    ArrayList<String> cadenaProductos;
    TextView txtNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pand_p);
        addComponents();
        consultaProductos();
    }

    private void addComponents(){
        listado = new ArrayList<>();
        cadenaProductos = new ArrayList<>();

        btnIngresarProductosPyP = (Button)findViewById(R.id.btnIngresarPyP);
        lvProductos = (ListView)findViewById(R.id.lvProductos);
        txtNotFound = (TextView)findViewById(R.id.txtNotFountpandp);
        btnIngresarProductosPyP.setOnClickListener(this);
    }

    private void consultaProductos(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PANDP", response);
                if(response.isEmpty()){
                    txtNotFound.setVisibility(View.VISIBLE);
                    lvProductos.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }else{
                    response = response.replace("][",",");
                    try {
                        JSONArray ja = new JSONArray(response);
                        for(int i = 0; i<ja.length(); i+=12){
                            listado.add(new ProductosPyP(ja.getString(i),
                                    ja.getInt(i+1),
                                    ja.getInt(i+2),
                                    ja.getString(i+3),
                                    ja.getInt(i+4),
                                    ja.getInt(i+5),
                                    ja.getInt(i+6),
                                    false,
                                    ja.getInt(i+7),
                                    ja.getString(i+8),
                                    ja.getString(i+9),
                                    ja.getString(i+10),
                                    ja.getInt(i+11)));
                        }

                        AdaptadorLvProductos adapter = new AdaptadorLvProductos(listado, PandP.this);
                        lvProductos.setAdapter(adapter);
                        lvProductos.setVisibility(View.VISIBLE);
                        txtNotFound.setVisibility(View.GONE);
                        btnIngresarProductosPyP.setVisibility(View.VISIBLE);
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
                params.put("opt", "consultaProductos");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int pos = lvProductos.getPositionForView(compoundButton);
        if(pos != ListView.INVALID_POSITION){
            ProductosPyP productosPyP = listado.get(pos);
            productosPyP.setChecked(b);
            //Toast.makeText(this, "CheckBox clicked "+productosPyP.getNombreCompleto()+" Current state: "+b, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnIngresarPyP:
                boolean isChecked = true;
                String almacendestino = "";
                String almacenorigen = "";
                int idtraspaso = 0;
                String idpedidosv ="";
                String resultado ="";
                String[] arregloIds;
                JSONObject jsonObject = new JSONObject();
                JSONObject json = new JSONObject();
                ArrayList<String> ids = new ArrayList<>();
                ArrayList<String> iddetalle = new ArrayList<>();
                cadenaProductos = new ArrayList<>();
                for(int i = 0; i<listado.size(); i++){
                    ProductosPyP modelo = this.listado.get(i);
                    if(modelo.isChecked() == false){

                        isChecked = false;
                        break;
                    }else{

                        cadenaProductos.add(modelo.getCodigo_sap()+":"+modelo.getCantidadvalidada());
                        almacendestino = modelo.getAlmacendestino();
                        almacenorigen = modelo.getAlmacenorigen();
                        idtraspaso = modelo.getIdtraspaso();
                        idpedidosv = modelo.getIdpedidosv();
                        try {
                            json.put("idtraspasodetalle", String.valueOf(modelo.getIdtraspasodetalle()));
                            iddetalle.add(String.valueOf(json));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

                arregloIds = idpedidosv.split(",");

                if(isChecked){
                    resultado = TextUtils.join(";", cadenaProductos);
                    Log.d("PandP", resultado+";");
                    if(almacendestino.equals("")&& almacenorigen.equals("") && idtraspaso == 0){
                        Toast.makeText(getApplicationContext(), "Intente de nuevo", Toast.LENGTH_LONG).show();
                    }else{
                        for(int i = 0; i<arregloIds.length; i++){

                            try {
                                jsonObject.put("idpedidos", arregloIds[i]);
                                ids.add(String.valueOf(jsonObject));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("json", ""+ids);
                        }
                        Log.d("jsonarray", ""+iddetalle);
                       sendParams(resultado, almacenorigen, almacendestino, String.valueOf(idtraspaso), String.valueOf(ids), String.valueOf(iddetalle));

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "verifica todos los campos", Toast.LENGTH_LONG).show();
                }


                break;
                
                default:

                    break;
        }
    }


    private void sendParams(final String cadena, final String origen, final String destino, final String idtraspaso, final String json, final String jsoniddetalle){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PandP", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("response").equals("OK")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PandP.this);
                        dialog.setTitle("Atención");
                        dialog.setMessage("Se envió correctamente la información.");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(PandP.this, principal.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                        Intent intent = new Intent(PandP.this, principal.class);
                        startActivity(intent);
                    }else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PandP.this);
                        dialog.setTitle("Atención");
                        dialog.setMessage("No se pudo enviar correctamente la información, "+obj.getString("response"));
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        dialog.show();

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
                params.put("opt", "enviarProductoSap");
                params.put("idtraspaso", idtraspaso);
                params.put("cadena", cadena+";");
                params.put("usuario", login.usuario);
                params.put("jsonids", json);
                params.put("jsoniddetalle", jsoniddetalle);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
