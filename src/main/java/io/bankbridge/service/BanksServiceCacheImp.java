package io.bankbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import io.bankbridge.util.JsonUtil;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BanksServiceCacheImp implements BanksService {

    private static final int HEAP_SIZE_FOR_CACHE = 10;

    //Public as being used in test scenarios
    public static final String CACHE_NAME = "cacheForBanks";
    public static final String FILE_NAME = "banks-v1.json";
    public static CacheManager cacheManager;

    public static void init() throws Exception {

        //build cache
        cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().withCache(CACHE_NAME,
                        CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(String.class, String.class,
                                        ResourcePoolsBuilder.heap(HEAP_SIZE_FOR_CACHE)))
                .build();

        //init cache
        cacheManager.init();

        //get cache
        Cache cache = cacheManager.getCache(CACHE_NAME, String.class, String.class);

        try {

            //Read file values
            BankModelList models = new ObjectMapper().readValue(
                    Thread.currentThread().getContextClassLoader().getResource(FILE_NAME), BankModelList.class);

            //put cache
            for (BankModel model : models.banks) {
                cache.put(model.getBic(), model.getName());
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public String getBanks() {

        try {

            //initialize
            init();

            //retrieve from cache as map
            List<Map> result = new ArrayList<>();
            cacheManager.getCache(CACHE_NAME, String.class, String.class).forEach(entry -> {
                Map map = new HashMap<>();
                map.put("id", entry.getKey());
                map.put("name", entry.getValue());
                result.add(map);
            });

            //send banks
            //String resultAsString = new ObjectMapper().writeValueAsString(result);
            return JsonUtil.objectToJson(result);

        } catch (Exception e) {
            throw new RuntimeException("Error while processing request.");
        }
    }

}
