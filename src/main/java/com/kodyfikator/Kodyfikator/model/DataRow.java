package com.kodyfikator.Kodyfikator.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "data")
public class DataRow {

    @Id
    private Long id;

    private String code;

    @JoinColumn(name = "parent_id")
    private Long parentId;

    private String name;

    private int level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParent_id() {
        return parentId;
    }

    public void setParent_id(Long parent_id) {
        this.parentId = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Objects.equals(((DataRow) obj).getId(), this.getId())) return false;
        else if (!Objects.equals(((DataRow) obj).getName(), this.getName())) return false;
        else if (!Objects.equals(((DataRow) obj).getLevel(), this.getLevel())) return false;
        else if (!Objects.equals(((DataRow) obj).getParent_id(), this.getParent_id())) return false;
        else return Objects.equals(((DataRow) obj).getCode(), this.getCode());
    }
}
