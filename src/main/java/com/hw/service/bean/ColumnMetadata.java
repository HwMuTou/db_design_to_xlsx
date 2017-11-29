/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.hw.service.bean;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * JDBC column metadata
 *
 * @author Christoph Sturm
 */
@Data
public class ColumnMetadata {
    private final String name;
    private final String typeName;
    private final int columnSize;
    private final int decimalDigits;
    private final String isNullable;
    private final int typeCode;
    private final String columnDef;
    private final Boolean primaryKey;
    private final String remarks;

    ColumnMetadata(ResultSet rs, Map<String, PrimaryKeyMetaData> primaryKeys) throws SQLException {
        name = rs.getString("COLUMN_NAME");
        columnSize = rs.getInt("COLUMN_SIZE");
        decimalDigits = rs.getInt("DECIMAL_DIGITS");
        isNullable = rs.getString("IS_NULLABLE");
        typeCode = rs.getInt("DATA_TYPE");
        columnDef = rs.getString("column_def");
        remarks = rs.getString("remarks");
        typeName = new StringTokenizer(rs.getString("TYPE_NAME"), "() ").nextToken();

        if (primaryKeys.get(name) != null) {
            primaryKey = true;
        } else {
            primaryKey = false;
        }

    }
}
