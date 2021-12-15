package NEGOCIO;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.desarrolloii.inventario.ListaTotales;
import com.example.desarrolloii.inventario.MainActivity;
import com.example.desarrolloii.inventario.R;
import com.example.desarrolloii.inventario.login;
import com.example.desarrolloii.inventario.principal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import DATOS.JSONParser;
import DATOS.PicassoTrustAll;
import DATOS.cdDatos;
import DATOS.cdDescargarImagenes;
import DATOS.cdGetProductos;
import ENTIDAD.Entrada;
import ENTIDAD.LoginListener;
import ENTIDAD.ProductosDescargados;
import DATOS.cdEnviarProductos;
import ENTIDAD.Producto;
import ENTIDAD.ProductosEnviados;
import ENTIDAD.Total;
import ENTIDAD.URLS;

import static com.example.desarrolloii.inventario.MainActivity.tipo;

/*aqui en este archivo de la caa de negocio se conecta todo lo referente a los productos con la capa
* de datos*/
public class cnProductos implements ProductosDescargados, ProductosEnviados,com.squareup.picasso.Callback {
    Context context = null;
    Activity activity = null;
    AlertDialog.Builder builder;
    int memoria_interna_request = 0;
    String resultado;
    String cedis = null;
    ArrayList<String> arrayList;

    public cnProductos(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void InsertarProductos() {

    }

    public void Actualizar() {
        cdGetProductos hilo = new cdGetProductos(context, this, activity);
        hilo.execute();
    }

    public int GuardarProducto(Producto producto) {
        int resultado = 0;
        cdDatos datos = new cdDatos(context, "Administracion", null, 1);
        List<Producto> lista = new LinkedList<Producto>();
        lista.add(producto);
        resultado = datos.GuardarEntradas(lista);
        return resultado;
    }

    public Producto GetProducto(String qr) {
        Producto Resultado = null;
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            Resultado = datos.BuscaProducto(qr);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
        }
        return Resultado;
    }

