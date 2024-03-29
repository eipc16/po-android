package com.po.pwr.mountainmaps;

import android.content.Context;
import android.view.View;

import com.po.pwr.mountainmaps.Fragments.HikingTrails.HikingTrailCreatorFragment;
import com.po.pwr.mountainmaps.Models.PointModel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HikingTrailCreatorTest {

    private static HikingTrailCreatorFragment fragment;

    private Context context;

    View view;

    @Before
    public void initTest() {
        fragment = new HikingTrailCreatorFragment();
        context = fragment.getContext();
        view = fragment.getView();
    }

    @Test
    public void check_hikingTrailSetUpPointList() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        pointList.add(new PointModel(2, "Test Point 2"));

        fragment.setUpPointList(pointList);

        assertEquals(fragment.getCurrentTrailPoints(), pointList);
    }

    @Test
    public void check_hikingTrailGenerationFailWhenLessThanOnePoint() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        //pointList.add(new PointModel(2, "Test Point 2"));

        fragment.setUpPointList(pointList);
        boolean result = fragment.getGeneratedPointList(view, 10, 30);
        assertFalse(result);
    }

    @Test
    public void check_hikingTrailGenerationFailWhenMoreThanPoints() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        //pointList.add(new PointModel(2, "Test Point 2"));

        fragment.setUpPointList(pointList);
        boolean result = fragment.getGeneratedPointList(view, 10, 30);
        assertFalse(result);
    }

    @Test
    public void check_hikingTrailGenerationFailWhenLengthEqualsZero() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        pointList.add(new PointModel(2, "Test Point 2"));

        fragment.setUpPointList(pointList);
        boolean result = fragment.getGeneratedPointList(view, 0, 0);
        assertFalse(result);
    }

    @Test
    public void check_hikingTrailGenerationFailWhenMinBiggerThanMax() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        pointList.add(new PointModel(2, "Test Point 2"));

        fragment.setUpPointList(pointList);
        boolean result = fragment.getGeneratedPointList(view, 15, 3);
        assertFalse(result);
    }
}
