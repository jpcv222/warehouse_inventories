package DATOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.Nullable;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.desarrolloii.inventario.Registro;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

import ENTIDAD.Entrada;
import ENTIDAD.Producto;
import ENTIDAD.Total;
import com.example.desarrolloii.inventario.MainActivity;

import static com.example.desarrolloii.inventario.MainActivity.codigob;
import static com.example.desarrolloii.inventario.MainActivity.descripcion;
import static com.example.desarrolloii.inventario.MainActivity.nuke;
import static com.example.desarrolloii.inventario.MainActivity.tipo;
import static com.example.desarrolloii.inventario.MainActivity.tipo1;
import static com.example.desarrolloii.inventario.login.setDate;

public class cdDatos extends SQLiteOpenHelper {
    Context context = null;
    String tipoProducto = "";
    LogAndroid log = new LogAndroid();
    public cdDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Producto (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "Codigo TEXT," +
                "Descripcion TEXT," +
                "Capacidad TEXT," +
                "IMAGEN TEXT," +
                "URL TEXT," +
                "codigo_sap TEXT," +
                "cb_tarima TEXT," +
                "cb_caja TEXT," +
                "cb_unidad TEXT," +
                "cb_paquete TEXT," +
                "conversion TEXT," +
                "uenvio float,"+
                "idproducto int,"+
                "idsubproducto int,"+
                "cajastarima int,"+
                "tipo text"+
                ")");

        db.execSQL("CREATE TABLE Entradas(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "Codigo TEXT,"+
                "Cantidad TEXT,"+
                "Fecha TEXT"+
                ")");

        db.execSQL("CREATE TABLE INGRESOS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "Codigo TEXT,"+
                "Descripcion TEXT,"+
                "Cantidad TEXT,"+
                "FECHA TEXT,"+
                "idEntrada TEXT,"+
                "cantidadtarima TEXT,"+
                "cantidadpaquete TEXT,"+
                "cantidadunidad TEXT,"+
                "cantidadcaja TEXT,"+
                "tipo TEXT,"+
                "idregistro TEXT"+
                ")");

        db.execSQL("CREATE TABLE USUARIOS(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                        "Usuario TEXT,"+
                        "PATH TEXT,"+
                        "FECHA DATETIME DEFAULT CURRENT_TIMESTAMP"+
                ")");

