package com.example.appexamenkevinperez;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {
    private int id;
    private String latitude;
    private String longitude;
    private String description;
    private byte[] imageBytes;

    public ImageData(int id, String latitude, String longitude, String description, byte[] imageBytes) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageBytes = imageBytes;
    }

    public int getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setLongitude(String newLongitude) {
        longitude = newLongitude;
    }

    public void setLatitude(String newLatitude) {
        latitude = newLatitude;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    // Implementación de Parcelable
    protected ImageData(Parcel in) {
        id = in.readInt();
        latitude = in.readString();
        longitude = in.readString();
        description = in.readString();
        imageBytes = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(description);
        dest.writeByteArray(imageBytes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };
}
