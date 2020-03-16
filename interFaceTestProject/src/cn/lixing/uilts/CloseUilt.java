package cn.lixing.uilts;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CloseUilt {
	public static void closeFile(Closeable ... io) {
		for(Closeable temp:io) {
			try {
				temp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeConnection(Connection conn){
        //�ر�Connection����
        if(conn != null){
            try{
                conn.close();
                conn = null;
            }catch(SQLException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
	}
	
	public static void closeResultSet(ResultSet result){
        //�ر�ResultSet����
        if(result != null){
            try{
            	result.close();
            	result = null;
            }catch(SQLException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
	
	public static void closePreparedStatement(PreparedStatement pmt){
        //�ر�PreparedStatement����
        if(pmt != null){
            try{
            	pmt.close();
            	pmt = null;
            }catch(SQLException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