        db.execSQL("CREATE TABLE CEDIS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "nombre TEXT,"+
                "idcedis TEXT,"+
                "codigosap TEXT,"+
                "transito_sap TEXT"+
                ")");

    }

    public int IngresarUsu(String usuario,String path){
        int Resultado =0;
        cdDatos admin = new cdDatos(context,"Administracion",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int Result = 0;
        ContentValues values = new ContentValues();
        values.put("Usuario",usuario);
        values.put("PATH",path);
        Long RES = bd.insertOrThrow("USUARIOS",null,values);
        if(RES!=-1) Resultado++;
        return Resultado;
    }

    public int GuardarEntradas(List<Producto> productos){
        int Resultado =0;
        cdDatos admin = new cdDatos(context,"Administracion",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor;



        int Result = 0;
        int cantidad = 0, cantidadtarima = 0, cantidadcaja = 0, cantidadpaquete = 0, cantidadunidad = 0;

        for (Producto product :productos){

                Log.d("validation", "no");
                switch(tipo1){
                    case "1":
                        if(codigob.equalsIgnoreCase("T") || codigob == "T" && tipo1.equalsIgnoreCase("1") || tipo1 == "1"){
                            cantidadtarima = product.getConversion()*5*38*10;
                            cantidad = product.getConversion()*5*38*10;
                        }else if(codigob.equalsIgnoreCase("P") || codigob == "P" && tipo1.equalsIgnoreCase("1") || tipo1 == "1"){
                            cantidadpaquete = product.getConversion();
                        }else if(codigob.equalsIgnoreCase("C") || codigob == "C" && tipo1.equalsIgnoreCase("1") || tipo1 == "1"){
                            //cantidadtarima = product.getConversion()*5*10;
                            cantidadcaja = product.getConversion()*5*10;
                        }else if(tipo1.equalsIgnoreCase("1") || tipo1 == "1" && codigob.equalsIgnoreCase("7") || codigob == "7"){
                            cantidadunidad = product.getConversion()/10;
                        }
                        else{
                            // cantidad = product.getConversion()*product.getCantida();
                        }
                        break;

                    default:

                        if(codigob.equalsIgnoreCase("C") || codigob == "C" && tipo1 != "1"){
                            cantidadcaja = product.getCantida()*product.getConversion();
                        }else{
                            cantidadunidad = product.getConversion();
                        }
                        break;
                }



                ContentValues valores = new ContentValues();
                valores.put("Codigo", product.getCodigo());
                valores.put("Descripcion", product.getDescripcion());
                valores.put("Cantidad", cantidad);
                valores.put("cantidadtarima", cantidadtarima);
                valores.put("cantidadcaja", cantidadcaja);
                valores.put("cantidadpaquete", cantidadpaquete);
                valores.put("cantidadunidad", cantidadunidad);
                valores.put("FECHA", GetFecha());


                try{
                    Long result = bd.insertOrThrow("INGRESOS",null,valores);
                    if(result!=-1)Resultado++;
                }catch (Exception e){
                    e.printStackTrace();
                    log.appendLog(setDate(), ""+e);
                }
                bd.close();

        }

        return Resultado;
    }


    public List<Total> GetTotales(){
        List<Total> Resultado = new LinkedList<Total>();
        List<String> aux= new LinkedList<String>();
        try{
            cdDatos datos = new cdDatos(context,"Administracion",null,1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            String query = "select distinct Descripcion from INGRESOS";
            Cursor fila = bd.rawQuery (query,null);
            int count = fila.getCount();
            if(fila.getCount()>0){
                while (fila.moveToNext()){
                    if(fila.getString(0) !=null){
                        aux.add(fila.getString(0));
                    }
                }
            }
            for (String codigo:aux) {
                Total total = Gettotal(bd,codigo);
                Resultado.add(total);
            }
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
        return Resultado;
    }


    public Total Gettotal(SQLiteDatabase bd, String codio){
        nuke();
        Total Resultado = null;
        String fecha = "2019-09-05 00:00:00";
        int cantidad = 0;
        try{
            String query = "select sum(t0.Cantidad), t0.Descripcion, t1.uenvio, t0.FECHA, t1.URL, t0.idEntrada, t1.conversion, SUM(t0.cantidadtarima), SUM(t0.cantidadcaja), SUM(t0.cantidadpaquete), SUM(t0.cantidadunidad), t1.cajastarima, t1.tipo, t0.Codigo from INGRESOS as t0 left outer join Producto as t1 on t0.Descripcion = t1.Descripcion where t0.Descripcion = '"+codio+"'";
            Cursor fila = bd.rawQuery (query,null);
            Log.d("cursor", "main"+fila);
            int count = fila.getCount();
            if(fila.getCount()>0) {

                int cantidadtarima = 0, cantidadpaquete = 0, cantidadunidad = 0, cantidadcaja = 0;
                //
                /**/

                while (fila.moveToNext()) {

                    Log.d("cursor", "second"+fila);
                    Log.d("fila", ""+fila.getString(12));
                    tipoProducto = fila.getString(12);
                    if (tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto") {
                        Resultado = new Total(codio, fila.getString(1), fila.getInt(0), fila.getInt(2), fecha, fila.getInt(6), fila.getInt(7), fila.getInt(8), fila.getInt(9), fila.getInt(10),fila.getInt(11), fila.getString(12));

                        cantidadtarima = fila.getInt(0);
                        Log.d("gettotal ", "" + cantidadtarima);
                        Resultado.setTotal(cantidadtarima);
                    } else {

                        boolean si = bd.delete("INGRESOS", "Descripcion = '"+codio+"'", null) > 0;

                        if(si){
                            Log.d("eliminado","si"+" "+fila.getString(12)+" "+codio+" "+fila.getString(1));
                        }else{
                            Log.d("eliminado","no"+" "+fila.getString(12)+" "+codio+" "+fila.getString(1));
                        }

                        ContentValues valores = new ContentValues();
                        valores.put("Codigo", codio);
                        valores.put("Descripcion", fila.getString(1));
                        valores.put("Cantidad", fila.getString(0));
                        valores.put("cantidadtarima", fila.getString(7));
                        valores.put("cantidadcaja", fila.getString(8));
                        valores.put("cantidadpaquete", fila.getString(9));
                        valores.put("cantidadunidad", fila.getString(10));
                        valores.put("FECHA", GetFecha());
                        bd.insert("INGRESOS",null,valores);


                        Resultado = new Total(codio, fila.getString(1), fila.getInt(0), fila.getInt(2), fecha, fila.getInt(6), fila.getInt(7), fila.getInt(8),fila.getInt(9), fila.getInt(10),fila.getInt(11), fila.getString(12));

                        switch (tipoProducto) {
                            case "1":
                                if (tipoProducto.equalsIgnoreCase("1")) {

                                    cantidadtarima = Resultado.getCantidadtarima() / fila.getInt(2) / fila.getInt(6);
                                    cantidadpaquete = Resultado.getCantidadpaquete() / 10;
                                    cantidadunidad = Resultado.getCantidadunidad();
                                    cantidadcaja = Resultado.getCantidadcajaP()/Resultado.getUnidadenvio();
                                    Log.d("caja total", "" + cantidadcaja);
                                } else {
                                    //cantidad = fila.getInt(0)*fila.getInt(6);
                                }
                                break;

                            default:

                                if (tipoProducto != "1") {
                                    cantidadtarima = 0;
                                    /*Toast.makeText(context, ""+fila.getInt(2), Toast.LENGTH_LONG).show();
                                    Log.d("cantitadcaja xd",""+cantidadcaja);*/
                                    cantidadcaja = Resultado.getCantidadcajaP()/Resultado.getCantidadcajaP();
                                    cantidadunidad = Resultado.getCantidadunidad();
                                } else {

                                }

                                break;
                        }

                        Log.d("cantidadtarima", "" + cantidadtarima);
                        Log.d("cantidadunidad", "" + cantidadunidad);
                        Log.d("cantidadpaquete", "" + cantidadpaquete);
                        Log.d("cantidadcaja", "" + cantidadcaja);

                        Resultado.setCantidadpaquete(cantidadpaquete);
                        Resultado.setCantidadunidad(cantidadunidad);
                        Resultado.setTotal(cantidadtarima);
                        Resultado.setCantidadcaja(cantidadcaja);
                    }


                    Resultado.setImagen(fila.getString(4));

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);


        }
        return Resultado;
    }

    public void Limpiaratalogo(){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            bd.execSQL("delete from Producto");
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }

    }
    public List<String> GetPaths(){
        List<String> Resultado = new LinkedList<String>();
        try{
            cdDatos datos = new cdDatos(context,"Administracion",null,1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            String query = "select IMAGEN from Producto";
            Cursor fila = bd.rawQuery (query,null);
            int count = fila.getCount();
            if(fila.getCount()>0){
                while (fila.moveToNext()){
                    String obj = fila.getString(0);
                    Resultado.add(obj);
                }
            }
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
        return Resultado;
    }
    public String Getimagen(){
        String Resultado = "";
        try{
            cdDatos datos = new cdDatos(context,"Administracion",null,1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            java.lang.String query = "select URL from Producto ";
            Cursor fila = bd.rawQuery (query,null);
            int count = fila.getCount();
            if(fila.getCount()>0){
                while (fila.moveToNext()){
                    Resultado = fila.getString(0);

                }
            }
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
        return Resultado;
    }
    public void UpdateURLimagenes(String url,String QR){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("URL",url);
            bd.update("Producto",registro,"IMAGEN ='"+QR+"'",null);
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }

    }

    public Producto BuscaProducto(String codigo){
        Producto Resultado = null;
        try{
            cdDatos datos = new cdDatos(context,"Administracion",null,1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            int conversion;
            String query = "select Codigo, Descripcion, uenvio, URL, tipo, conversion, cajastarima from Producto where cb_tarima = '"+codigo+"' or cb_caja = '"+codigo+"' or cb_unidad = '"+codigo+"' or cb_paquete = '"+codigo+"'";
            Cursor fila = bd.rawQuery (query,null);
            int count = fila.getCount();
            if(fila.getCount()>0){
                while (fila.moveToNext()){
                    Log.d("tipo", fila.getString(4));
                    Resultado = new Producto(codigo,fila.getString(1),fila.getInt(2), fila.getString(4), fila.getInt(5),1, fila.getInt(6),"");

                    Resultado.setImagen(fila.getString(3));
                }
            }
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
        return Resultado;
    }


    public String GetFecha(){
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            Date date = new Date();
            return dateFormat.format(date);
        }
    }
    public int IngresarProducto(List<Producto> productos){
        cdDatos admin = new cdDatos(context,"Administracion",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int Result =0;
        for (Producto product: productos) {
            ContentValues valores = new ContentValues();
            valores.put("Codigo",product.getCodigo());
            valores.put("Descripcion",product.getDescripcion());
            valores.put("Capacidad",product.getCantida());
            valores.put("IMAGEN",product.getImagen());
            try {
                Long res = bd.insertOrThrow ("Producto", null, valores);
                if (res != -1) Result++;
            }catch (Exception e){
                e.printStackTrace();
                log.appendLog(setDate(), ""+e);
            }

        }
        bd.close();
        return Result;
    }
    public void BorraEnviados(){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            bd.execSQL("delete from INGRESOS");
            bd.close();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }
    public boolean BorraEntrada(String ID, String unidad, String paquete, String caja, String tarima){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            String sql = "UPDATE INGRESOS SET cantidadunidad = '"+unidad+"', cantidadpaquete = '"+paquete+"', cantidadcaja = '"+caja+"', cantidadtarima = '"+tarima+"', Cantidad = '"+tarima+"' where ID = "+ID+"";
            Log.d("sql", sql);
            bd.execSQL(sql);
            bd.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
            return false;
        }

    }

    public boolean actualizarEntrada(String ID, String cantidad){
        try {
            cdDatos datos = new cdDatos(context, "Administracion", null, 1);
            SQLiteDatabase bd = datos.getWritableDatabase();
            bd.execSQL("UPDATE INGRESOS SET Cantidad = '"+cantidad+"' where ID = "+ID+"");
            bd.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
            return false;
        }

    }


    public List<Entrada> GetDetalleEntrada(String Codigo){
        List<Entrada> Resultado = new LinkedList<Entrada>();
        cdDatos datos = new cdDatos(context,"Administracion",null,1);
        SQLiteDatabase bd = datos.getWritableDatabase();
        String query = "select t0.Codigo, t0.Descripcion, sum(t0.Cantidad), t0.FECHA, t0.ID, t1.URL, t0.idEntrada, t1.conversion, sum(t0.cantidadtarima), sum(t0.cantidadcaja), sum(t0.cantidadpaquete), sum(t0.cantidadunidad), t1.cajastarima, t1.tipo, t1.uenvio from INGRESOS as t0 LEFT OUTER JOIN Producto as t1 on t0.Descripcion = t1.Descripcion where t0.Descripcion ='"+Codigo+"'";
        Cursor fila = bd.rawQuery (query,null);
        int count = fila.getCount();
        int cantidad;
        if(fila.getCount()>0){

            while (fila.moveToNext()){
                Entrada obj = new Entrada(fila.getString(0),fila.getString(1),fila.getInt(2),fila.getString(3), fila.getInt(7), fila.getInt(8), fila.getInt(9), fila.getInt(10), fila.getInt(11), fila.getInt(12), fila.getString(13), fila.getInt(14));
                obj.setID(fila.getInt(4));
                obj.setImagen(fila.getString(5));


                Resultado.add(obj);
            }
        }
        bd.close();
        return Resultado;
    }


    public List<Entrada> GETProductoS(){
        List<Entrada> Resultado = new LinkedList<Entrada>();
        cdDatos datos = new cdDatos(context,"Administracion",null,1);
        SQLiteDatabase bd = datos.getWritableDatabase();
        String query = "select t0.Codigo, t0.Descripcion, t0.Cantidad, t0.FECHA, t1.URL, t0.idEntrada, t0.cantidadtarima, t0.cantidadcaja, t0.cantidadpaquete, t0.cantidadunidad, t1.cajastarima, t1.tipo, t1.uenvio from INGRESOS as t0 LEFT OUTER JOIN Producto as t1 on t0.Codigo = t1.Codigo";
        Cursor fila = bd.rawQuery (query,null);
        int count = fila.getCount();
        if(fila.getCount()>0){
            while (fila.moveToNext()){
                Log.d("tipoEntradaGetProductoS", fila.getString(11));
                Entrada obj = new Entrada(fila.getString(0),fila.getString(1),fila.getInt(2),fila.getString(3),0,fila.getInt(6), fila.getInt(7),fila.getInt(8), fila.getInt(9), fila.getInt(10), fila.getString(11), fila.getInt(12));
                obj.setImagen(fila.getString(4));
                Resultado.add(obj);
            }
        }
        bd.close();
        return Resultado;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
