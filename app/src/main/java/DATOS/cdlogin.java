package DATOS;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.desarrolloii.inventario.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import ENTIDAD.Usuarios;
import ENTIDAD.LoginListener;
import ENTIDAD.URLS;

import static com.example.desarrolloii.inventario.MainActivity.nuke;
import static com.example.desarrolloii.inventario.login.setDate;

public class cdlogin extends AsyncTask<Void,Void,String> {

    Context context = null;
    LoginListener listener = null;
    ProgressBar progreso = null;
    String Usuario= "",Contrasena= "";
    LogAndroid log = new LogAndroid();
    public cdlogin(Context context, LoginListener listener, View view, String Usuario, String Contrasena){
        this.context = context;
        this.listener = listener;
        progreso = view.findViewById(R.id.progreso);
        this.Usuario = Usuario;
        this.Contrasena = Contrasena;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progreso.setVisibility(View.GONE);
       // Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progreso.setIndeterminate(true);
        progreso.setVisibility(View.VISIBLE);

    }
    public String EnviarDatos(String Usuario,String Contraseña){
        nuke();
        String Resultado = "";
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        if(IsOnline()) {
            try {

                url = new URL(URLS.Enviarlogin(Usuario, Contraseña));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                int result = httpURLConnection.getResponseCode();
                if (result == HttpURLConnection.HTTP_OK) {
                    Resultado = ReadStream(httpURLConnection.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Resultado = e.getMessage();
                log.appendLog(setDate(), ""+e);
            } finally {

            }
        }else {
            Resultado = "Verifique su Conexion";
        }
        return  Resultado;
    }
    public String ReadStream(InputStream in) throws IOException {
        nuke();
        BufferedReader r = null;
        r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        if(r != null){
            r.close();
        }
        in.close();
        return total.toString();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String Resultado = "";
        try {
            Resultado = EnviarDatos(Usuario, Contrasena);
            if (Resultado.contains("{")) {
                Usuarios usu = new JSONParser().GetUsuario(Resultado);
                listener.Response(usu);
                //Resultado = "Bienvenido...!";
            }
        }catch (Exception e){
            e.printStackTrace();
            Resultado =e.getMessage();
            log.appendLog(setDate(), ""+e);
        }
        return Resultado;
    }


    public boolean IsOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 192.168.18.7");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
        return false;
    }
}
