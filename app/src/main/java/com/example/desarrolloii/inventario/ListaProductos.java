package com.example.desarrolloii.inventario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import DATOS.LogAndroid;
import ENTIDAD.Adaptador;
import ENTIDAD.DialogoActualizado;
import ENTIDAD.Entrada;
import ENTIDAD.Producto;
import ENTIDAD.ProductosAdapter;
import ENTIDAD.Total;
import NEGOCIO.cnProductos;

import static com.example.desarrolloii.inventario.ListaTotales.spChofer;
import static com.example.desarrolloii.inventario.login.setDate;

//esta actividad maneja el dialog fragmentt de la lista de productos scanneados
public class ListaProductos extends DialogFragment implements DialogInterface.OnClickListener {
    public static List<Entrada> Entradas = null;
    RecyclerView listView = null;
    View miview = null;
    TextView aviso = null;
    ImageButton boton = null;
    String opt = null;
    String origen, nreferencia;
    Context context;
    static DialogoActualizado listener = null;
    public  static ProductosAdapter adapter = null;
    public static Adaptador adaptador = null;
    public static Dialog Dialogo;
    LogAndroid log = new LogAndroid();
    @Override
    public void onResume() {
        super.onResume();
        Log.d("ListaProductos", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ListaProductos", "onPause");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         context = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Dialogo = new Dialog(context);


        try {
            //aqui se inicializan todas las variables
            String codigo = getArguments().getString("Codigo");
            nreferencia = getArguments().getString("nreferencia");
            origen = getArguments().getString("origen");


            cnProductos productos = new cnProductos(context,null);
            //Entradas = productos.GetProductos();

            //Entradas = productos.GetDetalleEntrada(codigo);
            Entradas = productos.GetDetalleEntrada(codigo);
            adapter = new ProductosAdapter(context,0,Entradas, codigo);

            LayoutInflater inflater = LayoutInflater.from(context);
            View Dialogview = inflater.inflate(R.layout.lista, null);
            miview = Dialogview;

            aviso =(TextView) Dialogview.findViewById(R.id.TXTAVISO);
            listView = (RecyclerView) Dialogview.findViewById(R.id.lista);

            Button btnRegresar = (Button)Dialogview.findViewById(R.id.btn_New);



           // Toast.makeText(getContext(), codigo, Toast.LENGTH_LONG).show();

            //aqui se valida si la lista y si esta vacia muestra un anuncio de que no hay registros
            if(Entradas==null||Entradas.size()==0){
                listView.setVisibility(View.GONE);
                aviso.setVisibility(View.VISIBLE);
            }

            opt = ListaTotales.opt;



            adaptador = new Adaptador(Entradas, getContext());
            listView.setLayoutManager(new LinearLayoutManager(getContext()));

            listView.setAdapter(adaptador);

            Dialogo.setContentView(Dialogview);
            Dialogo.setTitle("Productos");
            Dialogo.setCancelable(false);

            Dialogo.show();



            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogo.dismiss();
                    ListaTotales.adapter.notifyDataSetChanged();
                    actualizarItem();
                }
            });


        }catch (Exception e) {
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }finally {

        }
        return Dialogo;
    }
    public void setonActualizado(DialogoActualizado listener){
        this.listener = listener;
    }
    //este metodo valida todos lo eventos de los botones
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            //aqui se envian los dato de los datos al servidor cuando el boton que se presiona es el de enviar
            cnProductos productos = new cnProductos(getContext(),null);
            productos.EnviarProductos(miview, origen, nreferencia, opt);

        }else{
            dialog.dismiss();
        }
    }
    public static void actualizarItem(){
        try {
            adaptador.notifyDataSetChanged();
            Dialogo.dismiss();
            if(listener!=null)listener.Dialogoactualizado();
        }catch (Exception e){
            e.printStackTrace();

        }

    }






}
