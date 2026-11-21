package com.appliance.store.dal.mapper;

import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.dal.datasource.CsvRowMapper;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.Laptop;

public class LaptopCsvRowMapper implements CsvRowMapper<Laptop> {
    @Override
    public Laptop mapRow(String[] columns) throws DaoException {
        if (columns == null || columns.length != CsvSchemaConstants.LAPTOP_COLUMN_COUNT) {
            int actualLength = columns == null ? CsvSchemaConstants.ZERO_COLUMNS : columns.length;
            throw new DaoException(ErrorMessageConstants.INVALID_CSV_ROW_FORMAT + actualLength);
        }
        try {
            long id = Long.parseLong(columns[CsvSchemaConstants.INDEX_ID]);
            String name = columns[CsvSchemaConstants.INDEX_NAME];
            long price = Long.parseLong(columns[CsvSchemaConstants.INDEX_PRICE]);
            String os = columns[CsvSchemaConstants.INDEX_LAPTOP_OS];
            String cpu = columns[CsvSchemaConstants.INDEX_LAPTOP_CPU];
            return new Laptop(id, name, price, os, cpu);
        } catch (NumberFormatException e) {
            throw new DaoException(ErrorMessageConstants.INVALID_CSV_ROW_FORMAT + e.getMessage(), e);
        }
    }
}
