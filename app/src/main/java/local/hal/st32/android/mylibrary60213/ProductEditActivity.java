package local.hal.st32.android.mylibrary60213;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProductEditActivity extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;//これがないと保存できないです。
    private ImageView imageView;//イメージビューの宣言文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        Intent intent = this.getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("bitImage");
        imageView = findViewById(R.id.image_view);//先にImageViewをレイアウトビューのIDと紐づけ
        imageView.setImageBitmap(image);

        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {//普通のインナークラスを使っての実装
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
            imageView.setImageBitmap(bitmap);
        }
    }

    public void onCreateButtonClick(View view) {
        
    }
}
