package com.hw.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by HeWei on 2017/11/29.
 * .
 */
@Data
@Table
@Entity
public class DBProperty implements Serializable {

    private static final long serialVersionUID = -3525241671140027983L;

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String ip;

    @Column
    private Integer port;

    @Column
    private String dbName;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private DBType dbType;

    public enum DBType {

        mysql("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC"),
        postgres("jdbc:postgresql://%s:%d/%s"),
        sqlServer("jdbc:sqlserver://%s:%d/%s");

        private String url = null;

        DBType(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
