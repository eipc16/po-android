package com.po.pwr.mountainmaps;

import com.po.pwr.mountainmaps.Models.BadgeModel;

import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;


public class BadgeModelTest {

    @Test
    public void check_badgeDisplayNameSetter() {
        BadgeModel badge = new BadgeModel(1, "Test_1", "badge_01", new Date(2018, 01, 31));
        String newName = "Test_2";
        badge.setDisplayName(newName);
        assertEquals(badge.getDisplayName(), newName);
    }

    @Test
    public void check_badgeEqualsWhenIdSame() {
        BadgeModel badge = new BadgeModel(23, "Test_1", "badge_01", new Date(2018, 01, 31));
        BadgeModel badge2 = new BadgeModel(23, "Test_2", "badge_02", new Date(2018, 01, 31));
        assertEquals(badge, badge2);
    }
}
