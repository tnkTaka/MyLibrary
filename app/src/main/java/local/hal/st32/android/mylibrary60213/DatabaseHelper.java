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
        ps.append("category_id INTEGER,");
        ps.append("deadline DATE,");
        ps.append("done INTEGER DEFAULT 0,");
        ps.append("image BLOB");
        ps.append(");");

        String productSql = ps.toString();
        db.execSQL(productSql);

        StringBuffer cs = new StringBuffer();
        cs.append("CREATE TABLE category (");
        cs.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        cs.append("name TEXT NOT NULL");
        cs.append(");");
        cs.append("INSERT INTO category(name) VALUES ('肉');");
        cs.append("INSERT INTO category(name) VALUES ('魚');");
        cs.append("INSERT INTO category(name) VALUES ('野菜');");
        cs.append("INSERT INTO category(name) VALUES ('果物');");
        cs.append("INSERT INTO category(name) VALUES ('調味料');");
        cs.append("INSERT INTO category(name) VALUES ('酒');");
        cs.append("INSERT INTO category(name) VALUES ('飲料水');");
        cs.append("INSERT INTO category(name) VALUES ('お菓子');");
        cs.append("INSERT INTO category(name) VALUES ('その他');");

        String CategorySql = cs.toString();
        db.execSQL(CategorySql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}