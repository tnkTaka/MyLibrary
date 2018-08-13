package local.hal.st32.android.mylibrary60213;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{

    private Context mContext;

    private String[] _deadlines;
    private Bitmap[] _images;

    public GridViewAdapter(Context c, String[] deadlines, Bitmap[] images) {
        mContext = c;
        this._images = images;
        this._deadlines = deadlines;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return _images.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.activity_grid_item, null);

            TextView textView = (TextView) grid.findViewById(R.id.product_deadline);
            ImageView imageView = (ImageView) grid.findViewById(R.id.product_image);

            textView.setText(_deadlines[position]);
            imageView.setImageBitmap(_images[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
