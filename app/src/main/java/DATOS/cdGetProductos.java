package DATOS;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.desarrolloii.inventario.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ENTIDAD.Producto;
import ENTIDAD.ProductosDescargados;
import ENTIDAD.URLS;

public class cdGetProductos extends AsyncTask<Void,Void,String> {

    Context context = null;
    ProductosDescargados listener = null;
    ImageView boton =  null,boton2 =null,boton3=null;
    ProgressBar barra = null;
    Activity activity = null;
    public  cdGetProductos (Context context, ProductosDescargados listener, Activity activity){
        this.context = context;
        this.listener = listener;
        this.activity = activity;
        boton = this.activity.findViewById(R.id.BTNESCANEAR);
        boton2 = this.activity.findViewById(R.id.BTNENVIAR);
        //boton3 = this.activity.findViewById(R.id.BTNACTUALIZAR);
        //barra = this.activity.findViewById(R.id.progresoprincipal);
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.Descargados(s);
        boton.setVisibility(View.VISIBLE);
        boton2.setVisibility(View.VISIBLE);
       // barra.setVisibility(View.GONE);
    }


    public String GetDatos() throws Exception {
        String Resultado = "";
        URL url = new URL(URLS.GetProductos());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int code = connection.getResponseCode();
        if(code == HttpURLConnection.HTTP_OK){
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
    protected void onPreExecute() {
        super.onPreExecute();
        boton.setVisibility(View.GONE);
        boton2.setVisibility(View.GONE);
//        boton3.setVisibility(View.GONE);
        barra.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        int ingresados =0;

        try{
            result = GetDatos();
            JSONParser parser = new JSONParser();
            List<Producto> productos = parser.ReadJson(result);
            cdDatos datos = new cdDatos(context,"Administracion",null,1);
            if(productos.size()>0) {
                datos.Limpiaratalogo();
                ingresados = datos.IngresarProducto(productos);
            }
            result = "Se Ingresaron "+result+" nuevos productos";
        }catch (Exception e){
            result = e.getMessage();
        }finally {
        }
        return result;
    }
}
