package local.hal.st32.android.mylibrary60213;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class ProductListActivity extends AppCompatActivity {

    static final int MODE_INSERT = 1;
    static final int MODE_EDIT = 2;

    private final static int RESULT_CAMERA = 1001;

    private GridView GRID_VIEW_IMAGE_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        GRID_VIEW_IMAGE_TEXT = findViewById(R.id.grid_view_image_text);

    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseHelper helper = new DatabaseHelper(ProductListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = DatabaseAccess.findAll(db);
        Log.d("CursorMax",""+cursor.getCount());
        if(cursor.moveToFirst()){
            do {
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String deadline = cursor.getString(cursor.getColumnIndex("deadline"));
                Bitmap image = Tool.getToolImage(cursor.getBlob(cursor.getColumnIndex("image")));
                Log.d("Cursor"+id,""+id+" : "+category+" : "+deadline+" : "+ image);
            }while (cursor.moveToNext());
        }
    }

    //これからImageViewにとった写真を張り付け。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA) {
            Bitmap bitmap;//BitMapも最適。
            // cancelしたケースも含む
            if( data.getExtras() == null){
                Log.d("debug","cancel ?");
                return;
            }
            else{
                bitmap = (Bitmap) data.getExtras().get("data");

                // 画像サイズを計測
                int bmpWidth = bitmap.getWidth();
                int bmpHeight = bitmap.getHeight();
                Log.d("debug",String.format("w= %d",bmpWidth));
                Log.d("debug",String.format("h= %d",bmpHeight));
            }

            Intent intent = new Intent(ProductListActivity.this, ProductEditActivity.class);
            intent.putExtra("mode", MODE_INSERT);
            intent.putExtra("Image", bitmap);
            startActivity(intent);
        }
    }

    public void onNewButtonClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_CAMERA);
    }
}
