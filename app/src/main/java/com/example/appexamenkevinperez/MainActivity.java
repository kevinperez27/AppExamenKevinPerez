package com.example.appexamenkevinperez;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 101;
    private static final int IMAGE_CAPTURE_REQUEST = 102;

    private ImageView imgPreview;
    private EditText edtLatitude, edtLongitude, edtDescription;
    private Button btnAdd;
    private Button btnImageList;
    private Button btnExit;

    private byte[] imageByteArray;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPreview = findViewById(R.id.imgPreview);
        edtLatitude = findViewById(R.id.edtLatitude);
        edtLongitude = findViewById(R.id.edtLongitude);
        edtDescription = findViewById(R.id.edtDescription);
        Button btnCapture = findViewById(R.id.btnCapture);
        btnAdd = findViewById(R.id.btnAdd);
        btnImageList = findViewById(R.id.btnImageList);
        btnExit = findViewById(R.id.btnExit);

        databaseHelper = new DatabaseHelper(this);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Debug", "Camera permission not granted. Requesting permission...");
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                } else {
                    openCamera();
                }
            }


        });







        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        btnImageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageListActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void openCamera() {
        Log.d("Debug", "openCamera() method called");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Log.d("Debug", "Camera app is available");
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
       // } else {
           //Log.d("Debug", "No camera app available");
        //}
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgPreview.setImageBitmap(imageBitmap);
                imageByteArray = convertBitmapToByteArray(imageBitmap);
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void insertData() {
        String latitude = edtLatitude.getText().toString();
        String longitude = edtLongitude.getText().toString();
        String description = edtDescription.getText().toString();

        if (latitude.isEmpty() || longitude.isEmpty() || description.isEmpty() || imageByteArray == null) {
            Toast.makeText(this, "Completa todos los campos y captura una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_IMAGE, imageByteArray);

        long newRowId = databaseHelper.insertData(values);
        if (newRowId != -1) {
            Toast.makeText(this, "Datos insertados con éxito", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Error al insertar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        edtLatitude.getText().clear();
        edtLongitude.getText().clear();
        edtDescription.getText().clear();
        imgPreview.setImageResource(android.R.color.transparent);
        imageByteArray = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara otorgado, abre la cámara
                Log.d("Debug", "Permiso de cámara otorgado. Abriendo cámara...");
                openCamera();
            } else {
                // Permiso de cámara denegado, muestra un mensaje
                Log.d("Debug", "Permiso de cámara denegado. No se puede abrir la cámara.");
                Toast.makeText(this, "Permiso denegado. No se puede abrir la cámara.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
