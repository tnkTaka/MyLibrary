package local.hal.st32.android.mylibrary60213;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

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
}
