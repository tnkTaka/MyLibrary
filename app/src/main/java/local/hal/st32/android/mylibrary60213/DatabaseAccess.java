package local.hal.st32.android.mylibrary60213;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseAccess {

    public static Product findAll(SQLiteDatabase db, int selection, int selectionState) {
        String sql ="";
        switch (selectionState){
            case 0:
                sql = "SELECT _id, category, perishable, deadline, image FROM products ORDER BY deadline ASC";
                break;
            case 1:
                sql = "SELECT _id, category, perishable, deadline, image FROM products WHERE category = "+selection+" ORDER BY deadline ASC";
                break;
            case 2:
                sql = "SELECT _id, category, perishable, deadline, image FROM products WHERE perishable = "+selection+" ORDER BY deadline ASC";
                break;
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
        String sql = "SELECT _id, category, perishable, deadline, image FROM products WHERE _id = " + id ;
        Cursor cursor = db.rawQuery(sql, null);
        Product result = null;
        if(cursor.moveToFirst()) {

            int idxCategory = cursor.getColumnIndex("category");
            int idxPerishable = cursor.getColumnIndex("perishable");
            int idxDeadline = cursor.getColumnIndex("deadline");
            int idxImage = cursor.getColumnIndex("image");

            int category = cursor.getInt(idxCategory);
            int perishable = cursor.getInt(idxPerishable);
            String deadline = cursor.getString(idxDeadline);
            byte[] image = cursor.getBlob(idxImage);

            result = new Product();
            result.setId(id);
            result.setCategory(category);
            result.setPerishable(perishable);
            result.setDeadline(deadline);
            result.setImage(image);
        }
        return result;
    }

    public static long insert(SQLiteDatabase db, int category, int perishable, String deadline, byte[] image) {
        String sql = "INSERT INTO products (category, perishable, deadline, image) VALUES (?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, category);
        stmt.bindLong(2, perishable);
        stmt.bindString(3, deadline);
        stmt.bindBlob(4, image);
        long id = stmt.executeInsert();
        return id;
    }

    public static int update(SQLiteDatabase db, int id, int category, int perishable, String deadline, byte[] image) {
        String sql = "UPDATE products SET category = ?, perishable = ?, deadline = ?, image = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, category);
        stmt.bindLong(2, perishable);
        stmt.bindString(3, deadline);
        stmt.bindBlob(4, image);
        stmt.bindLong(5, id);
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
