package local.hal.st32.android.mylibrary60213;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer ps = new StringBuffer();
        ps.append("CREATE TABLE products (");
        ps.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        ps.append("category TEXT NOT NULL,");
        ps.append("deadline DATE NOT NULL,");
        ps.append("done INTEGER DEFAULT 0 NOT NULL,");
        ps.append("image BLOB NOT NULL");
        ps.append(");");

        String SQL = ps.toString();
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}