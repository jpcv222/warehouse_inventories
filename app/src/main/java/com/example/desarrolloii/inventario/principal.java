package com.example.desarrolloii.inventario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.BitSet;

import DATOS.LogAndroid;
import DATOS.cdDatos;
import DATOS.cdDescargarImagenes;
import ENTIDAD.DialogoActualizado;
import ENTIDAD.Producto;
import NEGOCIO.cnProductos;

import static com.example.desarrolloii.inventario.login.setDate;

public class principal extends AppCompatActivity implements DialogoActualizado {
    ImageView imagen = null;
    ListaTotales productos = null;
    static ImageView escan=null,send=null, salir= null, btnEnviaPedido = null, btnPandP = null,btnVerificar, btnEmplaye, btnConteo;
    static ProgressBar barra = null;
    //este metodo se ejecuta cuando se crea la actividad
    LogAndroid log = new LogAndroid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        escan = (ImageView) findViewById(R.id.BTNESCANEAR);
        send = (ImageView) findViewById(R.id.BTNENVIAR);
        salir = (ImageView)findViewById(R.id.salirApp);
        btnPandP = (ImageView)findViewById(R.id.btnpandp);
        btnVerificar = (ImageView)findViewById(R.id.btnVerificador);
        btnEmplaye = (ImageView)findViewById(R.id.btnEmplaye);
        btnConteo = (ImageView) findViewById(R.id.btnConteo);

        btnConteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(principal.this, ConteoInventario.class);
                startActivity(intent);
            }
        });

        btnEnviaPedido = (ImageView)findViewById(R.id.btnEnvioPedido);

        btnPandP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(principal.this, PandP.class);
                startActivity(i);
            }
        });

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(principal.this, VerificadorPedidos.class);
                startActivity(i);
            }
        });
        btnEmplaye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(principal.this, ProcesoEmplaye.class);
                startActivity(intent);
            }
        });

        btnEnviaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(principal.this, EnviaPedido.class);
                startActivity(i);
            }
        });


        String[] arregloTipo = login.tipo.split("##");

        for(int i = 0; i<arregloTipo.length; i++){
            switch (arregloTipo[i]){
                case "ENVIOPEDIDO":
                    btnEnviaPedido.setVisibility(View.VISIBLE);
                    Log.d("principal", "Si entró");
                    break;
                case "ENTRADA_PRODUCTO":
                    send.setVisibility(View.VISIBLE);
                    break;
                case "SALIDA_PRODUCTO":
                    escan.setVisibility(View.VISIBLE);
                    break;
                case "VERIFICAR_PEDIDO":
                    btnVerificar.setVisibility(View.VISIBLE);
                    break;
                case "EMPLAYE_PEDIDO":
                    btnEmplaye.setVisibility(View.VISIBLE);
                    break;
                case "PANDP":
                    btnPandP.setVisibility(View.VISIBLE);
                    break;
                case "CONTEOINVENTARIO":
                    btnConteo.setVisibility(View.VISIBLE);
                    break;
            }
        }


    }
    //este metodo se encarga de esconder todo para mostrar la barra de progreso

    //este metodo lleva al main activity para scannear los productos
    public void escanear(View view){
        try {
            Intent i = new Intent(view.getContext(), MainActivity.class);
            i.putExtra("tipo", "Ingresar Producto");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            deleteIngreso();
            view.getContext().startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }


    public void escanearSalida(View view){
        try {
            Intent i = new Intent(view.getContext(), MainActivity.class);
            i.putExtra("tipo", "Salida Producto");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            view.getContext().startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }

    //este emetodo es el que recive el evento del boton actualizar y actualiza los datos
    public void Actualizar(){
        cnProductos productos = new cnProductos(getApplicationContext(),this);
        productos.Actualizar();
    }

    //este metodo recibe los eventos del boton de enviar y abre el dialogo de los codigos enviados
    public void Enviar(View view){
        try {
            productos = new ListaTotales();
            productos.setOnActualizado(this);
            productos.show(getSupportFragmentManager(),"");
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }
    //este metodo no se usa no ya no me acuerdo que trataba de hacer
    public void Registro(View view){
        try{
            Intent i = new Intent(view.getContext(), Registro.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            view.getContext().startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void finalizarApp(View view){
        finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    //en este metodo actualiza el dialogo
    @Override
    public void Dialogoactualizado() {
        try{
            productos.Reload();
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }

    public void deleteIngreso(){
        cdDatos cdDatos = new cdDatos(getApplicationContext(), "Administracion", null,1);
        SQLiteDatabase db = cdDatos.getReadableDatabase();

        String sql = "DELETE FROM INGRESOS";
        db.execSQL(sql);
        db.close();
    }
}
