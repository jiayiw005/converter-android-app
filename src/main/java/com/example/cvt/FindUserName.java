package com.example.cvt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FindUserName {
    public static Connection con = null;
    static int id;
    static String userName;

    public FindUserName() {
    }

    public String find(int userid){
        id = userid;

        FindUserName.Threads_match threads_match = new FindUserName.Threads_match();
        threads_match.start();
        try {
            threads_match.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return userName;
    }

    class Threads_match extends Thread{
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                String sqlSelect = "Select * from user_detail where userid like (?) ";
                PreparedStatement pstmt = con.prepareStatement(sqlSelect);
                pstmt.setInt(1,id);
                ResultSet rset = pstmt.executeQuery();
                while(rset.next()) {
                    userName = rset.getString("username");
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
