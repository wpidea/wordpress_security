package filemodify;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class filemonitor
{
 /**
     * calculate MD5 value for files in the folder
*/
    public static String md5OfFile(File file) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fs = new FileInputStream(file);
        BufferedInputStream bs = new BufferedInputStream(fs);
       // File file2 = new File("C:\\Users\\admin\\Documents\\NYIT\\870\\test\\abc.txt");
       // FileWriter fw = new FileWriter(file2); 
        byte[] buffer = new byte[1024];
        int bytesRead;

        while((bytesRead = bs.read(buffer, 0, buffer.length)) != -1){
           md.update(buffer, 0, bytesRead);
        }
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for(byte bite : digest){
            sb.append(String.format("%02x", bite & 0xff));
        }
       // fw.write(file.getName()+ "," +sb.toString()+ "," +file.getAbsolutePath()+"\r\n");  
        return sb.toString();
    }
    public static Set<String> getFilePath(File file, Set<String> filePaths){
        File[] files = file.listFiles();
        if (files==null||files.length==0){
            return new HashSet<>();
        }
        for (File file1 : files) {
            if (file1.isDirectory()){
                //递归调用
                getFilePath(file1, filePaths);
            }else {
                //保存文件路径到集合中
                filePaths.add(file1.getAbsolutePath());
            }
        }
        return filePaths;
    }

    public static void findfolder(File file) throws Exception {
        
        //File[] files = dir.listFiles();
 
       //File file2 = new File("C:\\Users\\admin\\Documents\\NYIT\\870\\test\\abc.txt");
       File file2 = new File("MD5_Original.txt");
       FileWriter fw = new FileWriter(file2,true); 
       // BufferedWriter bWriter=new BufferedWriter(fw);
       
    	   if (file != null) 
          	{
          	if(file.isFile())
          		{
          			String newvalue = md5OfFile(file);
          			System.out.println(newvalue);
          			fw.append(file.getName()+ "," +newvalue+ "," +file.getAbsolutePath()+"\r\n"); 

          		}
    		else
    		{
    			File[] subfiles =file.listFiles();
           		 for (File flie:subfiles) 
           		 {
           			 findfolder(flie);
                    }
            }
       } 	
               
        fw.close();
 }
    
    public static void main(String[] args) throws Exception
    {

    	File dir = new File("wordpress"); 

    	findfolder(dir);
	
}
}

