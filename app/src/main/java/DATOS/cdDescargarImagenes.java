package DATOS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.net.HttpURLConnection;
import java.net.URL;

import ENTIDAD.URLS;

public class cdDescargarImagenes implements Target {

    private String name = "",qr = "";
    private ImageView imageView= null;
    Context context = null;
    public cdDescargarImagenes(String nombre,Context context){
        this.name = nombre;
        this.context = context;
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        String path = Environment.getExternalStorageDirectory().getPath()+"/TRUSOT/"+name;
        File directori =new File(Environment.getExternalStorageDirectory().getPath()+"/TRUSOT/");
        if(!directori.exists()){
            File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), "TRUSOT");
            nuevaCarpeta.mkdirs();
        }
        File file = new File(path);
        try{
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,75,outputStream);
            outputStream.close();
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            datos.UpdateURLimagenes(path,name);
            //Toast.makeText(context,"imagen guardada"+path,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }



    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
