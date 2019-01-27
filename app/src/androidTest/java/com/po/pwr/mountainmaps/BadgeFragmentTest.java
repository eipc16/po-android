package com.po.pwr.mountainmaps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Fragments.Badge.BadgeDisplayFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.LayoutRes;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4ClassRunner.class)
public class BadgeFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        Fragment fragment = new BadgeDisplayFragment();

        hiker_id = 3;

        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Test
    public void check_swipingChangesViewPager() {
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));
        onView(withId(R.id.badge_viewpager)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));
    }

    public void clickOutsideDrawer(View parentDrawerLayout, @LayoutRes Integer drawId) {
        onView(withId(drawId)).perform(TestingHelpers.clickXY(parentDrawerLayout.getX() + parentDrawerLayout.getWidth() + 10
                , parentDrawerLayout.getHeight() / 2f));
    }

    @Test
    public void check_swipingFromLeftToRightOpensNavigationDrawer() {
        onView(withId(R.id.fragmentList)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }

    @Test
    public void check_clickingOutsideClosesNavigationDrawer() {
        onView(withId(R.id.fragmentList)).perform(TestingHelpers.swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT));

        View parentDrawerLayout = mActivityRule.getActivity().findViewById(R.id.nav_view);
        clickOutsideDrawer(parentDrawerLayout, R.id.drawer_layout);
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
    }
}
