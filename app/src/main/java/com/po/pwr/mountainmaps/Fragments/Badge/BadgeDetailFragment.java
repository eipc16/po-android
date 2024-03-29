package com.po.pwr.mountainmaps.Fragments.Badge;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.po.pwr.mountainmaps.Models.BadgeModel;
import com.po.pwr.mountainmaps.R;

/**
 * Fragment wyświetlany przez ViewPager (Lista odznak)
 */
public class BadgeDetailFragment extends Fragment {

    private BadgeModel mViewModel;

    public static BadgeDetailFragment newInstance() {
        return new BadgeDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.badge_detail_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BadgeModel.class);
    }

}
