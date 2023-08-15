package com.example.appexamenkevinperez;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    private ListView listViewImages;
    private DatabaseHelper databaseHelper;
    private List<ImageData> imageDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        listViewImages = findViewById(R.id.listViewImages);
        databaseHelper = new DatabaseHelper(this);

        populateImageList();

        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = listViewImages.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    ImageData selectedImageData = imageDataList.get(selectedPosition);
                    Log.d("Debug", "Botón Editar presionado. Abriendo la actividad de edición.");
                    openEditActivity(selectedImageData);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = listViewImages.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    ImageData selectedImageData = imageDataList.get(selectedPosition);
                    Log.d("Debug", "Botón Eliminar presionado. Mostrando el diálogo de confirmación.");
                    showDeleteConfirmationDialog(selectedImageData);
                }
            }
        });

        registerForContextMenu(listViewImages);

        listViewImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageData selectedImageData = imageDataList.get(position);
                Log.d("Debug", "Elemento seleccionado en la lista. Abriendo la actividad de edición.");
                openEditActivity(selectedImageData);
            }
        });
    }

    private void populateImageList() {
        imageDataList = getAllImageData();
        ImageListAdapter imageListAdapter = new ImageListAdapter(this, imageDataList);
        listViewImages.setAdapter(imageListAdapter);
    }

    private List<ImageData> getAllImageData() {
        List<ImageData> imageDataList = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllData();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE));
                @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));

                imageDataList.add(new ImageData(id, latitude, longitude, description, imageBytes));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return imageDataList;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.image_list_context_menu, menu);
    }

    private void openEditActivity(ImageData imageData) {
        Intent intent = new Intent(this, EditImageActivity.class);
        intent.putExtra("imageData", imageData); // Aquí pasamos el objeto ImageData como extra
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(final ImageData imageData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar imagen");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta imagen?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage(imageData);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void deleteImage(ImageData imageData) {
        databaseHelper.deleteData(imageData.getId());
        populateImageList();
    }

}
