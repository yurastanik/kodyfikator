package com.kodyfikator.Kodyfikator.controller;

import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.parser.ExcelParser;
import com.kodyfikator.Kodyfikator.service.DataService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/get")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(value="/division/{division_code}", method=RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getDivision(@PathVariable(value = "division_code") String code) {
        if (!Helper.check(code)) return Helper.getResponse(HttpStatus.BAD_REQUEST, "error", "Request contains malformed parameter");
        List<DataRow> dataList = getByCode(code);
        if (dataList.isEmpty()) return Helper.getResponse(HttpStatus.NOT_FOUND, "error", "Not Found");
        JSONArray divisions = new JSONArray();
        for (DataRow dataRow : dataList) divisions.put(createObj(dataRow));
        return Helper.getResponse(HttpStatus.OK, "divisions", divisions.toList());
    }

    @RequestMapping(value="/division", method=RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getDivisions() {
        JSONArray divisions = new JSONArray();
        for (DataRow dataRow : this.dataService.findByParent_id(null)) divisions.put(createObj(dataRow));
        return Helper.getResponse(HttpStatus.OK, "divisions", divisions.toList());
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postReq() {
        return Helper.getResponse(HttpStatus.METHOD_NOT_ALLOWED, "error", "Method Not Allowed");
    }

    @RequestMapping(value = "/updateData", method = RequestMethod.GET)
    public List<DataRow> updateData() {
        ExcelParser excelParser = new ExcelParser(this.dataService);
        return excelParser.parse("kodyfikator-1.xlsx");
    }

    private JSONObject createObj(DataRow dataRow) {
        JSONObject division = new JSONObject();
        division.put("code", dataRow.getCode());
        division.put("name", dataRow.getName());
        if (!this.dataService.findByParent_id(dataRow.getId()).isEmpty())
            division.put("childs", "/get/division/" + dataRow.getCode());
        return division;
    }

    public List<DataRow> getByCode(String code) {
        DataRow dataRow = this.dataService.findByCode(code);
        if (dataRow == null)
            return Collections.emptyList();
        Long par_id = dataRow.getId();
        List<DataRow> dataList = this.dataService.findByParent_id(par_id);
        if (dataList.size() > 0) {
            return dataList;
        }
        return Collections.emptyList();
    }
}

class Helper {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean check(String code) {
        return (code.length() == 19 && code.contains("UA") && isNumeric(code.replace("UA", "")));
    }

    public static ResponseEntity<Map<String, Object>> getResponse(HttpStatus httpStatus, String key, String value) {
        return new ResponseEntity<>(new JSONObject().put(key, value).toMap(), httpStatus);
    }

    public static ResponseEntity<Map<String, Object>> getResponse(HttpStatus httpStatus, String key, List<Object> value) {
        return new ResponseEntity<>(new JSONObject().put(key, value).toMap(), httpStatus);

    }
}
