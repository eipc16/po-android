package com.po.pwr.mountainmaps.Utils.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.BadgeViewModel;
import com.po.pwr.mountainmaps.R;

import java.util.List;

public class DisplayBadgePagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<BadgeViewModel> badges;

    public DisplayBadgePagerAdapter(Context context, List<BadgeViewModel> badges) {
        this.context = context;
        this.badges = badges;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        BadgeViewModel badge = badges.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.badge_detail_fragment, collection, false);

        ImageView badgeImage = layout.findViewById(R.id.badgeImage);
        TextView badgeName = layout.findViewById(R.id.badgeName);
        TextView badgeDate = layout.findViewById(R.id.badgeDate);

        int image_id = context.getResources().getIdentifier("com.po.pwr.mountainmaps:drawable/" + badge.getName(), null, null);
        badgeImage.setImageResource(image_id);
        badgeName.setText(context.getString(R.string.badges_name, badge.getDisplayName()));
        badgeDate.setText(context.getString(R.string.badges_date, badge.getDate()));

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return this.badges.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
