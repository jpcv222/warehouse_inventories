package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import DATOS.LogAndroid;
import DATOS.PicassoTrustAll;
import DATOS.cdDatos;
import ENTIDAD.DialogoActualizado;
import ENTIDAD.Producto;
import NEGOCIO.cnProductos;

import NEGOCIO.cdMainActivity;

import static com.example.desarrolloii.inventario.login.setDate;


//esta es la clase que maneja la actividad donde se scannean los codigos qr
public class MainActivity extends AppCompatActivity implements Detector.Processor<Barcode>, SurfaceHolder.Callback,ActivityCompat.OnRequestPermissionsResultCallback, DialogoActualizado {

    CameraSource camara = null;
    SurfaceView ContentCamara = null;
    cdMainActivity layer = null;
    TextView Descripcion = null, CANTIDAD = null, txtTipo=null, txtCantidad, txtDescripcion;
    ImageView cajetilla = null;
    boolean Enviado = false;
    static Producto Actual = null;
    public static Spinner spCedis;
    List<String> listaCedis;
    JSONArray jsonArray;
    public static String opt;
    public static String tipo, cedis;
    Barcode codigobarras;
    JSONArray js, jsproducto;
    ListaTotales productos = null;
    Button btnValidar, btnGuardar, btnLimpiar;
    public static String codigob = "", tipo1, descripcion;
    public static int cantidad, conversion, position;
    LogAndroid log = new LogAndroid();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //en este metodo se inicializa la camara y todos los itemas de la pantalla de inicio
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        ImageView logo = new ImageView(getBaseContext());
        Descripcion = (TextView) findViewById(R.id.TXTDescripcion);
        CANTIDAD = (TextView) findViewById(R.id.TXTCANTIDAD);
        ContentCamara = (SurfaceView) findViewById(R.id.ContenedorCamara);
        txtTipo = (TextView)findViewById(R.id.textView4);
        txtCantidad = (TextView)findViewById(R.id.textView9);
        txtDescripcion = (TextView)findViewById(R.id.textView7);
        cajetilla = (ImageView)findViewById(R.id.imageView2);
        btnValidar = (Button)findViewById(R.id.btnValidar);
        btnGuardar = (Button)findViewById(R.id.BTNCARGARQR);
        btnLimpiar = (Button)findViewById(R.id.button5);



        consultaChofer("https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/ingresoproducto.php");

