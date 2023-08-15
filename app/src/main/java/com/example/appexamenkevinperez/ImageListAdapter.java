package com.example.appexamenkevinperez;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ImageListAdapter extends BaseAdapter {

    private Context context;
    private List<ImageData> imageDataList;

    public ImageListAdapter(Context context, List<ImageData> imageDataList) {
        this.context = context;
        this.imageDataList = imageDataList;
    }

    @Override
    public int getCount() {
        return imageDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; // You can use the ID of the item from your data if needed
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textViewInfo = convertView.findViewById(R.id.textViewInfo);

        ImageData imageData = imageDataList.get(position);

        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData.getImageBytes(), 0, imageData.getImageBytes().length);
        imageView.setImageBitmap(imageBitmap);

        String infoText = "Latitud: " + imageData.getLatitude() + "\n" +
                "Longitud: " + imageData.getLongitude() + "\n" +
                "Descripción: " + imageData.getDescription();

        textViewInfo.setText(infoText);

        return convertView;
    }
}


