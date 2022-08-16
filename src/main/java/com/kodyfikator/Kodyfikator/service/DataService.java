package com.kodyfikator.Kodyfikator.service;

import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;

public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public DataRow getDataRow(String code) {
        Long id = findByCode(code).getId();
        return dataRepository.getReferenceById(id);
    }

    public DataRow findByCode(String code) {
        List<DataRow> dataList = dataRepository.findByCode(code);
        if (!dataList.isEmpty()) return dataList.get(0);
        else return null;
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
