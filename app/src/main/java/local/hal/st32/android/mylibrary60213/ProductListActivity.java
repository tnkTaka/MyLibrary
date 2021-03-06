package local.hal.st32.android.mylibrary60213;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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

    private Intent INTENT;
    private GridView GRID_VIEW_IMAGE_TEXT;
    private Menu MENU;

    private int[] _ids;
    private String[] _deadlines;
    private Bitmap[] _images;

    private int _selection;
    private int _selectionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        GRID_VIEW_IMAGE_TEXT = findViewById(R.id.gridView);
        INTENT = new Intent(ProductListActivity.this, ProductEditActivity.class);
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
        Product productData = DatabaseAccess.findAll(db, _selection, _selectionState);

        // DBから各項目の配列を取得してAdapterに渡す
        _ids = productData.getGridId();
        _deadlines = productData.getGridDeadline();
        _images = productData.getGridImage();

        GridViewAdapter adapter = new GridViewAdapter(ProductListActivity.this, _deadlines, _images);
        GRID_VIEW_IMAGE_TEXT.setAdapter(adapter);
        GRID_VIEW_IMAGE_TEXT.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                INTENT.putExtra("mode", MODE_EDIT);
                INTENT.putExtra("idNo", _ids[position]);
                startActivity(INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 写真を撮影して登録画面に遷移
        if (requestCode == RESULT_CAMERA) {
            Bitmap bitmap;
            if( data.getExtras() == null){
                Toast.makeText(ProductListActivity.this, "写真の取得に失敗しました", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                bitmap = (Bitmap) data.getExtras().get("data");
            }

            INTENT.putExtra("mode", MODE_INSERT);
            INTENT.putExtra("Image", bitmap);
            startActivity(INTENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 各メニュー選択時の処理
        int itemId = item.getItemId();
        _selectionState = 1;
        MENU.getItem(0).setVisible(true);

        switch (itemId){
            case R.id.menu_category_meet:
                _selection = 0;
                Toast.makeText(ProductListActivity.this, "肉 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_fish:
                _selection = 1;
                Toast.makeText(ProductListActivity.this, "魚 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_vegetable:
                _selection = 2;
                Toast.makeText(ProductListActivity.this, "野菜 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_fruit:
                _selection = 3;
                Toast.makeText(ProductListActivity.this, "果物 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_seasoning:
                _selection = 4;
                Toast.makeText(ProductListActivity.this, "調味料 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_alcohol:
                _selection = 5;
                Toast.makeText(ProductListActivity.this, "酒 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_drink:
                _selection = 6;
                Toast.makeText(ProductListActivity.this, "飲料水 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_sweetTreat:
                _selection = 7;
                Toast.makeText(ProductListActivity.this, "お菓子 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_category_other:
                _selection = 8;
                Toast.makeText(ProductListActivity.this, "その他 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_ordinary:
                _selectionState = 2;
                _selection = 0;
                Toast.makeText(ProductListActivity.this, "普通 が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_easy_to_decay:
                _selectionState = 2;
                _selection = 1;
                Toast.makeText(ProductListActivity.this, "腐りやすい が選択されました", Toast.LENGTH_SHORT).show();
                onResume();
                break;
            case R.id.menu_hard_to_decay:
                _selectionState = 2;
                _selection = 2;
                Toast.makeText(ProductListActivity.this, "腐りにくい が選択されました", Toast.LENGTH_SHORT).show();
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
