package com.example.armtek_parcer_new.service;

import com.example.armtek_parcer_new.domain.FileLine;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileParser {

    /**
     * Метод для преобразования содержимого файла в список объектов FileLine
     *
     * @param file объект файла, полученный от клиента
     * @return список объектов FileLine, содержащих номера, имена и коды
     */
    public List<FileLine> getFileLines(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) { // Используем XSSFWorkbook для .xlsx файлов

            Sheet sheet = workbook.getSheetAt(0); // Берём первый лист документа
            List<FileLine> result = new ArrayList<>();
            int num = 1;

            for (Row row : sheet) {
                Cell cellName = row.getCell(0);
                    result.add(new FileLine(num++, cellName.getStringCellValue()));
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла.", e);
        }
    }
}
