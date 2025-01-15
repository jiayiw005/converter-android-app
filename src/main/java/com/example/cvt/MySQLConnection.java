package com.example.cvt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private MySQLConnection() throws Exception {
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver"); //load driver
            String ip = "rm-bp17284v07kya0q44zo.rwlb.rds.aliyuncs.com";//address of cloud database
            conn =(Connection) DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":3306/converter_demo",//database name
                    "manager1", "wjy050929-");//database account and password
        } catch (SQLException ex) {//catch exception
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;//return connection
    }

}

