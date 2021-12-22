package sql;
//批量插入文本文件内容
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//import com.mysql.cj.xdevapi.Statement;

public class txt {
	 public static ArrayList<String> getfile(String filepath){  
         try{  
           String temp = null;  
           File f = new File(filepath);  
          
           InputStreamReader read = new InputStreamReader(new FileInputStream(f),"utf-8");  
           ArrayList<String> readList = new ArrayList<String>();   
           BufferedReader reader=new BufferedReader(read);   
           //bufReader = new BufferedReader(new FileReader(filepath));  
           while((temp=reader.readLine())!=null &&!"".equals(temp)){  
               readList.add(temp);  
           }  
           read.close();  
           return readList;
           
           }catch (Exception e) {  
           e.printStackTrace();   
           }
       return null;     
       }   
 public static void comptxt(String path1, String path2) throws Exception {

		// BufferedReader in = null;
		// BufferedReader in2 = null;
		 LocalDateTime myObj = LocalDateTime.now();
		 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		 String formattedDate = myObj.format(myFormatObj);
		
		 //from hebing;
		 BufferedReader br1 = null;
		 BufferedReader br2 = null;
		 BufferedWriter br3 = null;
		 BufferedWriter br4 = null;
		 BufferedWriter br5 = null;
		 String sCurrentLine;
		 int linelength;
		 List<String> list1 = new ArrayList<String>();
		 List<String> list2 = new ArrayList<String>();
		 List<String> unexpectedrecords = new ArrayList<String>();
		 List<String> newrecords = new ArrayList<String>();
		 List<String> hisrecords = new ArrayList<String>();

		 br1 = new BufferedReader(new FileReader(path1));

		 br2 = new BufferedReader(new FileReader(path2));

		    while ((sCurrentLine = br1.readLine()) != null) {
		        list1.add(sCurrentLine);
		    }
		    while ((sCurrentLine = br2.readLine()) != null) {
		        list2.add(sCurrentLine);
		    }
		    List<String> expectedrecords = new ArrayList<String>(list1);//original

		    List<String> actualrecords = new ArrayList<String>(list2);//compare

		    if (expectedrecords.size() > actualrecords.size()) {
		        linelength = expectedrecords.size();
		    } else {
		        linelength = actualrecords.size();
		    }
		    int j=0;
		    String[] spliter = new String[list2.size()];
		    list2.toArray(spliter);
		    String[] splited= list2.toString().split(",");//.split(",");
		    //}
		    
	    	String[] filename = new String[linelength];
	    	String[] value = new String[linelength]   ;
	    	String[] path = new String[linelength];
	    	//filename[j] = spliter[0];
	    	for (int i = 0; i <linelength; i++) {
	    		
	    		filename[i] = splited[i*3];
	    		value[i] = splited[i*3+1];
	    		path[i] = splited[i*3+2];
	    		
	    	}
	    	
	    	for(int i1 =0; i1 < spliter.length ; i1++){
		    	if(expectedrecords.toString().contains(filename[i1])  && path[i1].contains("woocommerce")==false && path[i1].contains("contact-form-7")==false) {
		    		// System.out.println(filename[i1]);
		    		 unexpectedrecords.add(actualrecords.get(i1));   			 
		    	}
		    	if(expectedrecords.toString().contains(filename[i1]) && expectedrecords.toString().contains(value[i1])==false && path[i1].contains("woocommerce")==false && path[i1].contains("contact-form-7")==false) {
		    		// System.out.println(filename[i1]);
		    		 newrecords.add(expectedrecords.get(i1) + ","+ value[i1] + ","+ path[i1]+"\r");   
		    		 hisrecords.add(linelength + ","+ newrecords.size() + ","+ filename[i1] + ","+ formattedDate +"\r");  
		    	
		    	}
		    }

		    br3 = new BufferedWriter(new FileWriter(new File("result.txt")));
		    br3.write("");
		    for (int x = 0; x < unexpectedrecords.size(); x++) {
		        br3.write(unexpectedrecords.get(x));
		        br3.newLine();
		    }
		    
		    br4 = new BufferedWriter(new FileWriter(new File("newfiles.txt")));
		    br4.write("");
		    for (int x = 0; x < newrecords.size(); x++) {
		        br4.write(newrecords.get(x));
		        br4.newLine();
		    }
		    
		    br5 = new BufferedWriter(new FileWriter(new File("Query_history.txt")));
		    br5.write("");
		    if( hisrecords.size()>0) {
		    for (int x = 0; x < hisrecords.size(); x++) {
		    	br5.write(hisrecords.get(x));
		    	br5.newLine();
		    }
		        
		    }
		    else {
		    	 br5.write( linelength + ","+ "0"+ "," +"none"+ ","+formattedDate);
		    }
		   // br3.write("Records which are in actual but no present in expected");
		   // for (int i1 = 0; i1 < actualrecords.size(); i1++) {
		   //     br3.write(actualrecords.get(i1));
		   //     br3.newLine();
		   // }
		    br5.flush();
		    br5.close();
		    br4.flush();
		    br4.close();
		    br3.flush();
		    br3.close();
		    br1.close();
		    br2.close();
		    
}
		    
