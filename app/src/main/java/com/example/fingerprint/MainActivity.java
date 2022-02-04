package com.example.fingerprint;

import android.annotation.SuppressLint;
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
import org.opencv.core.Core;
import org.opencv.core.CvType;
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

    int fpLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView2;
        ImageView imageView;

        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);

        Button button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!isOpenCvLoaded)
                    return;

                //fpLevel 지정하여 전처리, 특징점추출 등의 순서를 지정
                switch (fpLevel) {
                    case 0:
                        Mat fingerprint = new Mat();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fp);
                        Utils.bitmapToMat(bitmap, fingerprint);

                        Bitmap bitmap_result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        imageView.setImageBitmap(bitmap);

                        Mat result = Pretreatment(fingerprint);

                        Utils.matToBitmap(result, bitmap_result);
                        imageView2.setImageBitmap(bitmap_result);

                        fpLevel++;
                        break;
                    case 1:
                    default:
                        break;
                }
            }
        });
    }

    public static Mat Pretreatment(Mat mainFP){
        Mat resultFP = new Mat();

        // LSM알고리즘

       int c = mainFP.cols();
        int r = mainFP.rows();

        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){

            }
        }


        //Sobel Mask
        /*Mat sobelFP_x = new Mat(), sobelFP_y = new Mat();
        Mat sobelFP_x_abs = new Mat(), sobelFP_y_abs = new Mat();

        Imgproc.Sobel(mainFP, sobelFP_x, CvType.CV_16S, 1, 0, 3, 1, 128);
        Imgproc.Sobel(mainFP, sobelFP_y, CvType.CV_16S, 0, 1, 3, 1, 128);

        Core.convertScaleAbs(sobelFP_x, sobelFP_x_abs);
        Core.convertScaleAbs(sobelFP_y, sobelFP_y_abs);

        Core.addWeighted(sobelFP_x_abs, 0.5, sobelFP_y_abs, 0.5, 1, resultFP, 8);*/
        return resultFP;
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

    /*@Override
    protected void onDestroy() {
        bitmap.recycle();
        bitmap_result.recycle();
        bitmap = null;
        bitmap_result = null;

        super.onDestroy();
    }*/
}
