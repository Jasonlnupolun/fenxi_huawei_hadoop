package cn.dinfo.cpic.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {
	public static String readFile(String dir) throws IOException{
		FileReader reader=new FileReader(new File(dir));
		char[] temp=new char[1000];
		int len=0;
		StringBuffer sb=new StringBuffer("");
		while((len=reader.read(temp))!=-1){
			sb.append(new String(temp));
		}
		return sb.toString();
	}
	public static void main(String[] args) throws IOException {
	}
}