	  public static void main(String[] args)  throws Exception {
		 	LocalDateTime myObj = LocalDateTime.now();
		 	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		 	String formattedDate = myObj.format(myFormatObj);  
		  String path1 = "MD5_Original.txt";//original file
		    //List list = readTxtFile(path1);
		    String path2 = "MD5_Monitor.txt";//monitor file 
		    String path3 ="result.txt";
		  try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
//	        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
	            //localhost:3306/demo_jdbc
	           // String url = "jdbc:mysql://173.255.211.73:3306/projectdata";
	           String url = "jdbc:mysql://localhost:3306/projectdata";
	            String user = "pymysql";// MD5test jing
	            String password = "6lO/]u(zWpBQUa5A";//Yoh@4(jVKJuyjFam  yRfaPhwcuS0mIlt/

	            //connect to sql
	            Connection conn=DriverManager.getConnection(url, user, password);
	            System.out.println("successfully connected");
	            //sql insert
	            //String sql="insert into MD5(filename,ori_MD5,new_MD5,path) values('license2.txt','123456','76893','var/lib')";
	            ArrayList<String> list=getfile("MD5_Monitor.txt");
	           // ArrayList<String> list=getfile("C:\\Users\\admin\\Documents\\NYIT\\870\\test\\MD5_Original.txt");  
	          // ArrayList<String> list2=getfile("result.txt");
	            //ArrayList<String> list2=getfile("C:\\Users\\admin\\Documents\\NYIT\\870\\test\\result.txt"); 
	            //List<String> list1=new ArrayList<String>();
	            int num=list.size();
	            for (int i = 0; i < num; i++) {
	                //System.out.println(list.get(i));
	                
	                if (list.get(i)!=null ) {
	                    String[] s=list.get(i).split(",");
	                  //  String[] s2=list2.get(i).split(",");
	             
	                //   String sql = "insert into File_compare(filename,New_value,Original_value,Querydate,IF_IS_NEW) values('"+s[0]+"','" +s[1] + "','"+s2[1] + "','" + formattedDate + "','" + s2.equals(s) + "')";
	                String sql = "insert into File_Monitor(File_name,New_value,path,Query_date) values('"+s[0]+"','" +s[1] + "','"+s[2] + "','"+ formattedDate + "')";
	                    Statement stmt=conn.createStatement();//Statement
	                   stmt.executeUpdate(sql);//update
	                   System.out.println("Successfully update to File_Monitor database : " + s[0]);
	                }
	                
	           // 
	           // System.out.println("successfully closed");
	        }
	            comptxt(path1,path2);  
	           // compline(path1,path3); 
	            ArrayList<String> list21=getfile("newfiles.txt");
	            ArrayList<String> list22=getfile("Query_history.txt"); 
	            int num21=list21.size();
	            if(num21>0) {
	            for (int i = 0; i < num21; i++) {
	                //System.out.println(list.get(i));
	                
	                if (list21.get(i)!=null) {
	                    String[] s=list21.get(i).split(",");
	                    String[] s22=list22.get(i).split(","); 

	                   if(s[2] != s[1] ) 
	                  //s[],
	                   { String sql = "insert into File_detected(File_name,Original_value,New_value,path,Query_date) values('"+s[0]+"','" + s[1]+"','" +s[3] + "','"  +s[4] +"','" +formattedDate +"')";
	                   String sql1 = "insert into File_Monitor_history(Query_time,Total_Number_of_files,No_of_Detected_file,File_detected ) values('" + s22[3] +"','" + s22[0]+"','" +s22[1] +"','" + s22[2] + "')"; 
	                   Statement stmt=conn.createStatement();//Statement
	                   stmt.executeUpdate(sql);
	                   stmt.executeUpdate(sql1);
	                   //update
	                   System.out.println("Detecte out new file:" + s[0]);
	                }

	            }  
	               
	            
	        }
	            }
	            else {
	                String[] s22=list22.get(0).split(","); 	
	                String sql1 = "insert into File_Monitor_history(Query_time,Total_Number_of_files,No_of_Detected_file,File_detected) values('" + s22[3] +"','" + s22[0]+"','" +s22[1] +"','" + s22[2] + "')"; 
	                Statement stmt=conn.createStatement();//Statemen
	                stmt.executeUpdate(sql1);
	                }
               
               
               // Statement stmt=conn.createStatement();
               
	            
	     conn.close();   
		  
		  }catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }//
	        catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }

}
