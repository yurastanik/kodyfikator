package com.kodyfikator.Kodyfikator.controller;

import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.parser.ExcelParser;
import com.kodyfikator.Kodyfikator.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(value = "/getAllData", method = RequestMethod.GET)
    public List<DataRow> getAllData() {
        return this.dataService.findAll();
    }

    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    public List<DataRow> getData(@RequestParam(required = false, name = "code") String code) {
        if (code == null) return this.dataService.findByParent_id(null);
        else if (this.dataService.findByCode(code) != null) return getByCode(code);
        return Collections.emptyList();
    }

    @RequestMapping(value = "/updateData", method = RequestMethod.GET)
    public List<DataRow> updateData() {
        ExcelParser excelParser = new ExcelParser(this.dataService);
        return excelParser.parse("kodyfikator-1.xlsx");
    }

    public List<DataRow> getByCode(String code) {
        Long par_id = this.dataService.findByCode(code).getId();
        List<DataRow> dataList = this.dataService.findByParent_id(par_id);
        if (dataList.size() > 0) {
            return dataList;
        }
        return Collections.emptyList();
    }
}
