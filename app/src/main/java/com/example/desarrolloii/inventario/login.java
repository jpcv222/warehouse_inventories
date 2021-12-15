package com.example.desarrolloii.inventario;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import DATOS.LogAndroid;
import ENTIDAD.Login;
import es.dmoral.toasty.Toasty;


public class login extends DialogFragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    EditText edUser, edPassword;
    public static String URL = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/inventarios_oi2.php";

    public static String usuario="", password="", idcedis="", nombre="", imei="", tipo="", idsecurity="", idregion="", idcentrocosto="", idsecuritysrvr="";

    Button Aceptar = null,Cancelar = null;
    ProgressBar barra;
    EditText txtusu = null,txtconta = null;
    View miview = null;
    public static String  res;

    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    JSONArray ja4,ja;
    private TelephonyManager mTelephonyManager;
    String IMEI = "";
    JSONArray jsproducto;
    String version = "";
    LogAndroid log = new LogAndroid();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getContext();
        Dialog builder = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View Dialogview = inflater.inflate(R.layout.login,null);
        miview = Dialogview;
        builder.setContentView(R.layout.login);

        ColorDrawable dialogColor = new ColorDrawable(ContextCompat.getColor(context, R.color.gris));
        dialogColor.setAlpha(100);
        builder.getWindow().setBackgroundDrawable(dialogColor);
        builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Aceptar = (Button) builder.findViewById(R.id.BTNINICIAR);
        Cancelar = (Button)builder.findViewById(R.id.BTNCANCEL);
        txtusu = (EditText) builder.findViewById(R.id.TXTUSUARIO);
        txtconta = (EditText) builder.findViewById(R.id.TXTCONTRASENA);
        barra = (ProgressBar) builder.findViewById(R.id.progreso);
        TextView txtVersion = (TextView)builder.findViewById(R.id.textView20);

        txtusu.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
            txtVersion.setText("Versión "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaVersion();
            }
        });

        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int accessFinePermission = getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        int accessCoarsePermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (accessFinePermission == PackageManager.PERMISSION_GRANTED && accessCoarsePermission == PackageManager.PERMISSION_GRANTED) {
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                idsecurity = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            }else{
                getDeviceImei();
            }
            //Toast.makeText(getApplicationContext(), ""+IMEI, Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(perms, permsRequestCode);
        }

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            idsecurity = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        }else{
            getDeviceImei();
        }
        return builder;
    }

    public void consultaUsuario(final String URL, final ProgressCustom dialog) {
        Log.i("url",""+URL+idsecurity);
        nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "Response "+response);
                if(response.isEmpty()){
                    Toasty.error(getActivity(), "Usuario o contraseña incorrectos").show();
                    dialog.dismiss();
                }else{
                    try {
                        if(response.contains("No tiene permiso")){
                            Toasty.error(getActivity(), "No tiene permiso para acceder a esta aplicación").show();
                        }else{
                            if(response.contains("No puede acceder desde otro dispositivo")){
                                Toasty.warning(getActivity(), "No puede acceder desde otro dispositivo o contraseña incorrecta").show();
                            }else{
                                response = response.replace("[[","").replace("]]","");
                                JSONArray ja = new JSONArray(response);

                                Log.d("idsecurity", idsecurity);

                                usuario = ja.getString(0);
                                password = ja.getString(1);
                                idcedis = ja.getString(2);
                                nombre = ja.getString(3);
                                imei = ja.getString(4);
                                tipo = ja.getString(5);
                                idsecuritysrvr = ja.getString(6);
                                idregion = ja.getString(7);
                                idcentrocosto = ja.getString(8);


                                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                    if(idsecurity.equals(idsecuritysrvr)){
                                        if(txtusu.getText().toString().equalsIgnoreCase(usuario)){
                                            Intent i = new Intent(getActivity(), principal.class);
                                            i.putExtra("tipo", ja.getString(5));
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(getContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toasty.warning(getActivity(), "El ID no corresponde con el dispositivo").show();
                                    }
                                }else{
                                    if(IMEI.equals(imei)) {
                                        if ( txtusu.getText().toString().equalsIgnoreCase(usuario)) {
                                            Intent i = new Intent(getActivity(), principal.class);
                                            i.putExtra("tipo", ja.getString(5));
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(getContext(), "Usuario o contraseña incorrecto 3", Toast.LENGTH_LONG).show();
                                        }
                                    } else{
                                        Toast.makeText(getContext(), "El IMEI no corresponde con el equipo", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }


                        }

                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        log.appendLog(setDate(), ""+e+ " response: "+response);
                        dialog.dismiss();
                        Toasty.error(getContext(), "Error no identificado, comunicarse a sistemas").show();

                    }catch (Exception e){
                        e.printStackTrace();
                        log.appendLog(setDate(), ""+e+ " response: "+response);
                        dialog.dismiss();
                        Toasty.error(getContext(), "Error no identificado, comunicarse a sistemas").show();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                log.appendLog(setDate(), ""+error);
                Toasty.error(getContext(), "Error no identificado, comunicarse a sistemas").show();
                error.printStackTrace();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "consultaUsuario");
                params.put("usuario", txtusu.getText().toString().trim());
                params.put("password", txtconta.getText().toString().trim());
                params.put("idsecurity", idsecurity);
                params.put("sdkversion", String.valueOf(android.os.Build.VERSION.SDK_INT));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void validaVersion(){
        final ProgressCustom progressCustom = new ProgressCustom();
        progressCustom.setMensaje("Iniciando sesión...");
        progressCustom.setCancelable(false);
        progressCustom.show(getFragmentManager(), "Login");
        nuke();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray ja = new JSONArray(response);
                    String validacion = ja.getString(0);
                    final String url = ja.getString(1);

                    if(validacion.equals(version)){
                        consultaUsuario(URL, progressCustom);
                    }else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("ATENCIÓN");
                        dialog.setMessage("Debes de actualizar la nueva versión de la aplicación");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                        dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        progressCustom.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressCustom.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressCustom.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opt", "validaVersion");
                params.put("version", version);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10*3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }else{
            getDeviceImei();
        }
    }
    private void getDeviceImei() {

        mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(mTelephonyManager.getDeviceId() != null){
            IMEI = mTelephonyManager.getDeviceId();
        }

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

    public static String setDate() {
        String fechahora = "";
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(" yyyy-MM-dd hh:mm:ss ");
        fechahora = formatter.format(today);

        return fechahora;
    }

}