        //se iniciliza la variable cdMaiactivity que es la clase intermedia donde se haran las peticiones
        //para obtener datos
        layer = new cdMainActivity();
        /*esta es una interface para avisar cuando se hallan aceptados los permisis
        * que se necesitan*/
        layer.ChecarPermisos(this);
        /*aqui se inicializa el objeto barcodetecto que es para detectar los codigos de barras*/
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE | Barcode.CODE_128 | Barcode.EAN_13 | Barcode.EAN_8 | Barcode.ALL_FORMATS)
                .build();
        barcodeDetector.setProcessor(this);
        /*aqui se inicializa la camara y se le pasa como parametros el barcodetector*/
        camara = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        Bundle b = new Bundle();
        b = getIntent().getExtras();

        tipo = b.getString("tipo");

        txtTipo.setText(tipo);

        if(tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto"){

            btnValidar.setVisibility(View.GONE);
            Descripcion.setVisibility(View.GONE);
            CANTIDAD.setVisibility(View.GONE);
            txtDescripcion.setVisibility(View.GONE);
            txtCantidad.setVisibility(View.GONE);
            ContentCamara.getHolder().addCallback(this);
            btnGuardar.setVisibility(View.GONE);

            cajetilla.setImageResource(R.drawable.ic_trusot);

        }

        ContentCamara.getHolder().addCallback(this);
        eliminarProducto();
        consultaProducto("https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/GetProductos.php");

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Enviar();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardar
                if(Enviado == false&&Actual!=null){

                    cnProductos datos = new cnProductos(v.getContext() ,MainActivity.this);
                    int result =datos.GuardarProducto(Actual);
                    if(result!=-1) {
                       // Toast.makeText(v.getContext(), "Datos Guardados", Toast.LENGTH_SHORT).show();
                        try {
                            Descripcion.setText("------");
                            CANTIDAD.setText("------");

                            //cajetilla.setImageBitmap(null);
                            Actual = null;
                            Enviado = false;
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                return;
                            }
                            camara.start(ContentCamara.getHolder());
                        }catch (Exception e){
                            //Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    Enviado = true;
                    Log.d("STRING", String.valueOf(Actual));
                    Limpiar(v);
                }else if(Actual==null){
                    Descripcion.setError("Escanee un código de barras");
                }
            }
        });


    }

    //este metodon se manda a llamar cuando los permisos de camara son aceptado
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();
                    if (camara != null) {
                        try {
                            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            //aqui se inicializa la camara
                            camara.start(ContentCamara.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //
                } else {

                }
                return;
            }
        }
    }

    //aqui se guarda el registro cuando se scannea un codigo qr
    public void Guardar(View view) {
        //guardar
        if(Enviado == false&&Actual!=null){

            cnProductos datos = new cnProductos(view.getContext(),this);
            int result =datos.GuardarProducto(Actual);
            if(result!=-1) {
                //Toast.makeText(view.getContext(), "Datos Guardados", Toast.LENGTH_SHORT).show();
                try {
                    Descripcion.setText("------");
                    CANTIDAD.setText("------");

//            cajetilla.setImageBitmap(null);
                    Actual = null;
                    Enviado = false;
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    camara.start(ContentCamara.getHolder());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Enviado = true;
            Log.d("STRING", String.valueOf(Actual));
        }else if(Actual==null){

            Descripcion.setError("Escanee un código de barras");

        }
    }

    @Override
    public void release() {

    }
    //este metodo se usa para limpiar los componentes y dejas las variables en 0
    public void Limpiar(View view) {
        try {

            if(tipo == "Salida Producto" || tipo.equalsIgnoreCase("Salida Producto")){
                Descripcion.setText("------");
                CANTIDAD.setText("------");
                borrarSalida();
//            cajetilla.setImageBitmap(null);
                Actual = null;
                Enviado = false;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                camara.start(ContentCamara.getHolder());

            }else{
                Descripcion.setText("------");
                CANTIDAD.setText("------");
                cajetilla.setImageResource(R.drawable.ic_trusot);

//            cajetilla.setImageBitmap(null);
                Actual = null;
                Enviado = false;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                camara.start(ContentCamara.getHolder());
            }
            Descripcion.setText("------");
            CANTIDAD.setText("------");

//            cajetilla.setImageBitmap(null);
            Actual = null;
            Enviado = false;
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
           // Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
    //este metodo se llama cuando la camara detecta un qr
    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray codigosdetect = detections.getDetectedItems();
        if(codigosdetect.size()>0){
            codigobarras = (Barcode) codigosdetect.valueAt(0);

            //se obtien el codigo de barras detectado
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Descripcion.setError(null);
                    camara.stop();

                    //se reproduce el sonido del beep
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.beep1);
                    mp.start();
                    //aqui se busca los datos del producto
                    cnProductos product = new cnProductos(getApplicationContext(),null);
                    //aqui se obtiene las descripcion del producto
                    Producto producto = product.GetProducto(codigobarras.rawValue.replace("\u001D",""));

                    //consultaPedido("http://192.168.0.49/app/consultaentradas.php?npedido="+codigobarras.rawValue);
                    if(tipo.equalsIgnoreCase("Salida Producto")){
                        try {
                            String codigo = codigobarras.rawValue;
                            if(codigo.contains(":")){
                                String[] separate = codigo.split(":");
                                cedis = separate[0].trim();
                                String npedido = separate[1].trim();
                                //https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/consultaentradas.php?npedido=
                                consultaPedido("https://corpogaranon.fortiddns.com:666/trusot/app/consultaentradas.php?npedido=" + cedis);
                            }else{
                               AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                               dialog.setTitle("Error");
                               dialog.setMessage("No puedes dar salida a un código que no corresponde al formato, intente de nuevo");

                               dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                   }
                               });
                               dialog.show();
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                           // Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
                        }

                    }else{
                        //Log.d("string", ""+producto.getCodigo().substring(0,1)+" tipo: "+producto.getTipo());
                        try {
                            codigob = null;
                            tipo1 = null;
                            cantidad = 0;
                            conversion = 0;
                            descripcion = null;
                            position = 0;

                            codigob = producto.getCodigo().substring(0, 1);
                            tipo1 = String.valueOf(producto.getTipo());
                            cantidad = producto.getCantida();
                            conversion = producto.getConversion();
                            descripcion = producto.getDescripcion();
                            position = Integer.valueOf(producto.getTipo());

                           // Toast.makeText(getApplicationContext(), "Conversión: " + conversion + " position: " + position + " codigo: " + codigob, Toast.LENGTH_LONG).show();

                            if (producto != null) {

                                if (codigob.equalsIgnoreCase("C") || codigob == "C") {
                                    if (tipo1.equalsIgnoreCase("1") || tipo1 == "1") {
                                        CANTIDAD.setText("" + producto.getCantida() + " Paquetes (Cajas: " + producto.getCantida() / producto.getCantida() + ")");
                                        Log.d("count", "1");
                                    } else {
                                        CANTIDAD.setText("" + producto.getCantida() / producto.getCantida() + " Caja");
                                        Log.d("count", "2");
                                    }

                                } else if (codigob.equalsIgnoreCase("7") || codigob == "7" && tipo1.equalsIgnoreCase("1") || tipo1 == "1") {

                                    if(Double.parseDouble(String.valueOf((producto.getConversion() / producto.getConversion()) / producto.getConversion())) <= 0.0 && tipo1.equalsIgnoreCase("1")) {

                                        CANTIDAD.setText("" + Double.parseDouble(String.valueOf((producto.getConversion() / producto.getConversion())))+" Cajetilla");
                                    }else{
                                        CANTIDAD.setText("" + Double.parseDouble(String.valueOf((producto.getConversion())))+" Unidades/Piezas");
                                    }
                                    Log.d("count", "3");
                                } else if (codigob.equalsIgnoreCase("T") || codigob == "T" && tipo1.equalsIgnoreCase("1") || tipo1 == "1") {
                                    CANTIDAD.setText("" + producto.getCantida() * 38 + " Paquetes (Cajas: " + producto.getCantida() * 38 / 50 + ")");
                                    Log.d("count", "4");
                                } else if (codigob.equalsIgnoreCase("P") || codigob == "P" && tipo1.equalsIgnoreCase("1") || tipo1 == "1") {
                                    CANTIDAD.setText("" + producto.getConversion() / 10 + " Paquete");
                                    Log.d("count", "5");
                                } else {
                                    CANTIDAD.setText("" + producto.getConversion());
                                    Log.d("count", "6");
                                }
                                //Toast.makeText(getApplicationContext(), producto.getCodigo(), Toast.LENGTH_LONG).show();
                                Descripcion.setText(producto.getDescripcion());


                                String path = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/" + producto.getDescripcion() + ".jpg";
                                try {
                                    //PicassoTrustAll.getInstance(MainActivity.this).load(path).into(cajetilla);
                                    Glide.with(getApplicationContext()).load(path).override(200,200).fitCenter().error(R.drawable.icon_trusot).into(cajetilla);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("exceptionpicasso", "" + e);
                                   // Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                                }

                                Actual = producto;

                            } else {
                               // Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(), ""+e+codigobarras.rawValue, Toast.LENGTH_LONG).show();
                        }
                    }


                }
            });
        }
    }
    //este metodo se llama cuando el surfaceview se crea aqui se inicia la camara
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            //aqui se inicializa la camara
            camara.start(ContentCamara.getHolder());
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camara.stop();
    }

    public String GetFecha(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void consultaPedido(String URL){
        nuke();
        cdDatos admin = new cdDatos(getBaseContext(),"Administracion",null,1);
        final SQLiteDatabase bd = admin.getWritableDatabase();
        Log.d("URL", URL);
        try{
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Por favor espere");
            dialog.setMessage("Cargando productos...");
            dialog.show();

            final RequestQueue queue = Volley.newRequestQueue(getBaseContext());

            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    response = response.replace("][", ",");
                    if (response.length() >= 0) {
                        try {
                             js = new JSONArray(response);

                             for(int i = 0; i<js.length(); i+=12){
                                 ContentValues cv = new ContentValues();

                                 cv.put("Descripcion", js.getString(i+1));

                                 if(js.getString(i+4) == "0" || js.getString(i+4).equalsIgnoreCase("0") || js.getString(i+4) == null){

                                 }else{
                                     cv.put("Cantidad", js.getString(i+4));
                                 }

                                 cv.put("Codigo", js.getString(i+5));
                                 cv.put("idregistro", js.getString(i+7));
                                 cv.put("cantidadcaja",js.getInt(i+4)/js.getInt(i+8)/js.getInt(i+9));
                                 cv.put("cantidadpaquete", js.getInt(i+4)/js.getInt(i+8));
                                 cv.put("tipo", js.getInt(i+11));


                                 bd.insertOrThrow("INGRESOS",null, cv);
                             }


                            bd.close();
                            //Toast.makeText(getApplicationContext(), "Datos insertados", Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            Log.d("response", response);
                        } catch (JSONException e) {
                           // Toast.makeText(getApplicationContext(), ""+e+" response: "+response, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            log.appendLog(setDate(), ""+e);
                        }
                    }
                    dialog.dismiss();
                        Enviar();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   dialog.dismiss();
                   error.printStackTrace();
                   //Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_LONG).show();
                }
            });



            queue.add(stringRequest);

        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
            //Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
        }


    }

    public void Enviar(){
        try {
            if(tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto"){
                opt = "sugerirPedidoCedis";
            }else{
                opt = "ingresarProducto";
            }


            productos = new ListaTotales();
            productos.setOnActualizado(this);
            productos.show(getSupportFragmentManager(),"");
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }

    @Override
    public void Dialogoactualizado() {
        try{
            productos.Reload();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }


    public void consultaProducto(String URL){
        nuke();

        cdDatos admin = new cdDatos(getBaseContext(),"Administracion",null,1);
        final SQLiteDatabase bd = admin.getWritableDatabase();

        try{
            nuke();


            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Por favor espere");
            dialog.setMessage("Cargando productos...");
            dialog.setCancelable(false);
            dialog.show();

            final RequestQueue queue = Volley.newRequestQueue(getBaseContext());

            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    response = response.replace("][", ",");
                    if (response.length() >= 0) {
                        try {
                            jsproducto = new JSONArray(response);

                            for(int i = 0; i<jsproducto.length(); i+=12){
                                ContentValues cv = new ContentValues();

                                cv.put("Descripcion", jsproducto.getString(i));
                                cv.put("codigo_sap",jsproducto.getString(i+1));
                                cv.put("conversion",jsproducto.getString(i+2));
                                cv.put("uenvio", jsproducto.getInt(i+3));
                                cv.put("cb_tarima",jsproducto.getString(i+4));
                                cv.put("cb_caja",jsproducto.getString(i+5));
                                cv.put("cb_unidad",jsproducto.getString(i+6));
                                cv.put("idproducto", jsproducto.getString(i+7));
                                cv.put("idsubproducto", jsproducto.getString(i+8));
                                cv.put("tipo", jsproducto.getString(i+9));
                                cv.put("cb_paquete", jsproducto.getString(i+10));
                                cv.put("cajastarima", jsproducto.getString(i+11));

                                bd.insertOrThrow("Producto",null, cv);
                            }

                            bd.close();
                            Log.d("response", response);
                        } catch (JSONException e) {
                            dialog.dismiss();
                            log.appendLog(setDate(), ""+e);
                            //Toast.makeText(getApplicationContext(), ""+e+" response: "+response, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                                dialog.dismiss();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    dialog.dismiss();
                    log.appendLog(setDate(), ""+error);
                    //Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_LONG).show();

                }
            });



            queue.add(stringRequest);

        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
           // Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();

            //Toast.makeText(getBaseContext(), ""+e, Toast.LENGTH_LONG).show();
        }

    }

    public void eliminarProducto(){
        cdDatos admin = new cdDatos(getBaseContext(),"Administracion",null,1);
        final SQLiteDatabase bd = admin.getWritableDatabase();

        String SQL = "DELETE FROM Producto";
        bd.execSQL(SQL);
        bd.close();

    }

    public static void nuke() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {

        }


    }

    public void borrarSalida(){
        cdDatos admin = new cdDatos(getBaseContext(), "Administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String sql = "DELETE FROM INGRESOS";
        bd.execSQL(sql);
        bd.close();
    }

    public void consultaChofer(String URL){

        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        final ArrayList<String> listachofer = new ArrayList<>();
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


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), ""+e+ "response: "+response, Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_LONG).show();
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


    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.d("exceptiondrawable", ""+e);

            return null;
        }
    }

}
