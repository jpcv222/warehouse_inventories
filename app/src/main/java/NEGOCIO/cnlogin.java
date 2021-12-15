package NEGOCIO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.desarrolloii.inventario.MainActivity;
import com.example.desarrolloii.inventario.PhotoTakingService;
import com.example.desarrolloii.inventario.R;

import DATOS.cdlogin;
import ENTIDAD.*;
import com.example.desarrolloii.inventario.principal;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//este archivo se encarga de conectar la capa de datos con la de entidad con los datos del login
public class cnlogin implements LoginListener  {
    Context context = null;
    Activity activity = null;
    public  cnlogin(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }
    public void EnviarDatos(View view, String Usuario, String contrasena){
        cdlogin login = new cdlogin(view.getContext(),this,view,Usuario,contrasena);

        login.execute();
    }
    public void Limpiar(){
        try {
            activity.findViewById(R.id.TXTCONTRASENA).setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void Response(Usuarios usuario) {
        try {
            Intent i = new Intent(context,principal.class);
            Intent camara = new Intent(context,PhotoTakingService.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
