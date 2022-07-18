package com.kodyfikator.Kodyfikator.repository;

import com.kodyfikator.Kodyfikator.model.DataRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataRepository extends JpaRepository<DataRow, Long> {
    List<DataRow> findByCode(String code);
    List<DataRow> findByParentId(Long level);
    boolean existsByCode(String code);
}
