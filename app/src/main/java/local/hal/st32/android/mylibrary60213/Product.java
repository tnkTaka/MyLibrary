package local.hal.st32.android.mylibrary60213;

import android.graphics.Bitmap;

public class Product {
    private int _id;
    private int _category;
    private String _deadline;
    private int _done;
    private byte[] _image;

    private int[] _gridIds;
    private String[] _gridDeadlines;
    private Bitmap[] _gridImages;

    public int getId(){
        return _id;
    }
    public void setId(int id){
        _id = id;
    }

    public int getCategory(){
        return _category;
    }
    public void  setCategory(int category){
        _category = category;
    }

    public String getDeadline() {
        return _deadline;
    }
    public void setDeadline(String deadline) {
        _deadline = deadline;
    }

    public byte[] getImage(){
        return _image;
    }
    public void setImage(byte[] image){
        _image = image;
    }

    public int[] getGridId(){
        return _gridIds;
    }
    public void setGridId(int[] gridIds){
        _gridIds = gridIds;
    }

    public String[] getGridDeadline(){
        return _gridDeadlines;
    }
    public void setGridDeadline(String[] gridDeadlines){
        _gridDeadlines = gridDeadlines;
    }

    public Bitmap[] getGridImage(){
        return _gridImages;
    }
    public void setGridImage(Bitmap[] gridImages){
        _gridImages = gridImages;
    }


}
