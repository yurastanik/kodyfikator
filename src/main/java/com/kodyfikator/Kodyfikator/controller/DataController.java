package com.kodyfikator.Kodyfikator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kodyfikator.Kodyfikator.exception.BadRequest;
import com.kodyfikator.Kodyfikator.exception.CodeNotFound;
import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.parser.ExcelParser;
import com.kodyfikator.Kodyfikator.response.ResponseHandler;
import com.kodyfikator.Kodyfikator.service.DataService;
import com.kodyfikator.Kodyfikator.utils.CodeChecker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/get")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(value="/division/{division_code}", method=RequestMethod.GET)
    public ResponseEntity<Object> getDivision(@PathVariable(value = "division_code") String code) throws CodeNotFound, BadRequest {
        if (!CodeChecker.check(code)) throw new BadRequest(code);
        List<DataRow> dataList = getByCode(code);
        if (dataList.isEmpty()) throw new CodeNotFound(code);
        JSONArray divisions = new JSONArray();
        for (DataRow dataRow : dataList) divisions.put(createObj(dataRow));
        return ResponseHandler.getResponse("divisions", divisions.toList());
    }

    @RequestMapping(value="/division", method=RequestMethod.GET)
    public ResponseEntity<Object> getDivisions() {
        JSONArray divisions = new JSONArray();
        for (DataRow dataRow : this.dataService.findByParent_id(null)) divisions.put(createObj(dataRow));
        return ResponseHandler.getResponse("divisions", divisions.toList());
    }


    @RequestMapping(value = "/updateData")
    public String uploadFile() {
        return "uploadform";
    }


    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelParser excelParser = new ExcelParser(this.dataService);
        excelParser.parse(file.getBytes());
        return "redirect:/get/division";
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
