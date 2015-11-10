package weico.gyx.org.person_comment_client.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gyxian9 on 2015/11/10.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME
            + "(" + Constants.KEY_ID  + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Constants.KEY_USERNAME + "TEXT NOT NULL,"
            + Constants.KEY_PASSWORD + " TEXT NOT NULL);";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            Log.v("创建表出现意外", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
