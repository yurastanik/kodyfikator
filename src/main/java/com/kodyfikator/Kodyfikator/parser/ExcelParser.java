package com.kodyfikator.Kodyfikator.parser;

import com.kodyfikator.Kodyfikator.model.DataRow;
import com.kodyfikator.Kodyfikator.service.DataService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    public String correctName(String category, String name) {
        switch (category) {
            case "M": return String.format("м. %s", name);
            case "Т": return String.format("смт. %s", name);
            case "C":
            case "X":
                return String.format("с. %s", name);
            default: return name;
        }
    }


    public List<DataRow> parse(String fileName) {
        InputStream inputStream;
        XSSFWorkbook workBook = null;
        try {
            inputStream = new FileInputStream(fileName);
            workBook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert workBook != null;
        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> it = sheet.rowIterator();

        ArrayList<DataRow> dataList = new ArrayList<>();

        while (it.hasNext()) {
            Row row = it.next();
            if (row.getRowNum() > 2) {
                if (row.getRowNum() % 5000 == 0) {
                    System.out.println(row.getRowNum());
                }
                if (getValue(row.getCell(0)).length() != 19) break;
                for (int i = 4; i >= 0; i--) {
                    Cell cell = row.getCell(i);
                    if (!getValue(cell).isEmpty() && !this.dataService.isExist(getValue(cell))) {
                        DataRow dataRow = new DataRow();
                        dataRow.setId((long) (row.getRowNum() - 2));
                        dataRow.setCode(getValue(cell));
                        if (i != 0) dataRow.setParent_id(this.dataService.findByCode(getValue(row.getCell(i - 1))).getId());
                        dataRow.setName(correctName(getValue(row.getCell(5)), getValue(row.getCell(6))));
                        dataRow.setLevel(i + 1);
                        dataList.add(dataService.saveRow(dataRow));
                    }
                }
            }
        }
        return dataList;
    }
}
