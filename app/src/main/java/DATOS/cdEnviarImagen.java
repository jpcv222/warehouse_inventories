package DATOS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ENTIDAD.URLS;

public class cdEnviarImagen extends AsyncTask<Void,Void,String> {
    String path = "";
    String USUARIO = "";
    private HttpURLConnection httpConn;
    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";
    public cdEnviarImagen(String path,String usuario){
        this.path= path;
        this.USUARIO = usuario;
    }
    @Override
    protected String doInBackground(Void... voids) {
        try {
           // EnviarImagen(path,USUARIO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public void Sendimage(String url){

        File file = new File(url);
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(URLS.GetSendImage());

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            String result = response.toString();
            //Do something with response...

        } catch (Exception e) {
            // show error
        }
    }

   /* public void EnviarImagen(String path,String Usuario) throws Exception{
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);//compress to which format you want.

        byte [] byte_arr = stream.toByteArray();
        String image_str = ""+Base64.encodeBase64(byte_arr);//encodeBytes(byte_arr);
        URL url = new URL(URLS.GetSendImage());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("POST");
            byte [] postdat1 = Base64.encodeBase64(byte_arr);
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + this.boundary);

            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                os.writeBytes(this.twoHyphens+this.boundary+this.crlf);
                os.writeBytes("Content-Disposition: form-data; name=\"" + "img_upload" + "\"; filename=\"" + "imagen1.jpg" +"\"" + "\r\n");
                os.writeBytes("Content-Type: image/jpeg" + "\r\n");
                os.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
                os.writeBytes("\r\n");
                os.write(byte_arr);
                os.writeBytes("\r\n");
                os.writeBytes("--" + boundary + "--" + "\r\n");
                os.writeBytes(this.twoHyphens+this.boundary+this.crlf);
                os.writeBytes("Content-Disposition: form-data; name=\"" + "Usuario" + "\""+this.crlf);
                os.writeBytes("Content-Type: text/plain; charset=utf-8"+this.crlf);
                os.writeBytes("\r\n");
                os.writeBytes(Usuario);
                os.writeBytes("\r\n");
                os.writeBytes("--" + boundary + "--" + "\r\n");
            }
            connection.connect();
            int result = connection.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                String Result = ReadStream(connection.getInputStream());
                int u =0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }



    }
    */
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

}
