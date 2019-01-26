package com.po.pwr.mountainmaps;

import com.po.pwr.mountainmaps.Models.PointModel;
import com.po.pwr.mountainmaps.Utils.Adapters.PointListAdapter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PointListAdapterTest {

    @Test
    public void check_pointListAdapterRemovePointFailWhenPositionLessThanZero() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        pointList.add(new PointModel(2, "Test Point 2"));

        PointListAdapter adapter = new PointListAdapter(pointList);
        boolean result = adapter.removeElement(-1);
        assertFalse(result);
    }

    @Test
    public void check_pointListAdapterRemovePointFailWhenPositionMoreThanListSize() {
        List<PointModel> pointList = new ArrayList<>();
        pointList.add(new PointModel(1, "Test Point 1"));
        pointList.add(new PointModel(2, "Test Point 2"));

        PointListAdapter adapter = new PointListAdapter(pointList);
        boolean result = adapter.removeElement(3);
        assertFalse(result);
    }
}
