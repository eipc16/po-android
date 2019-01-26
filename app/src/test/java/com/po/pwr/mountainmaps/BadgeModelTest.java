package com.po.pwr.mountainmaps;

import com.po.pwr.mountainmaps.Models.BadgeViewModel;

import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;


public class BadgeModelTest {

    @Test
    public void check_badgeDisplayNameSetter() {
        BadgeViewModel badge = new BadgeViewModel(1, "Test_1", "badge_01", new Date(2018, 01, 31));
        String newName = "Test_2";
        badge.setDisplayName(newName);
        assertEquals(badge.getDisplayName(), newName);
    }

    @Test
    public void check_badgeEqualsWhenIdSame() {
        BadgeViewModel badge = new BadgeViewModel(23, "Test_1", "badge_01", new Date(2018, 01, 31));
        BadgeViewModel badge2 = new BadgeViewModel(23, "Test_2", "badge_02", new Date(2018, 01, 31));
        assertEquals(badge, badge2);
    }
}
