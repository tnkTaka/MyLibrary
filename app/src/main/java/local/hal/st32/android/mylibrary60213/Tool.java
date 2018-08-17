package local.hal.st32.android.mylibrary60213;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Tool {

    // BitMapをByte配列に変換
    public static byte[] getToolBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // Byte配列をBitMapに変換
    public static Bitmap getToolImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // 賞味期限までの日数を求める
    public static int getToolDiffDays(Date fromDate, Date toDate){
        int  diffDays = 0;
        if(fromDate != null && toDate != null) {
            long fromDateTime = fromDate.getTime();
            long toDateTime = toDate.getTime();

            diffDays = (int)(( toDateTime - fromDateTime  ) / (1000 * 60 * 60 * 24 ));
        }

        return diffDays;

    }

    // 和暦に変換
    public static String getToolJapaneseCalendar(String date){
        int dy = Integer.valueOf(date.substring(0,4));
        int dm = Integer.valueOf(date.substring(5,7));
        int dd = Integer.valueOf(date.substring(8,10));
        date = String.format("%d年%02d月%02d日",dy,dm,dd);

        return date;
    }
}
