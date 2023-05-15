package com.ominfo.staff_original;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.ominfo.staff_original.util.LogUtil;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreviewView previewView = findViewById(R.id.previewView);

        cameraProviderFuture = ProcessCameraProvider.getInstance( getApplicationContext() );
        cameraProviderFuture.addListener(() -> {
            try {

                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder()
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();


                preview.setSurfaceProvider( previewView.getSurfaceProvider() );
                preview.setTargetRotation(Surface.ROTATION_0);

                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);





            } catch (ExecutionException | InterruptedException e) {


            }
        }, ContextCompat.getMainExecutor(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = previewView.getBitmap();
                    System.out.println("a : "+bitmap);
                }catch (Exception e){
                    System.out.println("exc : "+e);
                }
            }
        },10000);
    }

}