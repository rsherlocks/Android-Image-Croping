package com.example.androidshaper.androidimagecrop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION =99 ;
    private static final int SELECT_REQUEST_CODE =0 ;
   CropImageView cropImageView;
    Button buttonCrop;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cropImageView=findViewById(R.id.cropImageView);
        imageView=findViewById(R.id.imageView);
        buttonCrop=findViewById(R.id.cropButton);
        CheckPermission();

        buttonCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (CheckPermission())
                {
                    SelectImage();
                }

            }
        });
    }

    private void SelectImage() {
//        //Intent intentSelect= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent intent=new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,SELECT_REQUEST_CODE);

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==SELECT_REQUEST_CODE && data!=null)
//        {
//            Uri imageUri=data.getData();
//            CropImage.activity(imageUri)
//                    .start(this);
//        }

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                try {
                  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);

                    imageView.setImageBitmap(bitmap);

                    Bitmap cropped = cropImageView.getCroppedImage();
                   // cropImage(bitmap);
                    //cropImageView.setImageBitmap(bitmap);
//                    imageView.setImageBitmap(cropped);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
//            if (requestCode==RESULT_OK)
//            {
//
//                try {
//                    Uri uriImage=result.getUri();
//
//                    Bitmap bitmapCapture=MediaStore.Images.Media.getBitmap(getContentResolver(),uriImage);
//                    imageView.setImageBitmap(bitmapCapture);
//                    imageView.setImageURI(uriImage);
//                    Toast.makeText(getApplicationContext(),"Image Croping",Toast.LENGTH_SHORT).show();
//                    cropImage(bitmapCapture);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Data is null",Toast.LENGTH_SHORT).show();
        }

    }

    private void cropImage(Bitmap bitmapCapture) {
        if (bitmapCapture!=null)
        {
//            imageView.setImageBitmap(bitmapCapture);

        }
        else{
            Toast.makeText(getApplicationContext(),"View is null",Toast.LENGTH_SHORT).show();
        }


    }

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||  ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(MainActivity
                                        .this, MainActivity.class));
                                MainActivity.this.overridePendingTransition(0, 0);
                                SelectImage();
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }
}