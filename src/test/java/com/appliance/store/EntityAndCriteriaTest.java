package com.appliance.store;

import com.appliance.store.constant.EntityConstants;
import com.appliance.store.constant.TestConstants;
import com.appliance.store.criteria.Range;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.entity.Laptop;
import com.appliance.store.entity.Oven;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityAndCriteriaTest {

    @Test
    @DisplayName("Test equals, hashCode, and deterministic toString for entities")
    void testEntityEqualsHashCodeAndToString() {
        Laptop laptop1 = new Laptop(
                TestConstants.LAPTOP_ID_1,
                TestConstants.LAPTOP_NAME_VIVOBOOK,
                TestConstants.LAPTOP_PRICE_45K,
                TestConstants.LAPTOP_OS_WINDOWS,
                TestConstants.LAPTOP_CPU_I7
        );
        Laptop laptop1Duplicate = new Laptop(
                TestConstants.LAPTOP_ID_1,
                TestConstants.LAPTOP_NAME_VIVOBOOK,
                TestConstants.LAPTOP_PRICE_45K,
                TestConstants.LAPTOP_OS_WINDOWS,
                TestConstants.LAPTOP_CPU_I7
        );
        Laptop laptop2 = new Laptop(
                TestConstants.LAPTOP_ID_2,
                TestConstants.LAPTOP_NAME_MACBOOK,
                TestConstants.LAPTOP_PRICE_120K,
                TestConstants.LAPTOP_OS_MACOS,
                TestConstants.LAPTOP_CPU_M2
        );

        assertEquals(laptop1, laptop1);
        assertEquals(laptop1, laptop1Duplicate);
        assertEquals(laptop1.hashCode(), laptop1Duplicate.hashCode());
        assertNotEquals(laptop1, laptop2);
        assertNotEquals(null, laptop1);
        assertNotEquals(TestConstants.DUMMY_OBJECT_STRING, laptop1);

        assertEquals(TestConstants.EXPECTED_LAPTOP_TO_STRING, laptop1.toString());

        Oven oven1 = new Oven(
                TestConstants.OVEN_ID_1,
                TestConstants.OVEN_NAME_BOSCH,
                TestConstants.OVEN_PRICE_32K,
                TestConstants.OVEN_POWER_2500,
                TestConstants.OVEN_CAPACITY_66
        );
        Oven oven1Duplicate = new Oven(
                TestConstants.OVEN_ID_1,
                TestConstants.OVEN_NAME_BOSCH,
                TestConstants.OVEN_PRICE_32K,
                TestConstants.OVEN_POWER_2500,
                TestConstants.OVEN_CAPACITY_66
        );
        Oven oven2 = new Oven(
                TestConstants.OVEN_ID_2,
                TestConstants.OVEN_NAME_SAMSUNG,
                TestConstants.OVEN_PRICE_42K,
                TestConstants.OVEN_POWER_3000,
                TestConstants.OVEN_CAPACITY_75
        );

        assertEquals(oven1, oven1);
        assertEquals(oven1, oven1Duplicate);
        assertEquals(oven1.hashCode(), oven1Duplicate.hashCode());
        assertNotEquals(oven1, oven2);
        assertNotEquals(laptop1, oven1);

        assertEquals(TestConstants.EXPECTED_OVEN_TO_STRING, oven1.toString());
    }

    @Test
    @DisplayName("Test Range boundary conditions and contains logic")
    void testRangeBoundaryConditions() {
        Range<Long> singleValueRange = new Range<>(TestConstants.RANGE_LOW_10K, TestConstants.RANGE_LOW_10K);
        assertEquals(TestConstants.RANGE_LOW_10K, singleValueRange.getLow());
        assertEquals(TestConstants.RANGE_LOW_10K, singleValueRange.getHigh());
        assertTrue(singleValueRange.contains(TestConstants.RANGE_LOW_10K));
        assertFalse(singleValueRange.contains(TestConstants.RANGE_VAL_BELOW));

        assertThrows(IllegalArgumentException.class, () ->
                new Range<>(TestConstants.RANGE_HIGH_20K, TestConstants.RANGE_LOW_10K));

        assertThrows(IllegalArgumentException.class, () ->
                new Range<>(null, TestConstants.RANGE_HIGH_20K));
        assertThrows(IllegalArgumentException.class, () ->
                new Range<>(TestConstants.RANGE_LOW_10K, null));

        Range<Long> normalRange = new Range<>(TestConstants.RANGE_LOW_10K, TestConstants.RANGE_HIGH_20K);
        assertTrue(normalRange.contains(TestConstants.RANGE_LOW_10K));
        assertTrue(normalRange.contains(TestConstants.RANGE_HIGH_20K));
        assertTrue(normalRange.contains(TestConstants.RANGE_VAL_INSIDE));
        assertFalse(normalRange.contains(TestConstants.RANGE_VAL_BELOW));
        assertFalse(normalRange.contains(TestConstants.RANGE_VAL_ABOVE));
        assertFalse(normalRange.contains(null));

        assertNotNull(normalRange.toString());
        assertEquals(normalRange, new Range<>(TestConstants.RANGE_LOW_10K, TestConstants.RANGE_HIGH_20K));
    }

    @Test
    @DisplayName("Test SearchCriteria exact match, case-insensitive string match, and Range filtering")
    void testSearchCriteriaMatching() {
        Laptop laptop = new Laptop(
                TestConstants.LAPTOP_ID_1,
                TestConstants.LAPTOP_NAME_VIVOBOOK,
                TestConstants.LAPTOP_PRICE_45K,
                TestConstants.LAPTOP_OS_WINDOWS,
                TestConstants.LAPTOP_CPU_I7
        );

        SearchCriteria<Laptop> criteria = new SearchCriteria<>(Laptop.class);
        criteria.addExactMatch(EntityConstants.FIELD_ID, Laptop::getId, TestConstants.LAPTOP_ID_1);
        criteria.addCaseInsensitiveStringMatch(EntityConstants.FIELD_NAME, Laptop::getName, TestConstants.TEST_SEARCH_NAME);
        criteria.addRangeFilter(EntityConstants.FIELD_PRICE, Laptop::getPrice, new Range<>(TestConstants.CRITERIA_RANGE_LOW_40K, TestConstants.CRITERIA_RANGE_HIGH_50K));
        criteria.addCaseInsensitiveStringMatch(EntityConstants.FIELD_OS, Laptop::getOs, TestConstants.TEST_SEARCH_OS);
        criteria.addCaseInsensitiveStringMatch(EntityConstants.FIELD_CPU, Laptop::getCpu, TestConstants.TEST_SEARCH_CPU);

        assertTrue(criteria.matches(laptop));
        assertTrue(criteria.hasCriteria());
        assertEquals(TestConstants.EXPECTED_CRITERIA_COUNT_5, criteria.getCriteriaCount());
        assertFalse(criteria.matches(null));

        SearchCriteria<Laptop> nonMatchingCriteria = new SearchCriteria<>(Laptop.class);
        nonMatchingCriteria.addRangeFilter(EntityConstants.FIELD_PRICE, Laptop::getPrice, new Range<>(TestConstants.NON_MATCH_LOW_10K, TestConstants.NON_MATCH_HIGH_20K));
        assertFalse(nonMatchingCriteria.matches(laptop));

        SearchCriteria<Laptop> nonMatchingString = new SearchCriteria<>(Laptop.class);
        nonMatchingString.addCaseInsensitiveStringMatch(EntityConstants.FIELD_OS, Laptop::getOs, TestConstants.TEST_SEARCH_DIFF_OS);
        assertFalse(nonMatchingString.matches(laptop));
    }
}
