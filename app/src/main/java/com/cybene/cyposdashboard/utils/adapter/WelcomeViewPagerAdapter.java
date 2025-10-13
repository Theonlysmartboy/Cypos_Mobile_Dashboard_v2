package com.cybene.cyposdashboard.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.items.WelcomeItem;

import java.util.List;

public class WelcomeViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<WelcomeItem> welcomeItemList;
    ImageView imageView;
    TextView title, descr;

    public WelcomeViewPagerAdapter(Context mContext, List<WelcomeItem> welcomeItemList) {
        this.mContext = mContext;
        this.welcomeItemList = welcomeItemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_welcome,null);
        imageView = view.findViewById(R.id.wImage);
        title = view.findViewById(R.id.intro);
        descr = view.findViewById(R.id.wMessage);
        title.setText(welcomeItemList.get(position).getTitle());
        descr.setText(welcomeItemList.get(position).getDescription());
        imageView.setImageResource(welcomeItemList.get(position).getImg());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return welcomeItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
