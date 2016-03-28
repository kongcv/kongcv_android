package com.kongcv.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BitmapDataFragment extends Fragment {

	public static final String TAG = "bitmapsaver";
    private Bitmap bitmap;
 
    private BitmapDataFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
 
    public static BitmapDataFragment newInstance(Bitmap bitmap) {
        return new BitmapDataFragment(bitmap);
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
 
    public Bitmap getData() {
        return bitmap;
    }
}
