package com.sobralapps.android.shop_bazarsmg.Sell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageCropperActivity extends AppCompatActivity {

    private ImageButton btn_browse;
    private ImageButton btn_reset;
    private ImageView imageView;
    private CircleImageView circleImageView;
    Uri uri;
    private Uri uriPass = null;

    private int CODE_FOR_SHAPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        btn_browse = findViewById(R.id.image_cropper_btn_add_picture);
        btn_reset = findViewById(R.id.image_cropper_btn_reset);
        imageView = findViewById(R.id.cropper_image);
        circleImageView = findViewById(R.id.cropper_image_circle);

        Intent intent = getIntent();
        CODE_FOR_SHAPE = intent.getIntExtra("IMAGE_SHAPE_CODE", 0);

        if(CODE_FOR_SHAPE==0)
            circleImageView.setVisibility(View.GONE);
        if(CODE_FOR_SHAPE==1)
            imageView.setVisibility(View.GONE);

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(ImageCropperActivity.this);                      //CropImage é um método importado de com.theartofdev.edmodo:android-image-cropper:2.4.+ que é uma biblioteca que controla a programação por trás e retorna os dados pro onActivityResult.
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CODE_FOR_SHAPE==0)
                    imageView.setImageBitmap(null);
                if(CODE_FOR_SHAPE==1)
                    circleImageView.setImageBitmap(null);
                uriPass = null;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (android.R.id.home):
                setResult(RESULT_CANCELED);
                finish();
                return true;

            case (R.id.save_image):
                saveImage();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)){
                uri = imageUri;
            }
            else {
                startCrop(imageUri);
            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                if(CODE_FOR_SHAPE==0)
                    imageView.setImageURI(result.getUri());
                if(CODE_FOR_SHAPE==1)
                    circleImageView.setImageURI(result.getUri());
                uriPass = result.getUri();
                //Toast.makeText(this, "Imagem Atualizada Com Sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCrop(Uri imageUri) {

        if(CODE_FOR_SHAPE==0) {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
        }
        if(CODE_FOR_SHAPE==1){
            CropImage.activity(imageUri).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true)
                    .setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
        }
    }

    private void saveImage() {
        if (uriPass==null){
            Toast.makeText(this, "Por favor, insira uma imagem.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent dataForAddEditScreen = new Intent();
        dataForAddEditScreen.setData(uriPass);                                                      //Como vamos passar uma Uri, usa-se o método .setData ao invés de putExtra com nomes, mas se quiser passar várias Uris pode se criar um bundle.
        setResult(RESULT_OK, dataForAddEditScreen);
        finish();
    }
}
