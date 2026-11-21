package com.appliance.store.dal.mapper;

import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.dal.datasource.CsvRowMapper;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.Oven;

public class OvenCsvRowMapper implements CsvRowMapper<Oven> {
    @Override
    public Oven mapRow(String[] columns) throws DaoException {
        if (columns == null || columns.length != CsvSchemaConstants.OVEN_COLUMN_COUNT) {
            int actualLength = columns == null ? CsvSchemaConstants.ZERO_COLUMNS : columns.length;
            throw new DaoException(ErrorMessageConstants.INVALID_CSV_ROW_FORMAT + actualLength);
        }
        try {
            long id = Long.parseLong(columns[CsvSchemaConstants.INDEX_ID]);
            String name = columns[CsvSchemaConstants.INDEX_NAME];
            long price = Long.parseLong(columns[CsvSchemaConstants.INDEX_PRICE]);
            int power = Integer.parseInt(columns[CsvSchemaConstants.INDEX_OVEN_POWER]);
            int capacity = Integer.parseInt(columns[CsvSchemaConstants.INDEX_OVEN_CAPACITY]);
            return new Oven(id, name, price, power, capacity);
        } catch (NumberFormatException e) {
            throw new DaoException(ErrorMessageConstants.INVALID_CSV_ROW_FORMAT + e.getMessage(), e);
        }
    }
}
