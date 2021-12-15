package NEGOCIO;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

// en esta clase se validan los permisos de la aplicacion
public class cdMainActivity {
    AlertDialog.Builder builder;
    private static final int MY_CAMERA_REQUEST =100;
    private static final int MY_FOLDER_REQUEST =101;

    public void ChecarPermisos(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity.getApplicationContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity.getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity.getApplicationContext());
                }

                builder.setTitle("Permiso")
                        .setMessage("Es necesario los Permisos de la camara para leer Codigos de barras")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null).create();
                if(ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, MY_CAMERA_REQUEST);
                }else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST);
                }
            }else {
                if(ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, MY_CAMERA_REQUEST);
                }else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST);
                }
            }
            return;
        }
    }

    public void CHECARPERMISOSRED(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity.getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity.getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity.getApplicationContext());
                }

                builder.setTitle("Permiso")
                        .setMessage("Es necesario los Permisos ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null).create();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_FOLDER_REQUEST);
            }else {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},MY_FOLDER_REQUEST);
            }
            return;
        }
    }
}
