package com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImagesChosenViewModel extends ViewModel {

    private List<Uri> imagesList = new ArrayList<>();

    public ImagesChosenViewModel(){

    }

    public List<Uri> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<Uri> imagesList){
        this.imagesList = imagesList;
    }

    public void addImageToList(Uri imageUri) {
        imagesList.add(imageUri);
    }

    public void removeImageToList(int position){
        imagesList.remove(position);
    }
}
