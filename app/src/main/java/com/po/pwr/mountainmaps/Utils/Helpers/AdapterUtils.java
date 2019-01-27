package com.po.pwr.mountainmaps.Utils.Helpers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.po.pwr.mountainmaps.Models.PointModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.PointListAdapter;

import java.util.List;

public final class AdapterUtils {

    private AdapterUtils() { /* empty constructor */ }

    /** Metoda aktualizująca adapter obsługujący listę odcinków trasy
     * @param view Obecny widok
     * @param context Obecny kontekst
     * @param newPointList Nowa lista punktów
     */
    public static void updateRecyclerViewPointList(View view, Context context, List<PointModel> newPointList) {
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.hikingTrailPointsList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        PointListAdapter mRecyclerAdapter = new PointListAdapter(newPointList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
