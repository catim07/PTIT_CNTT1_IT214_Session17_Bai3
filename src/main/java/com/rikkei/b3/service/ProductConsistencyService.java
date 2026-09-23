package com.rikkei.b3.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductConsistencyService {
    private final Map<Long, String> productDb = new ConcurrentHashMap<>();
    private final AtomicInteger queryCounter = new AtomicInteger(0);

    public ProductConsistencyService() {
        productDb.put(100L, "Laptop");
    }

    @Cacheable(value = "products", key = "#id")
    public String getProduct(Long id) {
        queryCounter.incrementAndGet();
        return productDb.getOrDefault(id, "NotFound");
    }

    @CacheEvict(value = "products", key = "#id")
    public void updateProduct(Long id, String newName) {
        productDb.put(id, newName);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productDb.remove(id);
    }

    public int getQueryCount() {
        return queryCounter.get();
    }
}
