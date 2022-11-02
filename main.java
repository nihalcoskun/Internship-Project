import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

 
public class Project {	
	
	public static String findUndefinedMerchant (String filePath) throws Exception {
		
		//privacy
		String dbUrl = "-------------------------";
		String username = "-------";
		String password = "--------";
		
		Connection connection;
		Statement statement = null;
		StringBuilder str =new StringBuilder();  
		try{
		 connection = DriverManager.getConnection(dbUrl, username, password);
		 statement = connection.createStatement();
//		 str.append("Connected to Database\n"); 
		}
		catch(SQLException e){
		System.out.println("Error!");
		e.printStackTrace();
		}	  
		
		try 
		 {
	          File sourceFile = new File (filePath);
		  String fileName = sourceFile.getName();
				 
		  Scanner scanner = new Scanner(sourceFile);
		  ArrayList<String> notFounds = new ArrayList<String>();
		  ArrayList<String> currencies = new ArrayList<String>();
		  String merchant = null;
	       	  String currency = null;
 		  while(scanner.hasNextLine())	
		  {
		     String line = scanner.nextLine();
		     String [] split = line.split(",");
									
		     if(fileName.startsWith("Z5"))
		     {
						 	
			if(split.length >= 18) 
			{			
			   if(split[18].isEmpty())
			   {
				merchant = null;
			   }
			   else 
			   {
				   
				merchant = split[18].substring(4);
				if(merchant.startsWith("0"))
				{
					merchant = merchant.substring(1);
										}
					if(split[4].isEmpty())
			         	{
						currency = null;
					}
					else
		            			currency = split[4];
				  }
			   }
							
		     }
						
		     if(fileName.startsWith("Z1"))
		     {
						 
			 if(split[0].contentEquals("01") && split.length >= 2) 
			 {
				merchant = split[2];
								   
				if(split.length >= 13)
				currency = split[13];
							   							   
			  }					    	 					    	 
					    	 					    	 
		      }
		     
		      if(fileName.startsWith("AX"))
		      {
						 
			  if( split[0].contentEquals("\"TRANSACTN\"") && split.length >= 6) 
			   {			
				   if(split[6].isEmpty())
				   {
					merchant = null;
				   }
			   else
	         	   {
				merchant = split[6].substring(1,11);
				if(split[10].isEmpty())
				{
	            			currency = null;
				}
				else
					currency = split[10].substring(1,4);
									   }
			   }
						    	 
						    	 
	               }
					     						
				 			 				     	
				 
					  
		      if(merchant != null )
		      { 	
 		    	 ResultSet set = null;
			 if(fileName.startsWith("Z5"))
		    	 {
			   set = statement.executeQuery("SELECT * FROM SAP_MERCHANT_MASTER WHERE MERCHANT_ID = '" + merchant + "' AND SETTLEMENT_CURRENCY = '" + currency + "'" );
		    	 }
			 if(fileName.startsWith("Z1") || fileName.startsWith("AX") )
		    	 {
	               	   set = statement.executeQuery("SELECT * FROM SAP_MERCHANT_MASTER WHERE MERCHANT_ID = '" + merchant + "' AND CURRENCY = '" + currency + "'" );
		    	 }
					    	
	               if(!set.next())
	               {	
	            	if(!notFounds.contains(merchant)) 
		          {
		            	notFounds.add(merchant);
		            	currencies.add(currency);
			 }
		        }           
		     }
					       

				       }
				 
				 if(!notFounds.isEmpty()) {
				 
				 str.append("TANIMSIZ "  + notFounds.size() + " ADET MID ");
			
				 String Z5File = "Z5FILE_TANIMSIZMERCHANTLIST";
				 String Z1File = "Z1FILE_TANIMSIZMERCHANTLIST";
				 String AXFile = "AXFILE_TANIMSIZMERCHANTLIST";
				 
				 File file = null;
				 
				 try {
					  
					  if(fileName.startsWith("Z5"))
					  {	
						  //System.out.println(sourceFile.getParent());
					      file = new File(sourceFile.getParent() + "\\"+ Z5File + "_" + fileName );
						  //System.out.println(file.getPath());
						 // System.out.println(file.getName());
					      
					  }
					  if(fileName.startsWith("Z1"))
					  {
					      file = new File(sourceFile.getParent() + "\\"+ Z1File + "_" + fileName);
					  }
					  if(fileName.startsWith("AX"))
					  {
					      file = new File(sourceFile.getParent() + "\\"+ AXFile + "_" + fileName);
					  }
					  
					  
				      if (file.createNewFile()) {
				        str.append(" DOSYA OLUŞTURULDU: " + file.getName());
				      } else {
				        str.append(file.getName());
				      }
				    } catch (IOException e) {
				      str.append(" DOSYA OLUŞTURULAMADI. ");
				      e.printStackTrace();
				    }
			
				 try {
					 
					 FileWriter myWriter = null;				

				     myWriter = new FileWriter(file.getPath());
					
				      				      
				      for (int i = 0; i < notFounds.size(); i++) 
					   {
					      myWriter.write(notFounds.get(i) + "  " + currencies.get(i) + "\n");

					   }
				      
				      myWriter.close();
				      str.append(" DOSYAYA YAZDIRILDI. ");
				    } catch (IOException e) {
				      str.append(" DOSYAYA YAZDIRILAMADI. ");
				      e.printStackTrace();
				    }				 
				 
				 	     
				}	
				 
				 else
					 str.append("TANIMSIZ MID BULUNAMADI. DOSYA OLUŞTURULMADI");
		 
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
		
		System.out.println(str.toString());
		return str.toString();
	}
}		