    @Override
    public void Descargados(String resultado) {
        //Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            cnProductos productos = new cnProductos(context, activity);
            productos.ChecarpermisosImagenes();
        }
        DescargarImagenes();
        //cdDescargarImagenes imagenes = new cdDescargarImagenes("bahrein.png",context);
        //Picasso.get().load("http://192.168.18.12/pruebas/cajetillas/bahrein.png").into(imagenes);
    }

    public List<Entrada> GetProductos() {
        List<Entrada> Resultado = new LinkedList<Entrada>();
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            Resultado = datos.GETProductoS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Resultado;
    }


    public void EnviarProductos(View view, final String origen, final String nreferencia, final String opt) {
        cdDatos cdDatos = new cdDatos(context, "Administracion", null, 1);
        SQLiteDatabase db = cdDatos.getReadableDatabase();
        JSONObject jsonObject = new JSONObject();
        ArrayList<String> list = new ArrayList<>();

        final String productos;

        String query = "select t0.Codigo,t0.Descripcion, sum(t0.cantidadtarima)+SUM(t0.cantidadpaquete)+SUM(t0.cantidadunidad)+SUM(cantidadcaja),t0.FECHA,t1.URL, t1.codigo_sap, t1.idproducto, t1.idsubproducto, t0.idregistro, t0.Cantidad from INGRESOS as t0 LEFT OUTER JOIN Producto as t1 on t0.Descripcion = t1.Descripcion group by t0.Descripcion";
        Cursor fila = db.rawQuery(query, null);
        int count = fila.getCount();
        if(fila.moveToFirst() == true) {
            do{
                try {
                    //Log.d("idregistro", fila.getString(8));
                    if(tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto"){
                        jsonObject.put("cantidad", fila.getString(9));
                        jsonObject.put("idproducto", fila.getString(6));
                        jsonObject.put("idsubproducto", fila.getString(7));
                        jsonObject.put("idregistro", fila.getString(8));
                    }else{
                        jsonObject.put("cantidad", fila.getString(2));
                        jsonObject.put("idproducto", fila.getString(6));
                        jsonObject.put("idsubproducto", fila.getString(7));
                        jsonObject.put("idregistro", fila.getString(8));
                        //lista.add(fila.getString(5)+":"+fila.getString(2));
                    }
                    list.add(String.valueOf(jsonObject));

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }while (fila.moveToNext());

        }
        //resultado = TextUtils.join(";", lista);


        productos = String.valueOf(list);
        //Toast.makeText(context, ""+productos, Toast.LENGTH_LONG).show();
        db.close();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setMessage("Enviando información");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //https://corpogaranon.fortiddns.com:5827/trusot/app/inventarios/ingresoproducto.php
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/ingresoproducto.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    String Response = obj.getString("message");


                    if(Response.equals("Se inserto la informacion correctamente")){

                        progressDialog.dismiss();
                        BorrarEnviados();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Atención");
                        dialog.setMessage(Response);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(context, principal.class);
                                context.startActivity(intent);
                            }
                        });
                        dialog.show();

                    }else if(Response.equals("Pedido enviado") || Response.equalsIgnoreCase("Pedido enviado")){

                        progressDialog.dismiss();
                        BorrarEnviados();


                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Atención");
                        dialog.setMessage(Response);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(context, principal.class);
                                context.startActivity(intent);
                            }
                        });
                        dialog.show();

                    }else{
                        BorrarEnviados();
                        progressDialog.dismiss();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Atención");
                        dialog.setMessage(Response);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(context, principal.class);
                                context.startActivity(intent);
                            }
                        });
                        dialog.show();

                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();

                    BorrarEnviados();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Atención");
                    dialog.setMessage("Sucedió un problema, intenta de nuevo "+response);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    dialog.show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(context, ""+error, Toast.LENGTH_LONG).show();
                BorrarEnviados();
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Atención");
                dialog.setMessage("Sucedió un problema, intenta de nuevo "+error);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                dialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                    if(opt == "sugerirPedidoCedis" || opt.equals("sugerirPedidoCedis") || opt.equalsIgnoreCase("sugerirPedidoCedis")){
                        params.put("opt", opt);
                        params.put("productos", productos);
                    }else{
                        params.put("nreferencia", nreferencia.toUpperCase());
                        params.put("usuario", login.usuario.toUpperCase());
                        params.put("productos", productos);
                        params.put("origen", origen.toUpperCase());
                        params.put("opt", opt);

                    }



                return params;
            }
        };

       request.setRetryPolicy(new DefaultRetryPolicy(30*1000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       requestQueue.add(request);

    }


    public void BorrarEnviados(){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            datos.BorraEnviados();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void Enviados(String Resultado) {
        //new principal().Esconder(true);
        if((!Resultado.contains("Ocurrio un Error")&&!Resultado.contains("Verifique su conexion"))&&!Resultado.equals("0")){
            BorrarEnviados();
            Toast.makeText(context,"Se Actualizaron "+Resultado+" Nuevos Registros",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,Resultado,Toast.LENGTH_LONG).show();
        }
    }
    public void DescargarImagenes(){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            List<String> Resultado = datos.GetPaths();
            if(Resultado!=null&&Resultado.size()>0){
                for (String uri:Resultado ) {
                    String aru = URLS.GetImagenes(uri);
                    cdDescargarImagenes imagenes = new cdDescargarImagenes(uri,context);
                    PicassoTrustAll.getInstance(context).load(aru).into(imagenes);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<Entrada> GetDetalleEntrada(String qr){
        List<Entrada> Resultado = new LinkedList<Entrada>();
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            Resultado = datos.GetDetalleEntrada(qr);

        }catch (Exception e){
            e.printStackTrace();
        }
        return  Resultado;
    }

    public boolean actualizarEntrada(String id, String cantidad){
        boolean Resultado = false;
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            Resultado = datos.actualizarEntrada(id, cantidad);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Resultado;
    }


   public boolean BorrarEntrada(String id, String unidad, String paquete, String caja, String tarima){
        boolean Resultado = false;
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            Resultado = datos.BorraEntrada(id, unidad, paquete, caja, tarima);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Resultado;
    }

    public List<Total> GetTotales(){
        List<Total> Resultado = new LinkedList<Total>();
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            Resultado = datos.GetTotales();

        }catch (Exception e){
            e.printStackTrace();
        }
        return  Resultado;
    }


    public void ChecarpermisosImagenes(){
        if(ActivityCompat.checkSelfPermission(activity.getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity.getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity.getApplicationContext());
                }

                builder.setTitle("Permiso")
                        .setMessage("Es necesario los Permisos de la camara para leer Codigos de barras")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null).create();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, memoria_interna_request);
            }else {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},memoria_interna_request);
            }
            return;
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }

}
