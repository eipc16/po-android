package com.po.pwr.mountainmaps;

import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.Tap;

public final class TestingHelpers {

    private TestingHelpers() { /* empty constructor */ }

    static ViewAction clickXY(final float x, final float y ){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates( View view ){

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;

                        return new float[]{screenX, screenY};

                    }
                },
                Press.FINGER, InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.BUTTON_PRIMARY);
    }

    static ViewAction swipe(CoordinatesProvider from, CoordinatesProvider to) {
        return new GeneralSwipeAction(Swipe.FAST, from,
                to, Press.FINGER);
    }
}
