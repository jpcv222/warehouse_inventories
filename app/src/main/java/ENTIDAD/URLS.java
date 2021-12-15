package ENTIDAD;
//en esta clase se guardan todas las url de los web service

public class URLS {
    public static String ip = "https://corpogaranon.fortiddns.com:666/";
    public static String login = "trusot/app/inventarios/Login2.php?";
    public static String productos = "trusot/app/inventarios/GetProductos.php";
    public static String SendProductos = "trusot/app/inventarios/GuardarProductos.php";
    public static String SendImagen = "trusot/app/inventarios/SavedPictures.php";
    public static String GetImagen = "trusot/app/inventarios/Cajetillas/";
    public static String URLINVENTARIOS = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/inventarios_almacen.php";
    //public static String URLINVENTARIOS = "http://192.168.100.34:8080/app/inventarios/inventarios_almacen.php";

    public static String Enviarlogin(String Usuario, String Contraseña){
        return  ip+login+"usuario="+Usuario+"&password="+Contraseña;
    }

    public static String GetProductos(){
        return ip+productos;
    }
    public static String SenProductos(String datos){return  ip+SendProductos+"Datos="+datos;}
    public static String GetImagenes(String url){return ip+GetImagen+url;}
    public static String GetSendImage(){return ip+SendImagen;}
}
