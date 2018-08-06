package local.hal.st32.android.mylibrary60213;

import java.sql.Timestamp;

public class Product {
    private int _id;
    private int _categoryId;
    private String _deadline;
    private int _done;
    private byte[] _image;

    public int getId(){
        return _id;
    }
    public void setId(int id){
        _id = id;
    }

    public int getCategoryId(){
        return _categoryId;
    }
    public void  setCategoryId(int categoryId){
        _categoryId = categoryId;
    }

    public String getDeadline() {
        return _deadline;
    }
    public void setDeadline(String deadline) {
        _deadline = deadline;
    }

    public int getDone() {
        return _done;
    }
    public void setDone(int done) {
        _done = done;
    }

    public byte[] getImage(){
        return _image;
    }
    public void setImage(byte[] image){
        _image = image;
    }


}
