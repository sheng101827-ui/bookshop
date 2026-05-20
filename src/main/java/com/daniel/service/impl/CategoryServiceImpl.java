package com.daniel.service.impl;

import com.alibaba.fastjson.JSON;
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

    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    JedisPool jedisPool;

    private static final String CACHE_KEY = "category:list";

    @Override
    public List<Category> list() {
        try (Jedis jedis = jedisPool.getResource()) {
            String cached = jedis.get(CACHE_KEY);
            if (cached != null) {
                return JSON.parseArray(cached, Category.class);
            }
            List<Category> categories = categoryDAO.list();
            jedis.set(CACHE_KEY, JSON.toJSONString(categories));
            return categories;
        } catch (Exception e) {
            return null;
        }
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
    public Map<Integer, String> listByMap() {
        List<Category> categories = categoryDAO.list();
        Map<Integer, String> categoriesMap = new HashMap<>();
        for (Category category : categories) {
            categoriesMap.put(category.getId(),category.getName());
        }
        return categoriesMap;
    }
}
