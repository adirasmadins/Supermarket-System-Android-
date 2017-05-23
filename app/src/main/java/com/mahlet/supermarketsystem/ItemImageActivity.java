package com.mahlet.supermarketsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ItemImageActivity extends AppCompatActivity {

    Button btnFromCamera;
    Button btnFromGallery;
    LinearLayout linearLayout;
    ImageView imageResult;
    final int CAMERA_REQUEST=1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_image);
        btnFromCamera=(Button)findViewById(R.id.btnFromCamera);
        btnFromGallery=(Button)findViewById(R.id.btnFromGallery);
        btnFromCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
        );
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageResult=new ImageView(this);

            imageResult.setImageBitmap(photo);
            linearLayout.addView(imageResult);
        }
    }
}
