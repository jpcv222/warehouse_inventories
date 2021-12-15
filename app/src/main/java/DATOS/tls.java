package DATOS;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class tls extends AsyncTask<Void,Void,String> {

    Context context = null;
    public tls (Context context){
        this.context = context;
    }
    public String peticion() throws Exception{

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        URL url = new URL("https://corpogaranon.fortiddns.com:666/gps/production/app/consultarusuario.php?usuario=gdl215");
        String Resultado = "";
        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, trustAllCerts, new SecureRandom());

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(ssl.getSocketFactory());
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                String a = "";
                return true;
            }
        });
        int respnsecode = connection.getResponseCode();
        if(respnsecode==HttpURLConnection.HTTP_OK){
            Resultado = ReadStream(connection.getInputStream());
        }
        return Resultado;
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

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context,"Resultado"+s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return peticion();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
