package com.hw.controller;

import com.hw.domain.DBProperty;
import com.hw.service.DBMetaToExcelService;
import com.hw.service.bean.TableMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/metaExcel")
    public ResponseEntity getMetaExcel(@PathVariable(name = "id") DBProperty dbProperty) throws SQLException {
        List<TableMetadata> metadataList = dbMetaToExcelService.getMetaExcel(dbProperty);
        return ResponseEntity.ok().body(metadataList);
    }

}
