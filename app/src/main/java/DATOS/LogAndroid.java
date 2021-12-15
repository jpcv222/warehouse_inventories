package DATOS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IT on 23/09/2019.
 */

public class LogAndroid {

    public void appendLog(String fecha, String error){
        File logFile = new File("sdcard/log.txt");
        if (!logFile.exists()){
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try{
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(fecha+" "+error);
            buf.newLine();
            buf.close();
        }
        catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
