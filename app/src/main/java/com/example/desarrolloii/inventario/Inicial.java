package com.example.desarrolloii.inventario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static ENTIDAD.URLS.login;

public class Inicial extends AppCompatActivity {

    //esta es la actividad  de inicio y en la cual le login se encima
    //aca abjo se manda a llamar al dialogo del login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        login registro = new login();
        registro.setCancelable(false);
        registro.show(getSupportFragmentManager(),"");


    }
}
