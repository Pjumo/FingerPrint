package com.example.fingerprint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private boolean isOpenCvLoaded = false;

    final ImageView imageView = findViewById(R.id.imageView);
    final ImageView imageView2 = findViewById(R.id.imageView2);
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fp);
    Bitmap bitmap_result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!isOpenCvLoaded)
                    return;

                imageView.setImageBitmap(bitmap);

                Mat fingerprint = new Mat();
                Mat result = new Mat();
                Utils.bitmapToMat(bitmap, fingerprint);

                Imgproc.Canny(fingerprint, result, 50, 150);

                Utils.matToBitmap(result, bitmap_result);
                imageView2.setImageBitmap(bitmap_result);
            }
        });
    }

//    public static int getImageByteSize(int w, int h, Bitmap.Config config) {
//        int byteSize = w * h;
//        switch(config){
//            case ARGB_8888:
//                byteSize*=4;
//                break;
//            case RGB_565:
//            case ARGB_4444:
//                byteSize*=2;
//            case ALPHA_8:
//                break;
//            default:
//                return -1;
//        }
//        return byteSize;
//    }

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status){
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        System.loadLibrary("opencv_java4");
        System.loadLibrary("fingerprint");
        if(!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using openCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }
        else{
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            isOpenCvLoaded=true;
        }
    }

    @Override
    protected void onDestroy() {
        bitmap.recycle();
        bitmap_result.recycle();
        bitmap = null;
        bitmap_result = null;

        super.onDestroy();
    }
}
