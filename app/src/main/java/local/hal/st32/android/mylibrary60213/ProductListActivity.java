package local.hal.st32.android.mylibrary60213;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ProductListActivity extends AppCompatActivity {

    static final int MODE_INSERT = 1;
    static final int MODE_EDIT = 2;

    private final static int RESULT_CAMERA = 1001;

    private GridView GRID_VIEW_IMAGE_TEXT;
    private Menu MENU;

    private int[] _id;
    private String[] _deadlines;
    private Bitmap[] _images;

    private int _selectionCategory;
    private int _selectionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        GRID_VIEW_IMAGE_TEXT = findViewById(R.id.gridView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_list,menu);

        menu.getItem(0).setVisible(false);

        MENU = menu;

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseHelper helper = new DatabaseHelper(ProductListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = DatabaseAccess.findAll(db, _selectionCategory, _selectionState);

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

        GridViewAdapter adapter = new GridViewAdapter(ProductListActivity.this, _deadlines, _images);
        GRID_VIEW_IMAGE_TEXT.setAdapter(adapter);
        GRID_VIEW_IMAGE_TEXT.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductListActivity.this, ProductEditActivity.class);
                intent.putExtra("mode", MODE_EDIT);
                intent.putExtra("idNo", _id[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA) {
            Bitmap bitmap;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        _selectionState = 1;
        MENU.getItem(0).setVisible(true);

        switch (itemId){
            case R.id.menu_category_meet:
                _selectionCategory = 0;
                Toast.makeText(ProductListActivity.this, "肉 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_fish:
                _selectionCategory = 1;
                Toast.makeText(ProductListActivity.this, "魚 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_vegetable:
                _selectionCategory = 2;
                Toast.makeText(ProductListActivity.this, "野菜 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_fruit:
                _selectionCategory = 3;
                Toast.makeText(ProductListActivity.this, "果物 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_seasoning:
                _selectionCategory = 4;
                Toast.makeText(ProductListActivity.this, "調味料 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_alcohol:
                _selectionCategory = 5;
                Toast.makeText(ProductListActivity.this, "酒 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_drink:
                _selectionCategory = 6;
                Toast.makeText(ProductListActivity.this, "飲料水 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_sweetTreat:
                _selectionCategory = 7;
                Toast.makeText(ProductListActivity.this, "お菓子 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_other:
                _selectionCategory = 8;
                Toast.makeText(ProductListActivity.this, "その他 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_revert:
                _selectionState = 0;
                MENU.getItem(0).setVisible(false);
                Toast.makeText(ProductListActivity.this, "初期状態に戻しました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNewButtonClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_CAMERA);
    }
}
