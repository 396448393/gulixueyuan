package com.guli.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;

public class ExcelImportUtil {
    private InputStream file = null;

    public ExcelImportUtil(InputStream file){
        this.file=file;
    }
    public Sheet getSheet() throws IOException {
        Workbook workbook = new HSSFWorkbook(this.file);
        Sheet sheet = workbook.getSheetAt(0);
        return sheet;
    }

    public String getCellValue(Cell levelOneCell) {
        return levelOneCell.getStringCellValue();
    }
}
