package local.hal.st32.android.mylibrary60213;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class ProductEditActivity extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;

    private ImageView IMAGE_VIEW;
    private Button CAMERA_BUTTON;
    private Spinner CATEGORY_SPINNER;

    private byte[] _byteImage;
    private String _today;
    private String _category;
    private int _done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        Intent intent = this.getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("Image");

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

        // 画像の取り直し　Button の紐付け
        CAMERA_BUTTON = findViewById(R.id.camera_button);
        CAMERA_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
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

            IMAGE_VIEW.setImageBitmap(bitmap);
        }
    }

    public void onCreateButtonClick(View view) {
        Log.d("result",""+_byteImage);
        Log.d("result",""+_category);
    }
}
