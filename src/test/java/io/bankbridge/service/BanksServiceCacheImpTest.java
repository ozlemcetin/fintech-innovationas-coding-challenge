package io.bankbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModelList;
import org.ehcache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanksServiceCacheImpTest {

    BanksServiceCacheImp service;

    @BeforeEach
    void setUp() {
        service = new BanksServiceCacheImp();
    }

    @Test
    void init() throws Exception {

        //service
        service.init();

        //assertions - cacheManager initialized
        assertNotNull(BanksServiceCacheImp.cacheManager);

        //check cache
        Cache cache = BanksServiceCacheImp.cacheManager.
                getCache(BanksServiceCacheImp.CACHE_NAME, String.class, String.class);

        //assertions
        assertNotNull(cache);

        //does the cache have the values?
        BankModelList models = new ObjectMapper().readValue(
                Thread.currentThread().getContextClassLoader().getResource(BanksServiceCacheImp.FILE_NAME),
                BankModelList.class);

        //assertions - cache.get
        models.banks.forEach(entry -> assertEquals(cache.get(entry.getBic()), entry.getName()));

    }

    @Test
    void getBanks() throws Exception {

        //service
        String jacksonStr =
                service.getBanks();

        /*
        contains banks-v1.json id/name values
         */
        String bic = "1234";
        String name = "Royal Bank of Boredom";


        //assertions
        assertNotNull(jacksonStr);
        assertTrue(jacksonStr.contains(bic));
        assertTrue(jacksonStr.contains(name));

    }

}