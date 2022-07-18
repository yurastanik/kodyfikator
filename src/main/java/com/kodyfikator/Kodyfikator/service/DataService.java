package com.kodyfikator.Kodyfikator.service;

import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public boolean isExist(String code) {
        return dataRepository.existsByCode(code);
    }

    public DataRow findByCode(String code) {
        if (!isExist(code)) return null;
        return dataRepository.findByCode(code).get(0);
    }

    public List<DataRow> findByParent_id(Long parent_id) {
        return dataRepository.findByParentId(parent_id);
    }

    public List<DataRow> findAll() {
        return dataRepository.findAll();
    }

    public DataRow saveRow(DataRow row) {
        return dataRepository.save(row);
    }
}
