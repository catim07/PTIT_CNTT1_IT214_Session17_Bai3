package com.rikkei.b3;

import com.rikkei.b3.service.ProductConsistencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisConsistencyEvictApplicationTests {

    @Autowired
    private ProductConsistencyService service;

    @Test
    void testCacheEvictionsOnUpdateAndDelete() {
        assertEquals("Laptop", service.getProduct(100L));
        assertEquals(1, service.getQueryCount());

        // Update product -> evicts cache
        service.updateProduct(100L, "Gaming Laptop");

        // Query again -> cache miss -> queries DB again
        assertEquals("Gaming Laptop", service.getProduct(100L));
        assertEquals(2, service.getQueryCount());

        // Delete product -> evicts cache
        service.deleteProduct(100L);

        // Query again -> returns NotFound
        assertEquals("NotFound", service.getProduct(100L));
        assertEquals(3, service.getQueryCount());
    }
}
