package com.appliance.store;

import com.appliance.store.constant.CommandConstants;
import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.TestConstants;
import com.appliance.store.controller.Controller;
import com.appliance.store.controller.ControllerImpl;
import com.appliance.store.controller.factory.ControllerFactory;
import com.appliance.store.controller.model.RequestImpl;
import com.appliance.store.controller.model.Response;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.dal.factory.DaoFactory;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.service.ApplianceService;
import com.appliance.store.service.exception.ServiceException;
import com.appliance.store.service.factory.ServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {
    private Controller controller;

    @BeforeEach
    void setUp() {
        DaoFactory.init(CsvSchemaConstants.DEFAULT_LAPTOPS_CSV, CsvSchemaConstants.DEFAULT_OVENS_CSV);
        ServiceFactory.reset();
        ControllerFactory.reset();
        controller = ControllerFactory.getInstance();
    }

    @Test
    @DisplayName("Test exit command asserts isOut() == true")
    void testExitCommand() {
        Response response = controller.execute(new RequestImpl(TestConstants.COMMAND_EXIT));
        assertTrue(response.isOk());
        assertTrue(response.isOut());
        assertEquals(CommandConstants.EXIT_RESPONSE_MESSAGE, response.responseString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "finde",
            "find",
            "find unknown",
            "find all price=abc",
            "find all price=20000;10000",
            "find laptops unknownField=1"
    })
    @DisplayName("Test malformed commands assert isOk() == false && isOut() == false")
    void testMalformedCommands(String commandString) {
        Response response = controller.execute(new RequestImpl(commandString));
        assertFalse(response.isOk());
        assertFalse(response.isOut());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "find laptops | [Laptop{id=1, name='ASUS Vivobook', price=45000, os='Windows', cpu='Intel i7'}, Laptop{id=2, name='Apple MacBook Pro', price=120000, os='macOS', cpu='Apple M2'}, Laptop{id=3, name='Dell XPS 13', price=85000, os='Windows', cpu='Intel i5'}, Laptop{id=4, name='Lenovo ThinkPad', price=65000, os='Linux', cpu='AMD Ryzen 7'}, Laptop{id=5, name='HP Pavilion', price=35000, os='Windows', cpu='Intel i3'}]",
            "find laptops name=\"ASUS Vivobook\" | [Laptop{id=1, name='ASUS Vivobook', price=45000, os='Windows', cpu='Intel i7'}]",
            "find all price=18000;45000 | [Laptop{id=1, name='ASUS Vivobook', price=45000, os='Windows', cpu='Intel i7'}, Laptop{id=5, name='HP Pavilion', price=35000, os='Windows', cpu='Intel i3'}, Oven{id=1, name='Bosch Serie 4', price=32000, power=2500, capacity=66}, Oven{id=2, name='Samsung Dual Cook', price=42000, power=3000, capacity=75}, Oven{id=3, name='Electrolux SurroundCook', price=28000, power=2000, capacity=58}, Oven{id=4, name='Gorenje Essential', price=18000, power=1800, capacity=50}]",
            "cost laptops | 350000"
    })
    @DisplayName("Test valid commands against exact deterministic outputs")
    void testValidCommands(String commandString, String expectedOutput) {
        Response response = controller.execute(new RequestImpl(commandString));
        assertTrue(response.isOk());
        assertFalse(response.isOut());
        assertEquals(expectedOutput, response.responseString());
    }

    @Test
    @DisplayName("Test service unavailable forces ServiceException and asserts isOk() == false && isOut() == true")
    void testServiceUnavailable() {
        ApplianceService failingService = new ApplianceService() {
            @Override
            public <T extends BaseEntity> List<T> find(SearchCriteria<T> criteria) throws ServiceException {
                throw new ServiceException(TestConstants.SERVICE_UNAVAILABLE_MESSAGE);
            }

            @Override
            public List<BaseEntity> findAll(List<SearchCriteria<? extends BaseEntity>> criteriaList) throws ServiceException {
                throw new ServiceException(TestConstants.SERVICE_UNAVAILABLE_MESSAGE);
            }

            @Override
            public long calculateCost(SearchCriteria<? extends BaseEntity> criteria) throws ServiceException {
                throw new ServiceException(TestConstants.SERVICE_UNAVAILABLE_MESSAGE);
            }

            @Override
            public long calculateCost(List<SearchCriteria<? extends BaseEntity>> criteriaList) throws ServiceException {
                throw new ServiceException(TestConstants.SERVICE_UNAVAILABLE_MESSAGE);
            }
        };

        Controller failingController = new ControllerImpl(failingService);
        Response response = failingController.execute(new RequestImpl(TestConstants.COMMAND_FIND_LAPTOPS));
        assertFalse(response.isOk());
        assertTrue(response.isOut());
        assertEquals(TestConstants.SERVICE_UNAVAILABLE_MESSAGE, response.responseString());
    }
}

