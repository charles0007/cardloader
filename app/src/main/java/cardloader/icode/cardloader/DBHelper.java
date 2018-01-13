package cardloader.icode.cardloader;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBCardLoader.db";

    public static final String CARD_TABLE_NAME = "cardloader";
    public static final String COLUMN_USERNAME = "network";
    public static final String COLUMN_NETWORK_CODE = "networkcode";
    public static final String COLUMN_NETWORK = "network";
    public static final String COLUMN_NETWORK_NAME = "networkname";
    public static final String COLUMN_ID = "id";

    private HashMap hp;



    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE " + CARD_TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NETWORK + " TEXT NOT NULL, " +
                        COLUMN_NETWORK_NAME + " TEXT NOT NULL, " +
                        COLUMN_NETWORK_CODE + " TEXT NOT NULL); "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+CARD_TABLE_NAME);
        onCreate(db);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CARD_TABLE_NAME);
        return numRows;
    }

    public int numberOfRowsByNetwork(String network){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = 0;
        Cursor res =  db.rawQuery( "select * from "+CARD_TABLE_NAME+" where "+COLUMN_NETWORK+" = '"+network+"' ", null );
        while(res.isAfterLast() == false){
            res.moveToNext();
            numRows=numRows+1;
        }
        return numRows;
    }

    public boolean insertNetwork ( String network,String networkname, String networkCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put("username", username);
        contentValues.put(COLUMN_NETWORK, network);
        contentValues.put(COLUMN_NETWORK_NAME, networkname);
        contentValues.put(COLUMN_NETWORK_CODE, networkCode);
        db.insert(CARD_TABLE_NAME, null, contentValues);
        return true;
    }

    public String getDataById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CARD_TABLE_NAME+" where id="+id+"", null );
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COLUMN_NETWORK_CODE));

    }



    public String getByNetwork(String network){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CARD_TABLE_NAME+" where "+COLUMN_NETWORK+" = '"+network+"' order by id desc limit 1", null );
//        return res.getString(0);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COLUMN_NETWORK_NAME))+"-"+res.getString(res.getColumnIndex(COLUMN_NETWORK_CODE))+"-"+res.getString(res.getColumnIndex(COLUMN_ID));
    }

    public boolean updateNetwork (String network,String networkname, String networkCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

//        contentValues.put(COLUMN_NETWORK, network);
        contentValues.put(COLUMN_NETWORK_CODE, networkCode);
        contentValues.put(COLUMN_NETWORK_NAME, networkname);

        db.update(CARD_TABLE_NAME, contentValues, COLUMN_NETWORK+" = ? ", new String[]{network});
        return true;
    }

    public boolean updateGPSByNetwork ( String network, String networkCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NETWORK, network);
        contentValues.put(COLUMN_NETWORK_CODE,networkCode);

        db.update(CARD_TABLE_NAME, contentValues,COLUMN_NETWORK+" = ? ", new String[]{networkCode});
        return true;
    }

    public Integer deleteGPS (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CARD_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }


    public String getGPSDetailsByUser() {
        //ArrayList<String> array_list = new ArrayList<String>();
        String array_list;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ CARD_TABLE_NAME+" order by id desc limit 1", null );
        res.moveToFirst();
        array_list=res.getString(res.getColumnIndex(COLUMN_NETWORK))+"-"+res.getString(res.getColumnIndex(COLUMN_NETWORK_CODE));
        return array_list;
    }


}




