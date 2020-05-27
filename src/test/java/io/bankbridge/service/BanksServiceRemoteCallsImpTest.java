package io.bankbridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.bankbridge.model.BankModel;
import io.bankbridge.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BanksServiceRemoteCallsImpTest {

    @Mock
    BanksServiceRemoteCallsImp service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void init() throws Exception {

        //service
        service.init();

        //assertions - config initialized
        assertNotNull(BanksServiceRemoteCallsImp.config);

        /*
        contains banks-v2.json name/url values
         */
        String name = "Royal Bank of Boredom";
        String url = "http://localhost:1234/rbb";

        //assertions
        assertTrue(BanksServiceRemoteCallsImp.config.containsKey(name));
        assertEquals(BanksServiceRemoteCallsImp.config.get(name), url);

    }

    @Test
    void getBanks() throws JsonProcessingException {

        //given - mock remote calls
        String name = "Royal Bank of Boredom";
        String bic = "5678";
        {
            List<BankModel> banks = new ArrayList<>();
            banks.add(new BankModel("1234", name, "GB", "OAUTH"));
            banks.add(new BankModel(bic, "Credit Sweets", "CH", "OpenID"));

            when(service.getBanks()).thenReturn(JsonUtil.objectToJson(banks));
        }

        //service
        String jacksonStr =
                service.getBanks();

        //assertions
        assertNotNull(jacksonStr);
        assertTrue(jacksonStr.contains(name));
        assertTrue(jacksonStr.contains(bic));

    }
}