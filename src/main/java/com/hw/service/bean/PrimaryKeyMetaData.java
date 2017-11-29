package com.hw.service.bean;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by HeWei on 2017/11/29.
 * .
 */
@Data
public class PrimaryKeyMetaData {
    private String pkName;
    private String tableSchema;
    private String keySeq;
    private String columnName;
    private String tableCat;
    private String tableName;

    public PrimaryKeyMetaData(ResultSet rs) throws SQLException {
        this.pkName = rs.getString("pk_name");
        this.tableSchema = rs.getString("table_schem");
        this.keySeq = rs.getString("key_seq");
        this.columnName = rs.getString("column_name");
        this.tableCat = rs.getString("table_cat");
        this.tableName = rs.getString("table_name");
    }
}
