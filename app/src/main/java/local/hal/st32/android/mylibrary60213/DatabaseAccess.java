package local.hal.st32.android.mylibrary60213;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DatabaseAccess {

    public static Cursor findAll(SQLiteDatabase db) {
        //全ての場合
        String sql = "SELECT _id, category_id, deadline, done, image FROM products ORDER BY deadline DESC";

        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public static Product findByPK(SQLiteDatabase db, int id) {
        String sql = "SELECT _id, category_id, deadline, done, image FROM products WHERE _id = " + id ;
        Cursor cursor = db.rawQuery(sql, null);
        Product result = null;
        if(cursor.moveToFirst()) {

            int idxCategoryId = cursor.getColumnIndex("category_id");
            int idxDeadline = cursor.getColumnIndex("deadline");
            int idxDone = cursor.getColumnIndex("done");
            int idxImage = cursor.getColumnIndex("image");

            int categoryId = cursor.getInt(idxCategoryId);
            String deadline = cursor.getString(idxDeadline);
            int done = cursor.getInt(idxDone);
            byte[] image = cursor.getBlob(idxImage);

            result = new Product();
            result.setId(id);
            result.setCategoryId(categoryId);
            result.setDeadline(deadline);
            result.setDone(done);
            result.setImage(image);
        }
        return result;
    }

    public static long insert(SQLiteDatabase db, int categoryId, String deadline, int done, byte[] image) {
        String sql = "INSERT INTO products (category_id, deadline, done, image) VALUES (?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, categoryId);
        stmt.bindString(2, deadline);
        stmt.bindLong(3, done);
        stmt.bindBlob(4, image);
        long id = stmt.executeInsert();
        return id;
    }

    public static int delete(SQLiteDatabase db, int id) {
        String sql = "DELETE FROM products WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
}