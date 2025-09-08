package com.example.armtek_parcer_new.service;

import com.example.armtek_parcer_new.domain.FileLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
public class ExcelUtil {

    /**
     * Экспортирует ошибки в Excel
     *
     * @param errorsLog Набор сообщений об ошибках
     */
    public void exportErrorsToExcel(Set<FileLine> errorsLog) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Errors lines");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Номер в таблице");
            headerRow.createCell(1).setCellValue("Наименование");
            headerRow.createCell(2).setCellValue("Код 1С");
            headerRow.createCell(3).setCellValue("Код поставщика");

            int rowIndex = 1;
            for (FileLine line : errorsLog) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(line.getNumber());
                row.createCell(1).setCellValue(line.getName());
            }

            try (FileOutputStream outputStream = new FileOutputStream("errors.xlsx")) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            log.error("Ошибка экспорта журнала ошибок в Excel:", e);
        }
    }
}
