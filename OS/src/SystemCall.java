import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SystemCall {

	
	public SystemCall() {
		
	}

//	1. Read the data of any file from the disk. D
//	2. Write text output to a file in the disk. D
//	3. Print data on the screen. D
//	4. Take text input from the user. D
//	5. Reading data from memory.
//	6. Writing data to memory.
	
	public void print(Object start,Object end) {
		if(start instanceof String)
		{
	        if (((String)start).charAt(0)>((String)end).charAt(0)) {
	            System.out.println("Invalid input: start character should be smaller than or equal to end character.");
	            return;
	        }

	        for (char ch = ((String)start).charAt(0); ch <= ((String)end).charAt(0); ch++) {
	            System.out.print(ch + " ");
	        }
		}
		else  //int
			for(int i = (int)start ; i<=(int)end;i++) {
				System.out.println(i);
			}
	}
	
	public void print(Object a) {
		System.out.println(a.toString());
	}
	
	public Object readUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a value: ");
         String a = scanner.next();
         try {
        	 int b = Integer.parseInt(a);
        	 return b ;
         }catch(Exception e) {
        	 return a ;
         }
    }
	
	 public static void writeFile(String filename, String data) {
	        try {
	        	File file = new File(filename);
				if(!file.exists()) 
					file.createNewFile();
	            FileWriter writer = new FileWriter(filename, true);
	            writer.write(data);
	            writer.close();
	            System.out.println("Data written to file successfully.");
	        } catch (IOException e) {
	            System.out.println("An error occurred while writing to the file.");
	            e.printStackTrace();
	        }
	    }
	
	 public static String readFile(String filename) {
         String result ="" ;  
		 try {
	            BufferedReader reader = new BufferedReader(new FileReader(filename));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                result+=line ;
	            }
	            reader.close();
	        } catch (IOException e) {
	            System.out.println("An error occurred while reading the file.");
	            e.printStackTrace();
	        }
		 return result ;
	    }
	
}
