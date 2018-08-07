package local.hal.st32.android.mylibrary60213;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
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

    private ImageView IMAGE_VIEW;
    private Button CAMERA_BUTTON;
    private Spinner CATEGORY_SPINNER;
    private EditText DEADLINE_EDIT_TEXT;

    private byte[] _byteImage;
    private String _deadline;
    private String _category;
    private int _done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        // 今日日付の取得
        Date now = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd");
        _deadline = formatter.format(now);

        Intent intent = this.getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("Image");

        // 画像の取り直しButton の紐付け
        CAMERA_BUTTON = findViewById(R.id.camera_button);
        CAMERA_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });

        // 画像のBitMapをbyteに変換
        _byteImage = Tool.getToolBytes(image);

        // ImageView の紐付け
        IMAGE_VIEW = findViewById(R.id.image_view);
        IMAGE_VIEW.setImageBitmap(image);

        // Spinner の紐付け と 選択したアイテムの取得
        CATEGORY_SPINNER = findViewById(R.id.category_spinner);
        CATEGORY_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択したアイテムを取得
                _category = (String) spinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { // アイテムを選択しなかったとき
            }
        });

        // 賞味期限EditText の紐付け
        DEADLINE_EDIT_TEXT = findViewById(R.id.deadline_editText);
        DEADLINE_EDIT_TEXT.setText(_deadline);

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

            IMAGE_VIEW.setImageBitmap(bitmap);
        }
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            String msg = + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日に変更しました";
            Toast.makeText(ProductEditActivity.this, msg, Toast.LENGTH_SHORT).show();

            DEADLINE_EDIT_TEXT.setText(String.format("%d年%02d月%02d日",year,monthOfYear + 1,dayOfMonth));
        }
    }

    public void onCreateButtonClick(View view) {

        _deadline = DEADLINE_EDIT_TEXT.getText().toString();

        Log.d("Create",""+_byteImage);
        Log.d("Create",_deadline);
        Log.d("Create",_category);
        Log.d("Create",""+_done);

        DatabaseHelper helper = new DatabaseHelper(ProductEditActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            DatabaseAccess.insert(db, _category, _deadline, _done, _byteImage);
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
