package com.hw.service;

import com.hw.domain.DBProperty;
import com.hw.service.bean.TableMetadata;
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

    public List<TableMetadata> getMetaExcel(DBProperty property) throws SQLException {
        List<TableMetadata> metadataList = new ArrayList<>();

        Connection con = getConnection(property);

        DatabaseMetaData meta = con.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, null, new String[]{"TABLE"});

        while (resultSet.next()) {
            metadataList.add(new TableMetadata(resultSet, meta, true));
        }

        con.close();
        return metadataList;
    }

    private Connection getConnection(DBProperty property) throws SQLException {
        String url = String.format(property.getDbType().getUrl(), property.getIp(), property.getPort(), property.getDbName());
        return DriverManager.getConnection(url, property.getUsername(), property.getPassword());
    }
}
