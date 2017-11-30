package com.hw.controller;

import com.hw.domain.DBProperty;
import com.hw.service.DBMetaToExcelService;
import com.hw.service.bean.TableMetadata;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by HeWei on 2017/11/29.
 * .
 */
@Controller
@RequestMapping(value = "/dbProperty/{id}")
public class DBExcelController {

    private DBMetaToExcelService dbMetaToExcelService;

    @Autowired
    public DBExcelController(DBMetaToExcelService dbMetaToExcelService) {
        this.dbMetaToExcelService = dbMetaToExcelService;
    }

    @GetMapping("/meta")
    public ResponseEntity getMeta(@PathVariable(name = "id") DBProperty dbProperty) throws SQLException {
        List<TableMetadata> metadataList = dbMetaToExcelService.getMeta(dbProperty, true);
        return ResponseEntity.ok().body(metadataList);
    }

    @GetMapping("/metaExcel")
    public void getMetaExcel(@PathVariable(name = "id") DBProperty dbProperty, HttpServletResponse response) throws SQLException, IOException {
        String fileName = URLEncoder.encode(dbProperty.getDbName() + ".xlsx", "UTF-8");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

        SXSSFWorkbook wb = dbMetaToExcelService.getMetaExcel(dbProperty, true);
        wb.write(response.getOutputStream());
        wb.close();
    }

}
