package fr.pomp.adfuell.utils.edena.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by edena on 16/01/2017.
 */

public  class  EDDaoExistHelper  extends SQLiteOpenHelper {

    private static EDDaoExistHelper _instance;
    private String _dataBasePath;
    private final Context _context;
    EDBddListner _listner;
    private String _dbName;
    private int _dbVersion;

    public static synchronized EDDaoExistHelper getInstance(Context context,EDBddListner _listner, String dbName, int dbVersion) {
        if (_instance == null) { _instance = new EDDaoExistHelper(context,_listner,dbName,dbVersion); }
        return _instance;
    }

    // Constructeur
    private EDDaoExistHelper(Context context,EDBddListner _listner, String dbName, int dbVersion) {

        super(context, dbName, null, dbVersion);
        this._context = context;
        _dbName = dbName;
        _dbVersion = dbVersion;
        String filesDir = context.getFilesDir().getPath(); // /data/data/com.package.nom/files/
        _dataBasePath = filesDir.substring(0, filesDir.lastIndexOf("/")) + "/databases/"; // /data/data/com.package.nom/databases/
      /*
        _dataBasePath = "/data/data/"
                + context.getApplicationContext().getPackageName() + "/databases/"
                + EDBdd.DATABASE_NAME_TEST;
                */
        // Si la bdd n'existe pas dans le dossier de l'app
        if (!checkDatabase()) {
            // copy db de 'assets' vers DATABASE_PATH
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        // retourne true/false si la bdd existe dans le dossier de l'app
        File dbfile = new File(_dataBasePath + _dbName);
        return dbfile.exists();
    }

    // On copie la base de "assets" vers "/data/data/com.package.nom/databases"
    // ceci est fait au premier lancement de l'application
    private void copyDatabase() {

        final String outFileName = _dataBasePath + _dbName;

        InputStream input;
        try {
            // Ouvre la bdd de 'assets' en lecture
            input = _context.getAssets().open(_dbName);

            // dossier de destination
            File pathFile = new File(_dataBasePath);
            if(!pathFile.exists()) {
                if(!pathFile.mkdirs()) {
                    Toast.makeText(_context, "Erreur : copyDatabase(), pathFile.mkdirs()", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Ouverture en écriture du fichier bdd de destination
            OutputStream output = new FileOutputStream(outFileName);

            // transfert de inputfile vers outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Fermeture
            output.flush();
            output.close();
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(_context, "Erreur : copyDatabase()", Toast.LENGTH_SHORT).show();
        }

        // on greffe le numéro de version
        try{
            SQLiteDatabase checkdb = SQLiteDatabase.openDatabase(_dataBasePath + _dbName, null, SQLiteDatabase.OPEN_READWRITE);
            checkdb.setVersion(_dbVersion);
        }
        catch(SQLiteException e) {
            // bdd n'existe pas
        }

    } // copyDatabase()

    @Override
    public void onCreate(SQLiteDatabase db) {
        _listner.EDBddOnCreated(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion){
            _context.deleteDatabase(_dbName);
            copyDatabase();
            _listner.EDBddOnUpdated(db);
        }
    }


}
