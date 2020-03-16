package cn.lixing.uilts;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static cn.lixing.uilts.CloseUilt.*;
public class PropertiesFileUilt {
	public static String getData(String keyNanem,String fileName) {
		String classPath=System.getProperty("user.dir");
		Properties ps=new Properties();
		BufferedInputStream in=null;
		try {
			in=new BufferedInputStream(
					new FileInputStream(classPath+fileName+".properties"));
			ps.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeFile(in);
		return ps.getProperty(keyNanem);
	}
//	public static void main(String[] args) {
//		System.out.println(System.getProperty("file.separator"));
//		//System.out.println(getData("mysqldriverClass", "\\configFile\\db"));
//	}
}
