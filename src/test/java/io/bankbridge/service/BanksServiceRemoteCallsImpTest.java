package io.bankbridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModel;
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

        //init method call
        BanksServiceRemoteCallsImp.init();

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

            BankModel bankModel1 = new BankModel("1234", name, "GB", "OAUTH");
            banks.add(bankModel1);

            BankModel bankModel2 = new BankModel(bic, "Credit Sweets", "CH", "OpenID");
            banks.add(bankModel2);

            String resultAsString = new ObjectMapper().writeValueAsString(banks);
            when(service.getBanks()).thenReturn(resultAsString);
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