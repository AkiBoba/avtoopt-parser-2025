package com.example.armtek_parcer_new.aliance.controller;

import com.example.armtek_parcer_new.aliance.facade.FacadeAlianceAPI;
import com.example.armtek_parcer_new.aliance.domain.FileLine;
import com.example.armtek_parcer_new.aliance.service.FileParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1")
@Controller
@RequiredArgsConstructor
public class UploadFileController {
    private final FileParser fileParser;
    private final FacadeAlianceAPI facadeAPI;

    @PostMapping("/upload")
    public ResponseEntity<String> handleUpload(@RequestParam("excel-file") MultipartFile file,
            HttpServletRequest request) throws IOException, InterruptedException {
        List<FileLine> lines = fileParser.getFileLines(file);
        facadeAPI.getWorks(lines);
        return ResponseEntity.ok().body("Файл успешно отправлен!");
    }


}