package local.hal.st32.android.mylibrary60213;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProductEditActivity extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;
    private int _mode = ProductListActivity.MODE_INSERT;

    private ImageView IMAGE_VIEW;
    private Button CAMERA_BUTTON;
    private Spinner CATEGORY_SPINNER;
    private Spinner PERISHABLE_SPINNER;
    private EditText DEADLINE_EDIT_TEXT;
    private Button CREATE_BUTTON;

    private DateFormat JAPANESE_FORMAT;
    private DateFormat NORMAL_FORMAT;

    private String _date;

    private int _idNo;
    private byte[] _byteImage;
    private String _deadline;
    private int _category;
    private int _perishable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        // 戻るボタン設置
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 今日の日付の取得
        Date today = new Date(System.currentTimeMillis());
        JAPANESE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
        NORMAL_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

        // intent取得
        Intent intent = this.getIntent();
        _mode = intent.getIntExtra("mode",ProductListActivity.MODE_INSERT);

        // レイアウトビューのIDと紐付け
        CAMERA_BUTTON = findViewById(R.id.camera_button);
        IMAGE_VIEW = findViewById(R.id.image_view);
        CATEGORY_SPINNER = findViewById(R.id.category_spinner);
        DEADLINE_EDIT_TEXT = findViewById(R.id.deadline_editText);
        CREATE_BUTTON = findViewById(R.id.create_button);
        PERISHABLE_SPINNER = findViewById(R.id.perishable_spinner);

        // 新規作成かどうか_modeから調査
        if(_mode == ProductListActivity.MODE_INSERT){
            _deadline = JAPANESE_FORMAT.format(today);
            _date = NORMAL_FORMAT.format(today);
            Bitmap image = (Bitmap) intent.getParcelableExtra("Image");

            _byteImage = Tool.getToolBytes(image);
            IMAGE_VIEW.setImageBitmap(image);
            DEADLINE_EDIT_TEXT.setText(_deadline);
        }else {
            _idNo = intent.getIntExtra("idNo", 0);

            DatabaseHelper helper = new DatabaseHelper(ProductEditActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            try{
                Product productData = DatabaseAccess.findByPK(db, _idNo);
                _byteImage = productData.getImage();
                _category = productData.getCategory();
                _deadline = Tool.getToolJapaneseCalendar(productData.getDeadline());
                _perishable = productData.getPerishable();
                _date = productData.getDeadline();

                IMAGE_VIEW.setImageBitmap(Tool.getToolImage(_byteImage));
                CATEGORY_SPINNER.setSelection(_category);
                PERISHABLE_SPINNER.setSelection(_perishable);
                DEADLINE_EDIT_TEXT.setText(_deadline);
                CREATE_BUTTON.setText("更新");

            }catch(Exception ex) {
                Log.e("ERROR", ex.toString());
            }
            finally {
                db.close();
            }
        }

        // カテゴリースピナー選択リスナー
        CATEGORY_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _category = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // selectしなかった場合
            }
        });

        // 腐りやすさ選択リスナー
        PERISHABLE_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _perishable = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // selectしなかった場合
            }
        });

        // 取り直しボタンクリックリスナー
        CAMERA_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if (_mode == ProductListActivity.MODE_INSERT){
            return true;
        }else{
            inflater.inflate(R.menu.actionbar_menu,menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        switch (itemId){
            case android.R.id.home:
                finish();
                break;
            case R.id.actionbar_Delete:
                Bundle extras = new Bundle();
                extras.putInt("idNo",_idNo);
                DeleteDialogFragment dialog = new DeleteDialogFragment();
                dialog.setArguments(extras);
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager,"DeleteDialogFragment");
                break;
        }

        return super.onOptionsItemSelected(item);
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

            // 撮影した画像をビットマップに変換してビューにセット
            _byteImage = Tool.getToolBytes(bitmap);
            IMAGE_VIEW.setImageBitmap(bitmap);
        }
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            String msg = + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日に変更しました";
            Toast.makeText(ProductEditActivity.this, msg, Toast.LENGTH_SHORT).show();

            DEADLINE_EDIT_TEXT.setText(String.format("%d年%02d月%02d日",year,monthOfYear + 1,dayOfMonth));
            _date = String.format("%d/%02d/%02d",year,monthOfYear + 1,dayOfMonth);
        }
    }

    public void onCreateButtonClick(View view) {
        // DB登録　or　DB編集
        DatabaseHelper helper = new DatabaseHelper(ProductEditActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            if (_mode == ProductListActivity.MODE_INSERT){
                DatabaseAccess.insert(db, _category, _perishable, _date, _byteImage);
            }else {
                DatabaseAccess.update(db, _idNo, _category, _perishable, _date, _byteImage);
            }

        } catch(Exception ex) {
            Log.e("DB_ERROR", ex.toString());
        } finally {
            db.close();
        }
        finish();
    }

    public void onDateButtonClick(View view){
        _deadline = DEADLINE_EDIT_TEXT.getText().toString();

        if(_deadline.equals("")){
            Calendar cal = Calendar.getInstance();
            int nowYear = cal.get(Calendar.YEAR);
            int nowMonth = cal.get(Calendar.MONTH);
            int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(ProductEditActivity.this, new DatePickerDialogDateSetListener(), nowYear,nowMonth,nowDayOfMonth);
            dialog.show();
        }else{
            int dy = Integer.valueOf(_deadline.substring(0,4));
            int dm = Integer.valueOf(_deadline.substring(5,7));
            int dd = Integer.valueOf(_deadline.substring(8,10));
            DatePickerDialog dialog = new DatePickerDialog(ProductEditActivity.this, new DatePickerDialogDateSetListener(),dy,dm -1 ,dd);
            dialog.show();
        }

    }
}
