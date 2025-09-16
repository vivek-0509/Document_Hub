package com.sstinternaltools.sstinternal_tools.security.service.implementation;

import com.sstinternaltools.sstinternal_tools.security.service.interfaces.ExcelEmailChecker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ExcelEmailCheckerImpl implements ExcelEmailChecker {

    @Override
    public boolean isEmailInExcel(String emailToSearch, String excelFilePath) {
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(excelFilePath)) {

            if (fis == null) {
                throw new IOException("Excel file not found in classpath: " + excelFilePath);
            }

            try (Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0); // Read the first sheet

                for (Row row : sheet) {
                    Cell cell = row.getCell(0); // Assuming emails are in the first column (index 0)

                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String email = cell.getStringCellValue().trim();
                        if (email.equalsIgnoreCase(emailToSearch)) {
                            return true; // Email found
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Email not found
    }
}