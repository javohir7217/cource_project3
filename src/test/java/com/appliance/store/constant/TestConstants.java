package com.appliance.store.constant;

public final class TestConstants {
    public static final long LAPTOP_ID_1 = 1L;
    public static final long LAPTOP_ID_2 = 2L;
    public static final String LAPTOP_NAME_VIVOBOOK = "ASUS Vivobook";
    public static final String LAPTOP_NAME_MACBOOK = "Apple MacBook Pro";
    public static final long LAPTOP_PRICE_45K = 45000L;
    public static final long LAPTOP_PRICE_120K = 120000L;
    public static final String LAPTOP_OS_WINDOWS = "Windows";
    public static final String LAPTOP_OS_MACOS = "macOS";
    public static final String LAPTOP_CPU_I7 = "Intel i7";
    public static final String LAPTOP_CPU_M2 = "Apple M2";

    public static final long OVEN_ID_1 = 1L;
    public static final long OVEN_ID_2 = 2L;
    public static final String OVEN_NAME_BOSCH = "Bosch Serie 4";
    public static final String OVEN_NAME_SAMSUNG = "Samsung Dual Cook";
    public static final long OVEN_PRICE_32K = 32000L;
    public static final long OVEN_PRICE_42K = 42000L;
    public static final int OVEN_POWER_2500 = 2500;
    public static final int OVEN_POWER_3000 = 3000;
    public static final int OVEN_CAPACITY_66 = 66;
    public static final int OVEN_CAPACITY_75 = 75;

    public static final long RANGE_LOW_10K = 10000L;
    public static final long RANGE_HIGH_20K = 20000L;
    public static final long RANGE_VAL_INSIDE = 15000L;
    public static final long RANGE_VAL_BELOW = 5000L;
    public static final long RANGE_VAL_ABOVE = 25000L;

    public static final int EXPECTED_TOTAL_LAPTOPS = 5;
    public static final int EXPECTED_TOTAL_OVENS = 4;
    public static final int EXPECTED_ONE_ITEM = 1;
    public static final int EXPECTED_FILTERED_ALL_ITEMS = 6;
    public static final long EXPECTED_TOTAL_LAPTOP_COST = 350000L;
    public static final long EXPECTED_VIVOBOOK_COST = 45000L;

    public static final String EXPECTED_LAPTOP_TO_STRING = "Laptop{id=1, name='ASUS Vivobook', price=45000, os='Windows', cpu='Intel i7'}";
    public static final String EXPECTED_OVEN_TO_STRING = "Oven{id=1, name='Bosch Serie 4', price=32000, power=2500, capacity=66}";

    public static final String TEST_SEARCH_NAME = "asus VIVOBOOK";
    public static final String TEST_SEARCH_OS = "WINDOWS";
    public static final String TEST_SEARCH_CPU = "intel I7";
    public static final String TEST_SEARCH_DIFF_OS = "macOS";
    public static final String DUMMY_OBJECT_STRING = "SomeString";

    public static final long CRITERIA_RANGE_LOW_40K = 40000L;
    public static final long CRITERIA_RANGE_HIGH_50K = 50000L;
    public static final long NON_MATCH_LOW_10K = 10000L;
    public static final long NON_MATCH_HIGH_20K = 20000L;
    public static final int EXPECTED_CRITERIA_COUNT_5 = 5;

    public static final long PRICE_FILTER_LOW_60K = 60000L;
    public static final long PRICE_FILTER_HIGH_90K = 90000L;
    public static final int EXPECTED_PRICE_FILTERED_COUNT_2 = 2;

    public static final long MERGED_PRICE_LOW_18K = 18000L;
    public static final long MERGED_PRICE_HIGH_45K = 45000L;
    public static final long EXPECTED_COMBINED_COST_200K = 200000L;

    public static final String NON_EXISTENT_FILE = "non-existent-file.csv";
    public static final String SERVICE_UNAVAILABLE_MESSAGE = "Service is unavailable";
    public static final String DAO_FAILURE_MESSAGE = "Simulated DAO failure";

    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_FIND_LAPTOPS = "find laptops";

    public static final String DELIMITER_PIPE = "|";

    private TestConstants() {
    }
}
