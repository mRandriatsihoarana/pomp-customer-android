package fr.pomp.adfuell.utils.edena.bdd;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by edena on 16/01/2017.
 */

public class EDDaoUtils {

    /**
     * Run a command line in file
     *
     * @param cxt
     * @param filepath path of file in asset ex:bdd/create.sql
     * @param db
     */
    public static void executeSqlCommandesFromFile(Context cxt, String filepath,SQLiteDatabase db){
        String[] commands = getSqlCommandsFromFile(cxt,filepath);
        for (String cmd : commands) {
            try {
                if (!cmd.startsWith("/*") && !cmd.isEmpty() && !cmd.startsWith("\r")) {
                    db.execSQL(cmd);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] getSqlCommandsFromFile(Context cxt, String filepath) {
        String[] commands = null;
        //
        try {
            InputStream stream = cxt.getAssets().open(filepath, AssetManager.ACCESS_BUFFER);
            byte[] buffer;
            int streamLength = 0;
            String data = "";
            do {
                streamLength = stream.available();
                buffer = new byte[streamLength];
                stream.read(buffer, 0, buffer.length);
                data += new String(buffer);
            } while (streamLength > 0);
            commands = data.split("\r\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //
        return commands;
    }



}
