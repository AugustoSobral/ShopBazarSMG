package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sobralapps.android.shop_bazarsmg.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context){

        this.context = context;

    }

    public String[] frasesSlide = {

            "Encontre produtos e serviços anunciados por pessoas perto de você.",
            "Venda produtos novos e usados, anuncie grátis!",
            "Crie anúncios de venda de automóveis, roupas, celulares, venda e aluguel de imóveis e faça uma grana.",
            "Basta fotografar as coisas que você comprou e não usou, ou não usa mais e criar seu anúncio.",
            "Anuncie serviços prestados por você para alcançar mais clientes!"

    };

    @Override
    public int getCount() {
        return frasesSlide.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        TextView slideFrase = view.findViewById(R.id.frase_slide_text);

        slideFrase.setText(frasesSlide[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
