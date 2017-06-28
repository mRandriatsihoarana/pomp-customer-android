package fr.pomp.adfuell.utils.edena.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by edena on 16/01/2017.
 */

public  class EDDaoVideHelper extends SQLiteOpenHelper {

    private static EDDaoVideHelper _instance;
    EDBddListner _listner;
    private String _dbName ;
    private int _dbVersion ;

    private EDDaoVideHelper(Context context,EDBddListner listner, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        this._listner = listner;
        _dbName = dbName;
        _dbVersion = dbVersion;
    }

    public static synchronized EDDaoVideHelper getInstance(Context context,EDBddListner listner, String dbName, int dbVersion) {
        if (true) { _instance = new EDDaoVideHelper(context,listner,dbName,dbVersion); }
        return _instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        _listner.EDBddOnCreated(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if(newVersion > oldVersion){
            _listner.EDBddOnUpdated(db);
       }

    }


}
