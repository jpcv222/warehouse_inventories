package com.example.desarrolloii.inventario;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.ChasingDots;


public class ProgressCustom extends DialogFragment implements View.OnClickListener {
    Context context, contex2;
    ProgressBar progressBar;
    String mensaje, circulo = "", triangulo = "", equis = "", tipoInformacion = "", placa = "";
    int validado = 0;
    TextView txtMensaje;
    Button btnSi, btnNo;
    LinearLayout lnBotones;
    Intent intent;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.context = getContext();
        Dialog dialogo = new Dialog(this.context);
        dialogo.setContentView(R.layout.activity_progress_custom);
        dialogo.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMensaje = (TextView)dialogo.findViewById(R.id.txtMensaje);
        btnNo = (Button)dialogo.findViewById(R.id.btnNo);
        btnSi = (Button)dialogo.findViewById(R.id.btnSi);
        lnBotones = (LinearLayout)dialogo.findViewById(R.id.lnBotonesProgress);
        progressBar = (ProgressBar)dialogo.findViewById(R.id.spin_kit);
        btnSi.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        txtMensaje.setText(getMensaje());

        if(getTipoInformacion().equals("actualizar")){
            lnBotones.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            lnBotones.setVisibility(View.GONE);
        }

        ChasingDots chasingDots = new ChasingDots();
        progressBar.setIndeterminateDrawable(chasingDots);

        return dialogo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }


    public void setTipoInformacion(String tipoInformacion) {
        this.tipoInformacion = tipoInformacion;
    }

    public String getTipoInformacion() {
        return tipoInformacion;
    }

    public void setValidado(int validado) {
        this.validado = validado;
    }

    public int getValidado() {
        return validado;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSi:
                dismiss();
                setValidado(2);
                break;

            case R.id.btnNo:

                dismiss();
                setValidado(1);
                break;
        }
    }
}
