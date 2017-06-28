package fr.pomp.adfuell.utils.edena.bdd;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by edena on 16/01/2017.
 */

public interface EDBddListner {
    public void EDBddOnCreated(SQLiteDatabase db);
    public void EDBddOnUpdated(SQLiteDatabase db);
}
