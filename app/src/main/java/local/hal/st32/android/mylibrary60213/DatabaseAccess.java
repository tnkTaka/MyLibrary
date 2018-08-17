package local.hal.st32.android.mylibrary60213;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseAccess {

    public static Product findAll(SQLiteDatabase db, int category, int selectionState) {
        String sql ="";
        if (selectionState == 0){
            sql = "SELECT _id, category, deadline, image FROM products ORDER BY deadline DESC";
        }else if (selectionState == 1){
            sql = "SELECT _id, category, deadline, image FROM products WHERE category = "+category+" ORDER BY deadline DESC";
        }

        Cursor cursor = db.rawQuery(sql, null);
        Product result = null;

        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        int[] gridIds = new int[cursor.getCount()];
        String[] gridDeadlines = new String[cursor.getCount()];
        Bitmap[] gridImages = new Bitmap[cursor.getCount()];

        int i = 0;
        int diffDays = 0;
        if(cursor.moveToFirst()){
            do {
                gridIds[i] = cursor.getInt(cursor.getColumnIndex("_id"));

                try {
                    diffDays = Tool.getToolDiffDays(now,formatter.parse(cursor.getString(cursor.getColumnIndex("deadline"))));
                    if (diffDays > 0){
                        gridDeadlines[i] = "賞味期限まであと "+diffDays+" 日";
                    }else if (diffDays == 0){
                        gridDeadlines[i] = "今日が賞味期限です！";
                    }else {
                        gridDeadlines[i] = "賞味期限が切れました！";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                gridImages[i] = Tool.getToolImage(cursor.getBlob(cursor.getColumnIndex("image")));

                i ++;
            }while (cursor.moveToNext());
        }

        result = new Product();
        result.setGridId(gridIds);
        result.setGridDeadline(gridDeadlines);
        result.setGridImage(gridImages);

        return result;
    }

    public static Product findByPK(SQLiteDatabase db, int id) {
        String sql = "SELECT _id, category, deadline, image FROM products WHERE _id = " + id ;
        Cursor cursor = db.rawQuery(sql, null);
        Product result = null;
        if(cursor.moveToFirst()) {

            int idxCategory = cursor.getColumnIndex("category");
            int idxDeadline = cursor.getColumnIndex("deadline");
            int idxImage = cursor.getColumnIndex("image");

            int category = cursor.getInt(idxCategory);
            String deadline = cursor.getString(idxDeadline);
            byte[] image = cursor.getBlob(idxImage);

            result = new Product();
            result.setId(id);
            result.setCategory(category);
            result.setDeadline(deadline);
            result.setImage(image);
        }
        return result;
    }

    public static long insert(SQLiteDatabase db, int category, String deadline, byte[] image) {
        String sql = "INSERT INTO products (category, deadline, image) VALUES (?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, category);
        stmt.bindString(2, deadline);
        stmt.bindBlob(3, image);
        long id = stmt.executeInsert();
        return id;
    }

    public static int update(SQLiteDatabase db, int id, int category, String deadline, byte[] image) {
        String sql = "UPDATE products SET category = ?, deadline = ?, image = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, category);
        stmt.bindString(2, deadline);
        stmt.bindBlob(3, image);
        stmt.bindLong(4, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }

    public static int delete(SQLiteDatabase db, int id) {
        String sql = "DELETE FROM products WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
}
