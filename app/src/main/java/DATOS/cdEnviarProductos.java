package DATOS;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.support.annotation.RequiresPermission;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.desarrolloii.inventario.R;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import ENTIDAD.ProductosEnviados;
import ENTIDAD.URLS;

import static com.example.desarrolloii.inventario.login.setDate;

public class cdEnviarProductos extends AsyncTask <Void,Void,String> {
    Context context = null;
    String Datos = "";
    View miview = null;
    ProgressBar barra = null;
    ListView lista = null;
    ProductosEnviados listener = null;
    LogAndroid log = new LogAndroid();
    public cdEnviarProductos(Context context, String Datos, View miview, ProductosEnviados listener){
        this.context = context;
        this.Datos = Datos;
        this.miview = miview;
        this.listener = listener;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //barra.setVisibility(View.GONE);
        //lista.setVisibility(View.VISIBLE);
        listener.Enviados(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //barra = miview.findViewById(R.id.progresoprincipal);
        //lista = miview.findViewById(R.id.listatotales);
        //barra.setVisibility(View.VISIBLE);
        //lista.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String Resultado = "";
        if(IsOnline()) {
            try {
                Resultado = EnviaDatos(this.Datos);
            }catch (Exception e){
                e.printStackTrace();
                Resultado = "Ocurrio un Error "+e.getMessage();
                log.appendLog(setDate(), ""+e);
            }
        }else {
            Resultado = "Verifique su conexion";
        }
        return Resultado;
    }

    public String EnviaDatos(String datos) throws Exception {
        URL url = null;
        String Resultado = "";
        int result=0;
        url = new URL(URLS.ip+URLS.SendProductos);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("POST");
            String urlParameters = "Datos=" + datos ;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData);
            }
            connection.connect();
            result = connection.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                Resultado = ReadStream(connection.getInputStream());
            }
        }catch (Exception e){
            e.printStackTrace();
            Resultado = "Ocurrio un Error "+e.getMessage();
            log.appendLog(setDate(), ""+e);
        }finally {
            connection.disconnect();
        }
        return  Resultado;
    }
    public String ReadStream(InputStream in) throws IOException {
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
    public boolean IsOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 192.168.18.12");

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
