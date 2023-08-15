package com.example.appexamenkevinperez;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditImageActivity extends AppCompatActivity {

    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextDescription;

    private ImageData imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        editTextDescription = findViewById(R.id.editTextDescription);
        Button buttonSave = findViewById(R.id.buttonSave);

        // Obtener los datos de la imagen desde el Intent
        imageData = getIntent().getParcelableExtra("imageData");

        // Mostrar los datos de la imagen en los elementos de la interfaz
        if (imageData != null) {
            editTextLatitude.setText(imageData.getLatitude());
            editTextLongitude.setText(imageData.getLongitude());
            editTextDescription.setText(imageData.getDescription());
        }

        final DatabaseHelper dbHelper = new DatabaseHelper(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores editados de los EditText
                String newLatitude = editTextLatitude.getText().toString();
                String newLongitude = editTextLongitude.getText().toString();
                String newDescription = editTextDescription.getText().toString();

                // Actualizar los valores en la base de datos
                if (imageData != null) {
                    // Actualizar los valores en el objeto imageData
                    imageData.setLatitude(newLatitude);
                    imageData.setLongitude(newLongitude);
                    imageData.setDescription(newDescription);

                    // Actualizar los valores en la base de datos a través del DatabaseHelper
                    dbHelper.updateImageData(imageData);

                    // Mostrar mensaje de éxito
                    Toast.makeText(EditImageActivity.this, "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show();

                    // SetResult para indicar que se ha realizado una edición en la imagen
                    setResult(RESULT_OK);

                    // Finalizar la actividad
                    finish();
                }
            }
        });

    }
}
