package fr.pomp.adfuell.utils.edena.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


/**
 * Created by edena on 16/01/2017.
 */

public class EDBdd implements  EDBddListner{


    public SQLiteOpenHelper _helper;
    protected SQLiteDatabase _bdd;

    @Override
    public void EDBddOnCreated(SQLiteDatabase db) {
/*
       String TABLE_CONTACTS = "contacts";
        String KEY_ID = "id";
        String KEY_NAME = "name";
        String KEY_PH_NO = "number";
        String CREATE_CONTACTS_TABLE = "CREATE TABLE `" + TABLE_CONTACTS + "` ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        */

    }

    @Override
    public void EDBddOnUpdated(SQLiteDatabase db) {

       /* db.execSQL("DROP TABLE contacts");
        EDBddOnCreated(db);
        */
    }

   public EDBdd(Context context, String dbName, int dbVersion, boolean isVideBdd){

       if(isVideBdd){
           _helper = EDDaoVideHelper.getInstance(context,this,dbName,dbVersion);
       }else{
           _helper = EDDaoExistHelper.getInstance(context,this,dbName,dbVersion);
       }
   }
    /*
         * Open bdd, if 'true' is specified bdd will be open in read only mode otherwise bdd willl be open in read/write mode
         * return true if bdd is open otherwise return false.
         */
    public boolean open(boolean readOnly) {
        boolean isOpen;
        //
        isOpen = false;
        try {
            synchronized (this) {
                if (readOnly) {
                    if (this._bdd != null && this._bdd.isOpen() && !this._bdd.isReadOnly()) {
                        this._bdd.close();
                    }
                    if (this._bdd == null || !this._bdd.isOpen()) {
                        this._bdd = this._helper.getReadableDatabase();
                    }
                }
                else {
                    if (this._bdd != null && this._bdd.isOpen() && this._bdd.isReadOnly()) {
                        this._bdd.close();
                    }
                    if (this._bdd == null || !this._bdd.isOpen()) {
                        this._bdd = this._helper.getWritableDatabase();
                    }
                }
                isOpen = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        //
        return isOpen;
    }

    /*
     * Close bdd if this one is open.
     */
    public void close() {
        if (this._bdd != null) {
            this._bdd.close();
        }
    }

    /*
     * Start a transaction.
     */
    public void beginTransactionNonExclusive() {
        if (this._bdd != null) {
            this._bdd.beginTransaction();
        }
    }

    /*
     * Marks the current transaction as successful.
     */
    public void commit() {
        if (this._bdd != null) {
            this._bdd.setTransactionSuccessful();
        }
    }

    /*
     * End a transaction.
     */
    public void endTransaction() {
        if (this._bdd != null) {
            this._bdd.endTransaction();
        }
    }
    public Cursor rawQuery(String sql, String selectionArg[]) {
        if (this._bdd != null) {
            return this._bdd.rawQuery(sql,selectionArg);
        }
        return null;
    }

    /*
     * Compiles an SQL statement into a reusable pre-compiled statement object.
     */
    public SQLiteStatement compileStatement(String sql) {
        if (this._bdd != null) {
            return this._bdd.compileStatement(sql);
        }
        return null;
    }

    /*
     * Update specified table with specified value and where clause or where args
     * return number of row updated (return -1 if an error is raised).
     */
    protected int update(String tableName, ContentValues contentValues, String whereClause, String[] whereArgs) {
        int rowUpdated;
        //
        rowUpdated = -1;
        if (isBddIsOpenAndWritable()) {
            try {
                rowUpdated = this._bdd.update(tableName, contentValues, whereClause, whereArgs);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //
        return rowUpdated;
    }

    /*
     * Insert data in specified table and return id of inserted row (return -1 if insert failed)
     */
    protected long insert(String tableName, String nullColumnHack, ContentValues values) {
        long rowId;
        //
        rowId = -1;
        if (isBddIsOpenAndWritable()) {
            try {
                rowId = this._bdd.insert(tableName, nullColumnHack, values);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //
        return rowId;
    }

    /*
     * Delete specific line(s) of specific table (return -1 if an error is raised)
     */
    protected int delete(String tableName, String whereClause, String[] whereArgs) {
        int rowDeleted;
        //
        rowDeleted = -1;
        if (isBddIsOpenAndWritable()) {
            try {
                rowDeleted = this._bdd.delete(tableName, whereClause, whereArgs);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //
        return rowDeleted;
    }

    /*
     * Drop specified table if this exist
     * return true if drop request success otherwise return false
     */
    protected boolean drop(String table) {
        boolean dropSuccess;
        String request;
        //
        dropSuccess = false;
        request = "DROP TABLE " + table + ";";
        if (isBddIsOpenAndWritable()) {
            try {
                this._bdd.execSQL(request);
                dropSuccess = true;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //
        return dropSuccess;
    }

    /*
     * Check if bdd is open and is not in read only mode
     */
    private boolean isBddIsOpenAndWritable() {
        boolean bddIsOpenAndWritable;
        //
        bddIsOpenAndWritable = false;
        if (this._bdd != null && this._bdd.isOpen() && !this._bdd.isReadOnly()) {
            bddIsOpenAndWritable = true;
        }
        //
        return bddIsOpenAndWritable;
    }

    protected String getSortType(EDDaoSort sortType) {
        String result;
        //
        switch (sortType) {
            case ASC:
                result = "ASC";
                break;
            case DESC:
                result = "DESC";
                break;
            default:
                result = "";
                break;
        }
        //
        return result;
    }



    public enum EDDaoSort {
        ASC,
        DESC,
        NONE
    }
}
