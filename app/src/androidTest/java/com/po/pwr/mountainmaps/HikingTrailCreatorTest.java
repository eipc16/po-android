package com.po.pwr.mountainmaps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Fragments.HikingTrails.HikingTrailCreatorFragment;
import com.po.pwr.mountainmaps.Models.HikingTrailModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4ClassRunner.class)
public class HikingTrailCreatorTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        Fragment fragment = HikingTrailCreatorFragment.newInstance(mActivityRule.getActivity().getResources().getString(R.string.new_hikingtrail));

        hiker_id = 1;

        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Test
    public void check_clickReverseButtonOnEmptyListError() throws InterruptedException {
        onView(withId(R.id.reverseButton)).perform(click());
        onView(withText(R.string.err_no_points_to_reverse)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    @Test
    public void check_clickInfoButtonOnEmptyListError() throws InterruptedException {
        onView(withId(R.id.infoButton)).perform(click());
        onView(withText(R.string.err_not_enough_points)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    @Test
    public void check_sendingEmptyTrailReturnsError() throws InterruptedException {
        onView(withId(R.id.saveButton)).perform(click());
        onView(withText(R.string.err_not_enough_points)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    @Test
    public void check_clickAddButtonOpensDialog() throws InterruptedException {
        onView(withId(R.id.addButton)).perform(click());
        Thread.sleep(1000);
    }

    @Test
    public void check_checkInfoShowsDialog() throws InterruptedException {
        List<Integer> pointList = new ArrayList<>();
        mActivityRule.getActivity().loadPoints();
        Thread.sleep(2000);
        pointList.add(MainActivity.pointSet.get(1).getId());
        pointList.add(MainActivity.pointSet.get(2).getId());
        HikingTrailModel newHikingTrail = new HikingTrailModel(1, "Test", new Date(2018, 01, 12), false);
        newHikingTrail.setPoints(pointList);

        Fragment fragment = HikingTrailCreatorFragment.newInstance(mActivityRule.getActivity().getResources().getString(R.string.update_hikingtrail), newHikingTrail);

        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        onView(withId(R.id.hikingTrailName)).perform(clearText()).perform(typeText("Nowa trasa"));
        onView(withId(R.id.hikingTrailDate)).perform(clearText()).perform(typeText("2019-01-28"));

        closeSoftKeyboard();

        onView(withId(R.id.infoButton)).perform(click());
        pressBack();
    }

    @Test
    public void check_sendingDataClosesFragment() throws InterruptedException {
        List<Integer> pointList = new ArrayList<>();
        mActivityRule.getActivity().loadPoints();
        Thread.sleep(2000);
        pointList.add(MainActivity.pointSet.get(1).getId());
        pointList.add(MainActivity.pointSet.get(2).getId());
        HikingTrailModel newHikingTrail = new HikingTrailModel(1, "Test", new Date(2018, 01, 12), false);
        newHikingTrail.setPoints(pointList);

        Fragment fragment = HikingTrailCreatorFragment.newInstance(mActivityRule.getActivity().getResources().getString(R.string.update_hikingtrail), newHikingTrail);

        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        onView(withId(R.id.hikingTrailName)).perform(clearText()).perform(typeText("Nowa trasa"));
        onView(withId(R.id.hikingTrailDate)).perform(clearText()).perform(typeText("2019-01-28"));

        closeSoftKeyboard();

        onView(withId(R.id.saveButton)).perform(click());
        onView(withText(R.string.err_no_error)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    @Test
    public void check_clickReverseButtonReversesPoints() {
        List<Integer> pointList = new ArrayList<>();
        mActivityRule.getActivity().loadPoints();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pointList.add(MainActivity.pointSet.get(1).getId());
        pointList.add(MainActivity.pointSet.get(2).getId());
        HikingTrailModel newHikingTrail = new HikingTrailModel(1, "Test", new Date(2018, 01, 12), false);
        newHikingTrail.setPoints(pointList);

        Fragment fragment = HikingTrailCreatorFragment.newInstance(mActivityRule.getActivity().getResources().getString(R.string.update_hikingtrail), newHikingTrail);

        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        onView(withId(R.id.hikingTrailName)).perform(clearText()).perform(typeText("Nowa trasa"));
        onView(withId(R.id.hikingTrailDate)).perform(clearText()).perform(typeText("2019-01-28"));
        closeSoftKeyboard();

        onView(withId(R.id.reverseButton)).perform(click());
    }

    @Test
    public void check_longClickAddButtonDialog() {
        List<Integer> pointList = new ArrayList<>();
        mActivityRule.getActivity().loadPoints();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pointList.add(MainActivity.pointSet.get(1).getId());
        pointList.add(MainActivity.pointSet.get(2).getId());
        HikingTrailModel newHikingTrail = new HikingTrailModel(1, "Test", new Date(2018, 01, 12), false);
        newHikingTrail.setPoints(pointList);

        Fragment fragment = HikingTrailCreatorFragment.newInstance(mActivityRule.getActivity().getResources().getString(R.string.update_hikingtrail), newHikingTrail);

        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        onView(withId(R.id.hikingTrailName)).perform(clearText()).perform(typeText("Nowa trasa"));
        onView(withId(R.id.hikingTrailDate)).perform(clearText()).perform(typeText("2019-01-28"));
        closeSoftKeyboard();

        onView(withId(R.id.addButton)).perform(longClick());

        onView(withText(R.string.generated_dialog_min)).check(matches(isDisplayed()));

        onView(withId(R.id.minLengthField)).perform(typeText("10"));
        onView(withId(R.id.maxLengthField)).perform(typeText("20"));

        onView(withText(R.string.track_list_dialog_agree)).perform(click());
    }
}