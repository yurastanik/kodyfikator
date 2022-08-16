package com.kodyfikator.Kodyfikator.parser;

import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.service.DataService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelParser {

    private final DataService dataService;

    public ExcelParser(DataService dataService) {
        this.dataService = dataService;
    }

    public String getValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return cell.getNumericCellValue() + "";
            default: return "";
        }
    }

    public String correctName(String category, String name, String parent_name) {
        name = clearWhiteSpaces(name);
        if ("B".equals(category)) {
            return String.format("%s район міста %s", name, parent_name.replace("м. ", ""));
        }
        return name;
    }

    public String correctName(String category, String name) {
        name = clearWhiteSpaces(name);
        switch (category) {
            case "O": if (!Objects.equals(name, "Автономна Республіка Крим")) return String.format("%s область", name); else return name;
            case "P": return String.format("%s район", name);
            case "H": return String.format("%s територіальна громада", name);
            case "K":
            case "M": return String.format("м. %s", name);
            case "T": return String.format("смт. %s", name);
            case "C":
            case "X":
                return String.format("с. %s", name);
            default: return name;
        }
    }

    public static String clearWhiteSpaces(String name) {
        String[] arrayName = name.split("\\s+");
        StringBuilder endName = new StringBuilder();
        for (String word : arrayName) {
            if (endName.toString().isEmpty()) endName.append(word);
            else endName.append(" ").append(word);
        }
        return endName.toString();
    }

//    public void parse(String fileName) {
//        InputStream inputStream;
//        XSSFWorkbook workBook = null;
//        try {
//            inputStream = new FileInputStream(fileName);
//            workBook = new XSSFWorkbook(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert workBook != null;
//        Sheet sheet = workBook.getSheetAt(0);
//        getData(sheet);
//    }

    public List<DataRow> parse(byte[] file) {
        InputStream inputStream;
        SXSSFWorkbook workBook = null;
        try {
            inputStream = new ByteArrayInputStream(file);
            workBook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert workBook != null;
        Sheet sheet = workBook.getXSSFWorkbook().getSheetAt(0);
        return getData(sheet);
    }


    public List<DataRow> getData(Sheet sheet) {
        Iterator<Row> it = sheet.rowIterator();
        DataRow dataRow = new DataRow();
        while (it.hasNext()) {
            Row row = it.next();
            if (row.getRowNum() > 2) {
                if (row.getRowNum() % 5000 == 0) {
                    System.out.println(row.getRowNum());
                }
                if (getValue(row.getCell(0)).length() != 19) break;
                try {
                    for (int i = 4; i >= 0; i--) {
                        Cell cell = row.getCell(i);
                        if (!getValue(cell).isEmpty()) {
                            dataRow.setId((long) (row.getRowNum() - 2));
                            dataRow.setCode(getValue(cell));
                            if (i != 0) {
                                DataRow rowData = this.dataService.findByCode(getValue(row.getCell(i - 1)));
                                dataRow.setParent_id(rowData.getId());
                                if (i + 1 == 5) dataRow.setName(correctName(getValue(row.getCell(5)), getValue(row.getCell(6)), rowData.getName()));
                                else dataRow.setName(correctName(getValue(row.getCell(5)), getValue(row.getCell(6))));
                            }
                            else {
                                dataRow.setName(correctName(getValue(row.getCell(5)), getValue(row.getCell(6))));
                            }
                            dataRow.setLevel(i + 1);
//                            this.dataService.saveRow(dataRow);
                            break;
                        }
                    }
                } catch (java.lang.OutOfMemoryError ex) {
                    ex.printStackTrace();
                }
            }
        }
        return dataService.findAll();
    }
}
