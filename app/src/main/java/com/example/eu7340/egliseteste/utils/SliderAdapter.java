package com.example.eu7340.egliseteste.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<Bitmap> fotos;
    private List<String> titulos;
    //private List<String> descricoes;

    public SliderAdapter(Context context, List<Bitmap> fotos, List<String> titulos/*, List<String> descricoes*/) {
        this.context = context;
        this.fotos = fotos;
        this.titulos = titulos;
        //this.descricoes = descricoes;
    }

    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView titulo = (TextView) view.findViewById(R.id.banner_titulo);
        //TextView descricao = (TextView) view.findViewById(R.id.banner_descricao);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        titulo.setText(titulos.get(position));
        //descricao.setText(descricoes.get(position));
        linearLayout.setBackground(new BitmapDrawable(container.getResources(), fotos.get(position)));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
