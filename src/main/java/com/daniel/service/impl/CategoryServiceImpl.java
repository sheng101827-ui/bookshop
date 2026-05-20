package com.daniel.service.impl;

import com.daniel.dao.CategoryDAO;
import com.daniel.pojo.Category;
import com.daniel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.alibaba.fastjson.JSON;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    JedisPool jedisPool;

    @Override
    public List<Category> list() {
        return categoryDAO.list();
    }

    @Override
    public List<Category> getAllCategories() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String categoriesJson = jedis.get("categoryList");
            if (categoriesJson != null && !categoriesJson.isEmpty()) {
                return JSON.parseArray(categoriesJson, Category.class);
            } else {
                List<Category> categories = categoryDAO.list();
                if (categories != null) {
                    jedis.set("categoryList", JSON.toJSONString(categories));
                }
                return categories;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
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
        List<Category> categories = getAllCategories();
        if (categories == null) {
            return null;
        }
        Map<Integer, String> categoriesMap = new HashMap<>();
        for (Category category : categories) {
            categoriesMap.put(category.getId(),category.getName());
        }
        return categoriesMap;
    }
}
