package local.hal.st32.android.mylibrary60213;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ProductListActivity extends AppCompatActivity {

    static final int MODE_INSERT = 1;
    static final int MODE_EDIT = 2;

    private final static int RESULT_CAMERA = 1001;

    private GridView GRID_VIEW_IMAGE_TEXT;

    private int[] _id;
    private String[] _deadlines;
    private Bitmap[] _images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        GRID_VIEW_IMAGE_TEXT = findViewById(R.id.gridView);
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseHelper helper = new DatabaseHelper(ProductListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = DatabaseAccess.findAll(db);

        _id = new int[cursor.getCount()];
        _deadlines = new String[cursor.getCount()];
        _images = new Bitmap[cursor.getCount()];

        int i = 0;
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int category = cursor.getInt(cursor.getColumnIndex("category"));
                String deadline = cursor.getString(cursor.getColumnIndex("deadline"));
                Bitmap image = Tool.getToolImage(cursor.getBlob(cursor.getColumnIndex("image")));

                _id[i] = id;
                _deadlines[i] = "賞味期限 : "+deadline;
                _images[i] = image;
                i ++;
            }while (cursor.moveToNext());
        }

        for (int v = 0; v < i ;v++){
            Log.d("Array",""+_id[v]);
            Log.d("Array",""+_deadlines[v]);
            Log.d("Array",""+_images[v]);
        }

        GridViewAdapter adapter = new GridViewAdapter(ProductListActivity.this, _deadlines, _images);
        GRID_VIEW_IMAGE_TEXT.setAdapter(adapter);
        GRID_VIEW_IMAGE_TEXT.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Positon",""+_id[position]);

                Intent intent = new Intent(ProductListActivity.this, ProductEditActivity.class);
                intent.putExtra("mode", MODE_EDIT);
                intent.putExtra("idNo", _id[position]);
                startActivity(intent);
            }
        });
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
