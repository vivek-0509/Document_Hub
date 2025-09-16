package com.sstinternaltools.sstinternal_tools.security.service.interfaces;

public interface ExcelEmailChecker {
    boolean isEmailInExcel(String emailToSearch, String excelFilePath);
}