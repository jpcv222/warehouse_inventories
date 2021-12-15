package DATOS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import ENTIDAD.Entrada;
import ENTIDAD.Producto;
import ENTIDAD.Usuarios;

import com.example.desarrolloii.inventario.login;

//en esta clase se ejecutan todas las operaciones que tengan que ver con archivos json
public class JSONParser {
    public Usuarios GetUsuario(String Datos){
        Usuarios usuarios = null;
        try{
            JSONArray array = new JSONArray(Datos);
            JSONObject object = array.getJSONObject(0);
            usuarios = new Usuarios(object.getString("Usuario"),object.getString("Contrasena"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return usuarios;
    }

    public List<Producto> ReadJson(String Datos){
        List<Producto> Resultado = new LinkedList<Producto>();
        try {
            JSONArray array = new JSONArray(Datos);
            for (int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                Producto producto =
                        new Producto(obj.getString("Barras"),obj.getString("Descripcion"),obj.getInt("Cantidad"), obj.getString("Fecha"), obj.getInt("conversion"),0,0,"");

                Resultado.add(producto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Resultado;
    }

    public String listtojson(List<Entrada> productos){
        JSONArray array = new JSONArray();
        for (Entrada cod:productos) {
            try {

                JSONObject object = new JSONObject();
                object.put("Codigo", cod.getCodigo());
                object.put("Descripcion", cod.getDescripcion());
                object.put("Cantidad", cod.getCantida());
                object.put("Fecha", cod.GetFecha());
                object.put("Usuario", login.usuario);

                array.put(object);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return array.toString();
    }
}
