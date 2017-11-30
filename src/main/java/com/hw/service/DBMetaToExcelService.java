package com.hw.service;

import com.hw.domain.DBProperty;
import com.hw.service.bean.TableMetadata;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeWei on 2017/11/29.
 * .
 */
@Service
public class DBMetaToExcelService {

    private int rowAccessWindowSize = 1000;
    private String[] columnName = new String[]{"名称", "类型", "长度", "小数点", "键", "默认值", "空", "注释"};
    private String[] indexCName = new String[]{"名称", "字段", "索引方法", "唯一键"};

    public List<TableMetadata> getMeta(DBProperty property, boolean extras) throws SQLException {
        List<TableMetadata> metadataList = new ArrayList<>();

        Connection con = getConnection(property);

        DatabaseMetaData meta = con.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, null, new String[]{"TABLE"});

        while (resultSet.next()) {
            metadataList.add(new TableMetadata(resultSet, meta, extras));
        }

        con.close();
        return metadataList;
    }

    public SXSSFWorkbook getMetaExcel(DBProperty property, boolean extras) throws SQLException {
        List<TableMetadata> metadataList = getMeta(property, extras);

        SXSSFWorkbook wb = new SXSSFWorkbook(rowAccessWindowSize);
        metadataList.forEach(tableMetadata -> {
            Sheet sh = wb.createSheet(tableMetadata.getName());

            sh.createRow(sh.getFirstRowNum()).createCell(0).setCellValue("Fields");

            Row rowColumnName = sh.createRow(sh.getLastRowNum() + 1);
            for (int index = 0; index < columnName.length; index++) {
                rowColumnName.createCell(index).setCellValue(columnName[index]);
            }

            tableMetadata.getColumns().forEach((columnName, columnMetadata) -> {
                Row rowNext = sh.createRow(sh.getLastRowNum() + 1);
                rowNext.createCell(0).setCellValue(columnName);
                rowNext.createCell(1).setCellValue(columnMetadata.getTypeName());
                rowNext.createCell(2).setCellValue(columnMetadata.getColumnSize());
                rowNext.createCell(3).setCellValue(columnMetadata.getDecimalDigits());
                rowNext.createCell(4).setCellValue(columnMetadata.getPrimaryKey());
                rowNext.createCell(5).setCellValue(columnMetadata.getColumnDef());
                rowNext.createCell(6).setCellValue(columnMetadata.getIsNullable());
                rowNext.createCell(7).setCellValue(columnMetadata.getRemarks());
            });

            sh.createRow(sh.getFirstRowNum()).createCell(0).setCellValue("Index");

            Row rowIndexName = sh.createRow(sh.getLastRowNum() + 1);
            for (int index = 0; index < indexCName.length; index++) {
                rowIndexName.createCell(index).setCellValue(indexCName[index]);
            }

            tableMetadata.getIndexes().forEach((indexName, indexMetadata) -> {
                Row rowNext = sh.createRow(sh.getLastRowNum() + 1);
                rowNext.createCell(0).setCellValue(indexName);
                rowNext.createCell(1).setCellValue(indexMetadata.getColumnName());
                rowNext.createCell(2).setCellValue(indexMetadata.getType());
                rowNext.createCell(3).setCellValue(indexMetadata.getUnique());
            });
        });

        return wb;
    }

    private Connection getConnection(DBProperty property) throws SQLException {
        String url = String.format(property.getDbType().getUrl(), property.getIp(), property.getPort(), property.getDbName());
        return DriverManager.getConnection(url, property.getUsername(), property.getPassword());
    }
}
