package fr.pomp.adfuell.utils.edena.bdd.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by edena on 20/01/2017.
 */

public class TestBddRequest  extends TestBddBase {

    public TestBddRequest(Context context) {
        super(context);
    }

    public long insert(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","Ede");
        open(true);
        long id = insert("contacts",null,contentValues);
        close();
        return id;
    }

    public void select(){
        open(true);
        String sql = "select * from contacts";
        Cursor c = rawQuery(sql,null);
        while(c != null && c.moveToNext()){
            long id =  c.getLong(c.getColumnIndex("id"));
            String name =  c.getString(c.getColumnIndex("name"));
            Log.i("SELECT"," QUERy "+name+" id "+id);
        }

        close();
    }
}
