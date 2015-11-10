package weico.gyx.org.person_comment_client.DB;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by gyxian9 on 2015/11/10.
 */
public class DB {
    private SQLiteDatabase db;
    private final Context context;
    private final DBHelper dbHelper;

    public DB(Context c) {
        context = c;
        dbHelper = new DBHelper(context, Constants.DATABASE_NAME
                , null, Constants.DATABASE_VERSION);
    }

    public void close(){
        db.close();
    }

    public void open(){
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.v("打开数据库发生意外", e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public void insert2DB(String uname,String upwd){
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.KEY_USERNAME, uname);
            values.put(Constants.KEY_PASSWORD, upwd);
            db.insert(Constants.TABLE_NAME, null, values);
        }catch (Exception e) {
            Log.v("插入过程发生意外",e.getMessage());
        }
    }

    public Cursor getUserData(){
        Cursor c = db.query(Constants.TABLE_NAME,null,null,null,null,null,null);
        return c;
    }
}
