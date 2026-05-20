package com.daniel.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.daniel.dao.CategoryDAO;
import com.daniel.pojo.Category;
import com.daniel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService{

    private static final String CATEGORY_CACHE_KEY = "category:all";

    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Category> list() {
        return categoryDAO.list();
    }

    @Override
    public Category get(int id) {
        return categoryDAO.get(id);
    }

    @Override
    public void update(Category category) {
        categoryDAO.update(category);
    }

    @Override
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    @Override
    public int count() {
        return categoryDAO.count();
    }

    @Override
    public Map<Integer, String> getAllCategories() {
        try (Jedis jedis = jedisPool.getResource()) {
            String cacheValue = jedis.get(CATEGORY_CACHE_KEY);
            if (cacheValue != null && !cacheValue.isEmpty()) {
                return JSON.parseObject(cacheValue, new TypeReference<Map<Integer, String>>() {
                });
            }
            List<Category> categories = categoryDAO.list();
            Map<Integer, String> categoriesMap = new HashMap<>();
            for (Category category : categories) {
                categoriesMap.put(category.getId(), category.getName());
            }
            jedis.setex(CATEGORY_CACHE_KEY, 300, JSON.toJSONString(categoriesMap));
            return categoriesMap;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<Integer, String> listByMap() {
        return getAllCategories();
    }
}
