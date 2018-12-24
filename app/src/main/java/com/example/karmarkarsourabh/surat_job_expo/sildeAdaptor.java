package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class sildeAdaptor extends PagerAdapter {

    Context mContext;
    LayoutInflater inflater;

    public int[] dec_text = {
            R.string.intro_1_card,
            R.string.intro_2_card,
            R.string.intro_3_card,
            R.string.intro_4_card
    };

    public int[] symb = {
            R.drawable.cv_img,
            R.drawable.company,
            R.drawable.org,
            R.drawable.student_img
    };

    public sildeAdaptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (ConstraintLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.intro,container,false);

        ImageView sym = (ImageView) view.findViewById(R.id.ic_symb);
        TextView dec = (TextView) view.findViewById(R.id.dec);

        sym.setImageResource(symb[position]);
        dec.setText(dec_text[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
