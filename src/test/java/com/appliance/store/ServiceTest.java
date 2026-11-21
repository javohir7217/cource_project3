package com.appliance.store;

import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.EntityConstants;
import com.appliance.store.constant.TestConstants;
import com.appliance.store.criteria.Range;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.dal.dao.Dao;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.dal.factory.DaoFactory;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.entity.Laptop;
import com.appliance.store.entity.Oven;
import com.appliance.store.service.ApplianceService;
import com.appliance.store.service.exception.ServiceException;
import com.appliance.store.service.factory.ServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceTest {
    private ApplianceService service;

    @BeforeEach
    void setUp() {
        DaoFactory.init(CsvSchemaConstants.DEFAULT_LAPTOPS_CSV, CsvSchemaConstants.DEFAULT_OVENS_CSV);
        ServiceFactory.reset();
        service = ServiceFactory.getInstance();
    }

    @Test
    @DisplayName("Test find single entity contract")
    void testFindSingleEntity() throws ServiceException {
        SearchCriteria<Laptop> criteria = new SearchCriteria<>(Laptop.class);
        criteria.addCaseInsensitiveStringMatch(EntityConstants.FIELD_NAME, Laptop::getName, TestConstants.LAPTOP_NAME_VIVOBOOK);

        List<Laptop> result = service.find(criteria);
        assertEquals(TestConstants.EXPECTED_ONE_ITEM, result.size());
        assertEquals(TestConstants.LAPTOP_NAME_VIVOBOOK, result.get(CsvSchemaConstants.INDEX_ID).getName());
    }

    @Test
    @DisplayName("Test findAll merging Laptop and Oven results")
    void testFindAllMerged() throws ServiceException {
        SearchCriteria<Laptop> laptopCriteria = new SearchCriteria<>(Laptop.class);
        laptopCriteria.addRangeFilter(EntityConstants.FIELD_PRICE, BaseEntity::getPrice, new Range<>(TestConstants.MERGED_PRICE_LOW_18K, TestConstants.MERGED_PRICE_HIGH_45K));

        SearchCriteria<Oven> ovenCriteria = new SearchCriteria<>(Oven.class);
        ovenCriteria.addRangeFilter(EntityConstants.FIELD_PRICE, BaseEntity::getPrice, new Range<>(TestConstants.MERGED_PRICE_LOW_18K, TestConstants.MERGED_PRICE_HIGH_45K));

        List<BaseEntity> result = service.findAll(List.of(laptopCriteria, ovenCriteria));
        assertEquals(TestConstants.EXPECTED_FILTERED_ALL_ITEMS, result.size());

        boolean hasLaptop = false;
        boolean hasOven = false;
        for (BaseEntity item : result) {
            if (item instanceof Laptop) {
                hasLaptop = true;
            } else if (item instanceof Oven) {
                hasOven = true;
            }
        }
        assertTrue(hasLaptop);
        assertTrue(hasOven);
    }

    @Test
    @DisplayName("Test calculateCost for single criteria and multiple criteria")
    void testCalculateCost() throws ServiceException {
        SearchCriteria<Laptop> allLaptopsCriteria = new SearchCriteria<>(Laptop.class);
        long laptopCost = service.calculateCost(allLaptopsCriteria);
        assertEquals(TestConstants.EXPECTED_TOTAL_LAPTOP_COST, laptopCost);

        SearchCriteria<Laptop> singleItemCriteria = new SearchCriteria<>(Laptop.class);
        singleItemCriteria.addCaseInsensitiveStringMatch(EntityConstants.FIELD_NAME, Laptop::getName, TestConstants.LAPTOP_NAME_VIVOBOOK);
        long vivobookCost = service.calculateCost(singleItemCriteria);
        assertEquals(TestConstants.EXPECTED_VIVOBOOK_COST, vivobookCost);

        SearchCriteria<Laptop> laptopRangeCriteria = new SearchCriteria<>(Laptop.class);
        laptopRangeCriteria.addRangeFilter(EntityConstants.FIELD_PRICE, BaseEntity::getPrice, new Range<>(TestConstants.MERGED_PRICE_LOW_18K, TestConstants.MERGED_PRICE_HIGH_45K));
        SearchCriteria<Oven> ovenRangeCriteria = new SearchCriteria<>(Oven.class);
        ovenRangeCriteria.addRangeFilter(EntityConstants.FIELD_PRICE, BaseEntity::getPrice, new Range<>(TestConstants.MERGED_PRICE_LOW_18K, TestConstants.MERGED_PRICE_HIGH_45K));

        long combinedCost = service.calculateCost(List.of(laptopRangeCriteria, ovenRangeCriteria));
        assertEquals(TestConstants.EXPECTED_COMBINED_COST_200K, combinedCost);
    }

    @Test
    @DisplayName("Test DaoException wrapping into ServiceException")
    void testDaoExceptionWrappedInServiceException() {
        Dao<Laptop> failingDao = new Dao<>() {
            @Override
            public List<Laptop> findAll() throws DaoException {
                throw new DaoException(TestConstants.DAO_FAILURE_MESSAGE);
            }

            @Override
            public List<Laptop> find(SearchCriteria<Laptop> criteria) throws DaoException {
                throw new DaoException(TestConstants.DAO_FAILURE_MESSAGE);
            }
        };

        DaoFactory.init(Map.of(Laptop.class, failingDao));

        SearchCriteria<Laptop> criteria = new SearchCriteria<>(Laptop.class);
        ServiceException exception = assertThrows(ServiceException.class, () -> service.find(criteria));
        assertNotNull(exception.getCause());
        assertInstanceOf(DaoException.class, exception.getCause());
        assertEquals(TestConstants.DAO_FAILURE_MESSAGE, exception.getMessage());
    }
}
