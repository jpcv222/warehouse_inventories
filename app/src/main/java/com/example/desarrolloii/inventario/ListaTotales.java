package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DATOS.LogAndroid;
import DATOS.cdDatos;
import ENTIDAD.DialogoActualizado;
import ENTIDAD.Entrada;
import ENTIDAD.Total;
import ENTIDAD.Totalesadapter;
import ENTIDAD.URLS;
import NEGOCIO.cnProductos;

import static com.example.desarrolloii.inventario.login.setDate;

//esta clase es la que se encarga de manejar el dialogo de los totales donde se muestra la lista de
//los productos scaneados
public class ListaTotales extends DialogFragment implements AdapterView.OnItemClickListener {
    static Context context = null;
    public static List<Total> totals = null;
    static ListView lista = null;
    View miview = null;
    DialogoActualizado listener = null;
    static TextView aviso = null, txtReferencia, txtOrigen, txtChofer;
    EditText edNreferencia;
    Spinner spProveedores;
    static Spinner spChofer;
    public static Totalesadapter adapter;
    public static String opt = null;
    LogAndroid log = new LogAndroid();


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = getContext();
        setCancelable(false);

        final AlertDialog Dialogo = new AlertDialog.Builder(context).setPositiveButton("Enviar",null).setNegativeButton("Cancelar", null).create();
        cnProductos productos = new cnProductos(context,null);

        LayoutInflater inflater = LayoutInflater.from(context);
        View Dialogview = getActivity().getLayoutInflater().inflate(R.layout.listatotales, null);
        miview = Dialogview;

        aviso = (TextView) Dialogview.findViewById(R.id.avisototales);
        lista = (ListView) Dialogview.findViewById(R.id.listatotales);
        spChofer = (Spinner)Dialogview.findViewById(R.id.spChofer);
        txtChofer = (TextView)Dialogview.findViewById(R.id.TextView);
        totals = productos.GetTotales();

        edNreferencia = (EditText)Dialogview.findViewById(R.id.nreferencia);
        spProveedores = (Spinner) Dialogview.findViewById(R.id.spProveedores);
        txtOrigen = (TextView)Dialogview.findViewById(R.id.origentxt);
        txtReferencia = (TextView)Dialogview.findViewById(R.id.referenciatxt);

        //aqui se valida si la lista de los datos no es vacia si es asi se muestra un letrero
        //indicando que no hay items
        if(totals==null||totals.size()==0){
            lista.setVisibility(View.GONE);
            aviso.setVisibility(View.VISIBLE);
            edNreferencia.setVisibility(View.GONE);
            spProveedores.setVisibility(View.GONE);
            txtOrigen.setVisibility(View.GONE);
            txtReferencia.setVisibility(View.GONE);
            spChofer.setVisibility(View.GONE);
            txtChofer.setVisibility(View.GONE);
        }


        opt = MainActivity.opt;

        if(opt == "sugerirPedidoCedis" || opt.equalsIgnoreCase("sugerirPedidoCedis")){
            edNreferencia.setVisibility(View.GONE);
            spProveedores.setVisibility(View.GONE);
            txtOrigen.setVisibility(View.GONE);
            txtReferencia.setVisibility(View.GONE);
        }

        adapter = new Totalesadapter(context,0,totals);
        lista = (ListView) Dialogview.findViewById(R.id.listatotales);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(this);


        Dialogo.setTitle("Lista de producto");
        Dialogo.setView(Dialogview);
        Dialogo.setCancelable(false);

        Dialogo.show();

        Button button = Dialogo.getButton(Dialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.tipo.equalsIgnoreCase("Salida Producto") || MainActivity.tipo == "Salida Producto"){
                    opt = "sugerirPedidoCedis";
                    cnProductos productos = new cnProductos(getContext(),null);
                    productos.EnviarProductos(miview, spProveedores.getSelectedItem().toString(), edNreferencia.getText().toString(), opt);
                }else{

                    opt = "ingresarProducto";

                    if(edNreferencia.getText().toString() == "" || edNreferencia.getText().toString().equalsIgnoreCase("")){
                        edNreferencia.setError("Campo obligatorio");
                        Toast.makeText(getContext(), "Verifica el campo de No. Referencia", Toast.LENGTH_LONG).show();
                    }else if(spProveedores.getSelectedItem().toString().equals("ELIGE")  || spProveedores.getSelectedItem().toString().equalsIgnoreCase("ELIGE")){
                        //spProveedores.getSelectedItem().toString().setError("Campo obligatorio");
                        Toast.makeText(getContext(), "Verifica el campo de Origen", Toast.LENGTH_LONG).show();
                    }else{
                        cnProductos productos = new cnProductos(getContext(),null);
                        productos.EnviarProductos(miview, spProveedores.getSelectedItem().toString(), edNreferencia.getText().toString(), opt);
                        Dialogo.dismiss();
                        //MostarAlertfialogPROGRESO(getContext());
                    }
                }
            }
        });

        consultaChofer("https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/consultas.php");

        //lista.setScrollingCacheEnabled(true);
        getProveedores();

        return Dialogo;
    }

    public void setOnActualizado(DialogoActualizado listener){
        this.listener = listener;
    }

    public void consultaChofer(String URL){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        final List<String> listachofer = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                try {

                    JSONArray ja = new JSONArray(response);
                    for(int i = 0; i<ja.length(); i++){
                        ja.getString(i);
                        listachofer.add(ja.getString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listachofer);
                    spChofer.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("choferes");
                    dialog.setMessage("JSONException listachofer: "+e);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    log.appendLog(setDate(), "ListaTotales "+e+" response: "+response);
                }catch(Exception e){
                    e.printStackTrace();
                    log.appendLog(setDate(), ""+e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("choferes");
                dialog.setMessage("Error Response"+error);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                log.appendLog(setDate(), ""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "consultaChofer");

                return params;
            }
        };

        queue.add(request);

    }

    //este metodo se ejecuta para recargar la lista cuando se carga la lista de los detalles
    public static void Reload(){
        cnProductos productos = new cnProductos(context,null);
        totals = productos.GetTotales();
        if(totals==null||totals.size()==0){
            lista.setVisibility(View.GONE);
            aviso.setVisibility(View.VISIBLE);
            Log.d("reload","true");
        }else {

            Totalesadapter adapter = new Totalesadapter(context,0,totals);
            lista.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d("reload","false");
        }

    }

    //este metodo se encarga de gestionar los eventos de los items de la lista
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Total total = (Total) parent.getItemAtPosition(position);
        ListaProductos productos = new ListaProductos();

        Bundle args = new Bundle();
        args.putString("Codigo",total.getDescripcion());
        args.putString("origen", spProveedores.getSelectedItem().toString());
        args.putString("nreferencia", edNreferencia.getText().toString());

        productos.setArguments(args);
        productos.setonActualizado(this.listener);
        productos.show(getFragmentManager(), "");
    }

    public void actualizarList(){
        try {
            this.adapter.notifyDataSetChanged();
//            lista.invalidateViews();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }

    }

    private void getProveedores(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, URLS.URLINVENTARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ListTotales", response);
                try {

                    response = response.replace("][",",");
                    JSONArray ja = new JSONArray(response);
                    ArrayList<String> listadProveedores = new ArrayList<>();
                    listadProveedores.add("ELIGE");
                    for(int i = 0; i<ja.length(); i++){
                        listadProveedores.add(ja.getString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listadProveedores);
                    spProveedores.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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
                params.put("opt","getProveedores");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

}
