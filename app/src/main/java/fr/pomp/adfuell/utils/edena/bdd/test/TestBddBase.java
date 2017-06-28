package fr.pomp.adfuell.utils.edena.bdd.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import fr.pomp.adfuell.utils.edena.bdd.EDBdd;

/**
 * Created by edena on 16/01/2017.
 */

public class TestBddBase extends EDBdd  {
    public static final int	DATABASE_VERSION   = 1;
    public static final String DATABASE_NAME   = "dbname.db";//dbname.sqlite
    public static final boolean DATABASE_IS_VIDE  = true;//false s'il existe deja une bdd dans le dossier asset

    @Override
    public void EDBddOnCreated(SQLiteDatabase db) {

       String TABLE_CONTACTS = "contacts";
        String KEY_ID = "id";
        String KEY_NAME = "name";
        String KEY_PH_NO = "number";
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        Log.i("EDBdd","Created : "+TABLE_CONTACTS);
        db.execSQL(CREATE_CONTACTS_TABLE);


    }

    @Override
    public void EDBddOnUpdated(SQLiteDatabase db) {
        db.execSQL("DROP TABLE contacts");
        EDBddOnCreated(db);
    }


    /************************************************/
    public TestBddBase(Context context) {
        super(context ,DATABASE_NAME,DATABASE_VERSION,DATABASE_IS_VIDE);
    }



}
