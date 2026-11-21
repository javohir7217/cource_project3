package com.appliance.store;

import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.EntityConstants;
import com.appliance.store.constant.TestConstants;
import com.appliance.store.criteria.Range;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.dal.dao.Dao;
import com.appliance.store.dal.dao.DaoImpl;
import com.appliance.store.dal.datasource.CsvDataSource;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.dal.factory.DaoFactory;
import com.appliance.store.dal.mapper.LaptopCsvRowMapper;
import com.appliance.store.entity.Laptop;
import com.appliance.store.entity.Oven;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DaoTest {

    @BeforeEach
    void setUp() {
        DaoFactory.init(CsvSchemaConstants.DEFAULT_LAPTOPS_CSV, CsvSchemaConstants.DEFAULT_OVENS_CSV);
    }

    @Test
    @DisplayName("Test CSV loading and mapping for laptops and ovens")
    void testCsvLoading() throws DaoException {
        Dao<Laptop> laptopDao = DaoFactory.getInstance(Laptop.class);
        List<Laptop> laptops = laptopDao.findAll();
        assertEquals(TestConstants.EXPECTED_TOTAL_LAPTOPS, laptops.size());

        Laptop firstLaptop = laptops.get(CsvSchemaConstants.INDEX_ID);
        assertEquals(TestConstants.LAPTOP_ID_1, firstLaptop.getId());
        assertEquals(TestConstants.LAPTOP_NAME_VIVOBOOK, firstLaptop.getName());
        assertEquals(TestConstants.LAPTOP_PRICE_45K, firstLaptop.getPrice());
        assertEquals(TestConstants.LAPTOP_OS_WINDOWS, firstLaptop.getOs());
        assertEquals(TestConstants.LAPTOP_CPU_I7, firstLaptop.getCpu());

        Dao<Oven> ovenDao = DaoFactory.getInstance(Oven.class);
        List<Oven> ovens = ovenDao.findAll();
        assertEquals(TestConstants.EXPECTED_TOTAL_OVENS, ovens.size());

        Oven firstOven = ovens.get(CsvSchemaConstants.INDEX_ID);
        assertEquals(TestConstants.OVEN_ID_1, firstOven.getId());
        assertEquals(TestConstants.OVEN_NAME_BOSCH, firstOven.getName());
        assertEquals(TestConstants.OVEN_PRICE_32K, firstOven.getPrice());
        assertEquals(TestConstants.OVEN_POWER_2500, firstOven.getPower());
        assertEquals(TestConstants.OVEN_CAPACITY_66, firstOven.getCapacity());
    }

    @Test
    @DisplayName("Test DAO filtering via SearchCriteria")
    void testDaoFiltering() throws DaoException {
        Dao<Laptop> laptopDao = DaoFactory.getInstance(Laptop.class);
        SearchCriteria<Laptop> criteria = new SearchCriteria<>(Laptop.class);
        criteria.addCaseInsensitiveStringMatch(EntityConstants.FIELD_NAME, Laptop::getName, TestConstants.LAPTOP_NAME_VIVOBOOK);

        List<Laptop> filtered = laptopDao.find(criteria);
        assertEquals(TestConstants.EXPECTED_ONE_ITEM, filtered.size());
        assertEquals(TestConstants.LAPTOP_NAME_VIVOBOOK, filtered.get(CsvSchemaConstants.INDEX_ID).getName());

        SearchCriteria<Laptop> priceCriteria = new SearchCriteria<>(Laptop.class);
        priceCriteria.addRangeFilter(EntityConstants.FIELD_PRICE, Laptop::getPrice, new Range<>(TestConstants.PRICE_FILTER_LOW_60K, TestConstants.PRICE_FILTER_HIGH_90K));
        List<Laptop> priceFiltered = laptopDao.find(priceCriteria);
        assertEquals(TestConstants.EXPECTED_PRICE_FILTERED_COUNT_2, priceFiltered.size());
    }

    @Test
    @DisplayName("Test DaoException on missing CSV file")
    void testDaoExceptionOnMissingFile() {
        CsvDataSource<Laptop> missingDataSource = new CsvDataSource<>(
                TestConstants.NON_EXISTENT_FILE,
                new LaptopCsvRowMapper());
        Dao<Laptop> failingDao = new DaoImpl<>(missingDataSource);

        assertThrows(DaoException.class, failingDao::findAll);
        assertThrows(DaoException.class, () -> failingDao.find(new SearchCriteria<>(Laptop.class)));
    }
}