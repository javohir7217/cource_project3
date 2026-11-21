package com.appliance.store.constant;

public final class CsvSchemaConstants {
    public static final String DEFAULT_LAPTOPS_CSV = "laptops-test.csv";
    public static final String DEFAULT_OVENS_CSV = "ovens-test.csv";

    public static final int LAPTOP_COLUMN_COUNT = 5;
    public static final int OVEN_COLUMN_COUNT = 5;
    public static final int ZERO_COLUMNS = 0;

    public static final int INDEX_ID = 0;
    public static final int INDEX_NAME = 1;
    public static final int INDEX_PRICE = 2;
    public static final int INDEX_LAPTOP_OS = 3;
    public static final int INDEX_LAPTOP_CPU = 4;
    public static final int INDEX_OVEN_POWER = 3;
    public static final int INDEX_OVEN_CAPACITY = 4;

    public static final String HEADER_ID = "id";

    private CsvSchemaConstants() {
    }
}
