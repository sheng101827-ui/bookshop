package com.daniel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.daniel.common.Result;
import com.daniel.common.ResultGenerator;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dataImport")
public class DataImportController {

    private static final Logger log = Logger.getLogger(DataImportController.class);

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result importData(@RequestBody String jsonStr) {
        try {
            log.info("request: data/import , json: " + jsonStr);
            
            Object parsedData = JSON.parseObject(jsonStr, Object.class, Feature.SupportAutoType);
            
            log.info("parsed object type: " + (parsedData != null ? parsedData.getClass().getName() : "null"));
            
            return ResultGenerator.genSuccessResult(parsedData);
        } catch (Exception e) {
            log.error("import data failed", e);
            return ResultGenerator.genFailResult("数据解析失败：" + e.getMessage());
        }
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public Result importBatchData(@RequestBody String jsonStr) {
        try {
            log.info("request: data/import/batch , json: " + jsonStr);
            
            List<Object> parsedDataList = new ArrayList<>();
            
            if (jsonStr.trim().startsWith("[")) {
                List<Object> list = JSON.parseArray(jsonStr, Object.class, Feature.SupportAutoType);
                parsedDataList.addAll(list);
            } else {
                Object obj = JSON.parseObject(jsonStr, Object.class, Feature.SupportAutoType);
                parsedDataList.add(obj);
            }
            
            for (Object obj : parsedDataList) {
                log.info("parsed object type: " + (obj != null ? obj.getClass().getName() : "null"));
            }
            
            return ResultGenerator.genSuccessResult(parsedDataList);
        } catch (Exception e) {
            log.error("import batch data failed", e);
            return ResultGenerator.genFailResult("数据解析失败：" + e.getMessage());
        }
    }
}
