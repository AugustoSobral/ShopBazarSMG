package com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sobralapps.android.shop_bazarsmg.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderImagesAdapter extends PagerAdapter {
    private Context context;
    private List<Uri> imagesList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public SliderImagesAdapter(List<Uri> imagesList, Context context){

        this.context = context;
        this.imagesList = imagesList;

    }

    public void setImagesList(List<Uri> imagesList){
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_image_layout, container, false);

        ImageView slideImage = view.findViewById(R.id.image_for_slide);

        Picasso.get().load(imagesList.get(position)).centerInside().fit().into(slideImage);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
