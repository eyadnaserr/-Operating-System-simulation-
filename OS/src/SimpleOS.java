import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

class SimpleOS{
	int timeSlice ;
	Integer processInCPU = null ;
	 static int serialID = 0 ;
	 Object[] Memory = new Object[40];
	 Queue General_Blocked_Queue = new Queue(3);
	 Queue File_Blocked_Queue = new Queue(3);
	 Queue Input_Blocked_Queue = new Queue(3);
	 Queue Output_Blocked_Queue = new Queue(3);
	 Queue Ready_Queue = new Queue(3);
	 boolean blocked = false ;
	 
	 Mutex file = new Mutex("file",1);
	 Mutex input =  new Mutex("userInput",1);
	 Mutex output = new Mutex("userOutput",1);
	
	//private static Map<String, Semaphore> resources = new HashMap<>();

	private static boolean canBeValue(String a) {
		try {
			int b = Integer.parseInt(a);
		}
		catch(Exception e) {
			if(!a.equals("input")&&!a.equals("readFile"))
				return true ;
			
			else
				return false ;
		}
		return true ;
	}
	
	 public void BlockProcess(int ID) {
		 if(ID== Integer.parseInt(Memory[0]+""))
				{
			 	Memory[1] = State.Blocked;
			 	processInCPU = null ;
			 	printQueues();
				}
			else if(ID== Integer.parseInt(Memory[5]+""))
				{
					Memory[6] = State.Blocked;
				 	processInCPU = null ;
					printQueues();
				}
			else
				System.out.print("7aga 8lt");
		 General_Blocked_Queue.enqueue(ID);
	 }
	 
	 public void setValueToAssign(Object a,int ID) {
		 //find the process
		 if(Integer.parseInt(Memory[0]+"")==ID) {
			String inst = (String) Memory[Integer.parseInt(Memory[2]+"")] ;
			String result ="";
			String[] tokens = inst.split(" ");
			result+=tokens[0];
			result+=" ";
			result+=tokens[1];
			result+= " ";
			result+=""+a;
			Memory[Integer.parseInt(Memory[2]+"")] = result ;
			Memory[2]= Integer.parseInt(Memory[2]+"")-1;
		 }else
			 if(Integer.parseInt(Memory[5]+"")==ID) {
				String inst = (String) Memory[Integer.parseInt(Memory[7]+"")]; 
				String result ="";
				String[] tokens = inst.split(" ");
				result+=tokens[0];
				result+=" ";
				result+=tokens[1];
				result+= " ";
				result+=""+a;
				Memory[Integer.parseInt(Memory[7]+"")] = result ;
				Memory[7]=Integer.parseInt(Memory[7]+"")-1 ;
			 }
	 }
	 
	 
	 public void setValToVar(String varName,Object a , int ID) {
		 System.out.println("in the variable part ");
		 display();
		 System.out.println(Memory[22]+" "+Memory[23]+" "+Memory[24]);
		 if(Integer.parseInt(Memory[0]+"")==ID) {
			if((Memory[22]+"").equals(null+"")) {
				(Memory[22]) = new Variable(varName,a);
			}
			else
			if((Memory[23]+"").equals(null+"")) {
				(Memory[23]) = new Variable(varName,a);				
			}
			else
			if((Memory[24]+"").equals(null+"")) {
				(Memory[24]) = new Variable(varName,a);
			}	
			else 
				System.out.print("this variable already exists");
		 }else if(Integer.parseInt(Memory[5]+"")==ID) {
			 if((Memory[37]+"").equals(null+"")) {
					(Memory[37]) = new Variable(varName,a);
				}
			 else if((Memory[38]+"").equals(null+"")) {
					(Memory[38]) = new Variable(varName,a);				
				}
			 else if((Memory[39]+"").equals(null+"")) {
					(Memory[39]) = new Variable(varName,a);
				}
			 else 
					System.out.print("this variable already exists");

		 }
		 else System.out.println("can find the process in the memory!!!");
		 
	 }
	
	 public void checklastInstSetStateFin(int process_ID) {
		 if(Memory[5]!=null&&process_ID==Integer.parseInt(Memory[0]+"")&&Integer.parseInt(Memory[2]+"")==Integer.parseInt(Memory[4]+"")) {
			 Memory[1] = State.Finished;
			 printQueues();
			 }
		 if(Memory[5]!=null && process_ID==Integer.parseInt(Memory[5]+"")&&Integer.parseInt(Memory[7]+"")==Integer.parseInt(Memory[9]+"")) {
			 Memory[6]=State.Finished;
			 printQueues();
		 }

	 }
	 
	 
	 
	public  void executeInstruction(String instruction, int process_ID) throws IOException {
		if(instruction==null) return ;
		if(instruction.equals("null")) return ;
		checklastInstSetStateFin(process_ID);
			
		String[] tokens = instruction.split(" ");
        String command = tokens[0];
        
        /*acquire  -->blocked
         * print  -->system call
         * release
         * nested inst
        */
        System.out.println("pc = "+Memory[2]);
        System.out.print("process "+process_ID);
		System.out.println(" instruction to be executed: "+instruction);
		switch (command) {
		case "print" : 
			if(process_ID==output.getProcess_ID()) {
				SystemCall sc = new SystemCall();
				sc.print(tokens[1]);
				}
			else
				System.out.println("trying to take print without having the mutex of that resource");
			break;
		case "assign" : 
			
			if(canBeValue(tokens[2])) {
				setValToVar(tokens[1],tokens[2],process_ID);
			}
			else
				if(tokens[2].equals("input")) {
					if(process_ID==input.getProcess_ID())
					{
						SystemCall sc1 = new SystemCall();
						Object temp = sc1.readUserInput();
						setValueToAssign(temp,process_ID);
					}
					else
						System.out.println("trying to take an input without having the mutex of that resource");
					
				}else
					if(tokens[2].equals("readFile")) {
						
						if(process_ID==input.getProcess_ID())
						{
							SystemCall sc1 = new SystemCall();
							String a = sc1.readFile(tokens[3]);
							setValueToAssign(a,process_ID);
						}	
						
					}else
						System.out.println("7aga tania 8er input w read w value");
			break;
		case "writeFile" :
			if(process_ID==file.getProcess_ID())
				{
					SystemCall sc2 = new SystemCall();
					sc2.writeFile(tokens[1],tokens[2]);					
				}
			else System.out.println("trying to write file without having the mutex of that resource");			
			break;
		case "readFile":	
			if(process_ID==file.getProcess_ID())
				{
					String a = readFile(tokens[1]);
					System.out.print("readfile "+tokens[1]+" :"+a);
				}
			break;
		case "printFromTo" :
			if(process_ID==output.getProcess_ID()){
				SystemCall sc4 = new SystemCall();
				sc4.print(tokens[1], tokens[2]);
			}
			break;
		case "semWait":
			//userInput, userOutput or file
			if(tokens[1].equals("userInput")) {
				if(input.getMutexValue()==0 &&input.getProcess_ID()!=process_ID) {
					BlockProcess(process_ID);
					Input_Blocked_Queue.enqueue(process_ID);					
					 if(Integer.parseInt(Memory[0]+"")==process_ID) {
							Memory[2] = Integer.parseInt(Memory[2]+"")-1 ; 
						 }else
							 if(Integer.parseInt(Memory[5]+"")==process_ID) {
								Memory[7] = Integer.parseInt(Memory[7]+"")-1;
							 }
							 else
								 System.out.print("!!!!!!!");
					blocked = true ;
					printQueues();
					return ;
				}
				input.setMutexValue(0);
				input.setProcess_ID(process_ID);
			}
			else if (tokens[1].equals("userOutput")) {
					if(output.getMutexValue()==0) {
						BlockProcess(process_ID);
						Output_Blocked_Queue.enqueue(process_ID);					
						 if(Integer.parseInt(Memory[0]+"")==process_ID) {
								Memory[2] = Integer.parseInt(Memory[2]+"")-1 ; 
							 }else
								 if(Integer.parseInt(Memory[5]+"")==process_ID) {
									Memory[7] = Integer.parseInt(Memory[7]+"")-1;
								 }
								 else
									 System.out.print("!!!!!!!");
						blocked = true ;
						printQueues();
						return ;
					}
					output.setMutexValue(0);
					output.setProcess_ID(process_ID);
			}
			else if (tokens[1].equals("file")) {
				if(file.getMutexValue()==0) {
					BlockProcess(process_ID);
					File_Blocked_Queue.enqueue(process_ID);					
					 if(Integer.parseInt(Memory[0]+"")==process_ID) {
							Memory[2] = Integer.parseInt(Memory[2]+"")-1 ; 
						 }else
							 if(Integer.parseInt(Memory[5]+"")==process_ID) {
								Memory[7] = Integer.parseInt(Memory[7]+"")-1;
							 }
							 else
								 System.out.print("!!!!!!!");
					blocked = true ;
					printQueues();
					return ;
				}
				file.setMutexValue(0);
				file.setProcess_ID(process_ID);
			
			}else {
				System.out.print("wrong resource name");
			}

			break;
		case "semSignal":
			
			if(tokens[1].equals("userInput")) {
				if(input.getMutexValue()==0 && input.getProcess_ID()==process_ID ) {
					if(Input_Blocked_Queue.isEmpty())
					{
						input.setMutexValue(1);
						input.setProcess_ID(-1);
					}
					else
						releaseInputWaitingProcess();
					
					}
//				else 
//					if(input.getMutexValue()==0 && input.getProcess_ID()!=process_ID) {
//					//do nothing
//				}
//				else
//					//input.getMutexValue()==1 
//					//do nothing
				}
			
			else if(tokens[1].equals("userOutput")) {
				if(output.getMutexValue()==0 && output.getProcess_ID()==process_ID ) {
					if(Output_Blocked_Queue.isEmpty())
					{
						output.setMutexValue(1);
						output.setProcess_ID(-1);
					}
					else
						releaseOutputWaitingProcess();
					
					}
					
					}
			else if(tokens[1].equals("file")) {
				if(file.getMutexValue()==0 && file.getProcess_ID()==process_ID ) {
					if(File_Blocked_Queue.isEmpty())
					{
						file.setMutexValue(1);
						file.setProcess_ID(-1);
					}
					else
						releaseFileWaitingProcess();
					
					}
				}
			else 
				System.out.print("wrong resource name"); break;

		default:System.out.println("Invalid system call: " + command);break;

		}
		   	}

	
	public  void releaseInputWaitingProcess() throws IOException {
		System.out.println("----------------");
		int process_ID = (int)Input_Blocked_Queue.dequeue();
		System.out.println(process_ID);
		UnblockProcess(process_ID);
		input.setProcess_ID(process_ID);
		printQueues();
		
	}
	public  void releaseOutputWaitingProcess() throws IOException {
		int process_ID =(int)Output_Blocked_Queue.dequeue();
		UnblockProcess(process_ID);
		printQueues();
		output.setProcess_ID(process_ID);
		
	}
	public  void releaseFileWaitingProcess() throws IOException {
		int process_ID = (int) File_Blocked_Queue.dequeue();
		UnblockProcess(process_ID);
		file.setProcess_ID(process_ID); 	
		printQueues();
	
	}
	 public  void UnblockProcess(int process_ID) throws IOException {
			System.out.println("----------------");
		 if(Integer.parseInt(Memory[0]+"")==process_ID) {
			 Memory[1] = State.Ready;
		 }
		 else if(Integer.parseInt(Memory[5]+"")==process_ID) {
			 Memory[6]=State.Ready;
		 }
		 else {
			 //in disk
			 BufferedReader reader = new BufferedReader(new FileReader(new File("Disk")));
	         String line;
	         ArrayList<String> program = new ArrayList<String>() ;
	         while ((line = reader.readLine()) != null) {
	        	 program.add(line);     	 
	         }
	         File file = new File("Disk");
	         FileWriter writer = new FileWriter(file);
	         System.out.println(program.get(1));
	         program.set(1, ""+State.Ready) ;
	         for(int i=0;i<program.size();i++) {
	        	 writer.write(program.get(i));
	        	 writer.write('\n');
	         }	
	         writer.flush();
	         writer.close();
		 }
		 Ready_Queue.enqueue(process_ID);
		 Object a =null;
		 for(int i=0;i<3;i++) {
			 if((int)General_Blocked_Queue.peek()==process_ID) {
				 General_Blocked_Queue.dequeue();
					printQueues();
				 return ;
			 }
			 else {
				 a = General_Blocked_Queue.dequeue();
				 General_Blocked_Queue.enqueue(a);
					printQueues();
			 }
		 }
		
	 }
	
	public  void printQueues() {
		System.out.print("the General Blocked Queue :");
		General_Blocked_Queue.printQueue();
		System.out.print("the File Blocked Queue :");
		File_Blocked_Queue.printQueue();
		System.out.print("the Input Blocked Queue :");
		Input_Blocked_Queue.printQueue();
		System.out.print("the Output Blocked Queue :");
		Output_Blocked_Queue.printQueue();
		System.out.print("the Ready Queue :");
		Ready_Queue.printQueue();
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
	 
	public void firstTimeInMemory(ArrayList<String> newProgram) {
		// Create a new PCB , process
		
		if(this.Memory[0]==null) {
			//load into the first block
			//PCB pcb = new PCB(serialID, State.Ready, 0, 10, newProgram.size()-1);
			Memory[0]= serialID ;
			Memory[1]= State.Ready;
			Memory[2]=10;
			Memory[3]=10;
			Memory[4]=10+newProgram.size()-1;
			int i =10 ;
			for (i = 10; i < newProgram.size()+10 ;i++) {  //up to 25
				Memory[i] = newProgram.get(i-10);
			}
			Memory[i] = null;
			Memory[i+1] = null;
			Memory[i+2] = null;
			serialID++;
	
		}
		else if(this.Memory[5]==null) {
			//load into the second block
			Memory[5]= serialID ;
			Memory[6]= State.Ready;
			Memory[7]=25;
			Memory[8]=25;
			Memory[9]=25+newProgram.size()-1;
			int i =25 ;
			for (i = 25; i < newProgram.size()+25; i++) {  //up to 25
				Memory[i] = newProgram.get(i-25);
			}
			Memory[i] = null;
			Memory[i+1] = null;
			Memory[i+2] = null;
			serialID++;
		}
		else {
			//swap
			try {
				System.out.println("here__________________--");
				create_and_Swap(newProgram);
				printDisk();
				display();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Enqueue the process to the ready queue
		Ready_Queue.enqueue(serialID-1);
		printQueues();
		
	}


	
	public void create_and_Swap(ArrayList<String> newProgram) throws IOException {
		File file = new File("Disk");
		FileWriter writer = new FileWriter(file);
		//unload the old one and load the new one 
		if(!Memory[1].equals(State.running)) {
			//swap with the first
			for(int i=0; i<=4;i++) {
				writer.write(Memory[i]+""); 
	        	 writer.write('\n');
			}
			for(int i=10; i<25;i++) {
				writer.write(Memory[i]+""); 
	        	 writer.write('\n');

			}
			writer.flush();
			writer.close();
			Memory[0]= serialID ;
			Memory[1]= State.Ready;
			Memory[2]=10;
			Memory[3]=10;
			Memory[4]=10+newProgram.size()-1;
			int i =10 ;
			for (i = 10; i < newProgram.size()+10; i++) {  //up to 25
				Memory[i] = newProgram.get(i-10);
			}
			while(i<25) {
				Memory[i] =null;
				i++;
			}
//			Memory[i+1] = null;
//			Memory[i+2] = null;
			serialID++;
		}
		else if(!Memory[6].equals(State.running)){
			//swap with the second
			for(int i=5; i<10;i++) {
				writer.write(Memory[i]+"");  
	        	 writer.write('\n');

			}
			for(int i=25; i<40;i++) {
				writer.write(Memory[i]+""); 
	        	 writer.write('\n');

			}
			writer.flush();
			writer.close();
			Memory[5]= serialID ;
			Memory[6]= State.Ready;
			Memory[7]=25;
			Memory[8]=25;
			Memory[9]=25+newProgram.size()-1;
			int i =25 ;
			for (i = 25; i < newProgram.size()+25; i++) {  //up to 25
				Memory[i] = newProgram.get(i-25);
			}
			while(i<40) {
				Memory[i] = null;
				i++;
				}
//			Memory[i] = null;
//			Memory[i+1] = null;
//			Memory[i+2] = null;
			serialID++;
			System.out.println("in the create and swap ");
			printDisk();
		}
		else
			System.out.print("fe 7aga 8lt al mafrod matgesh hena");
		
	}
	
	
//	public void run(int arr1 , int arr2 , int arr3) throws IOException {
//		//b count al process a5adet 2d eh mn al time slice bta3ha
//		System.out.println("cycle:" + this.cycle);
//		//a new process has arrived
//			if(cycle==arr1) {
//				//program1 in 
//				ArrayList<String> newProgram = new ArrayList<String>();				
//				BufferedReader reader = new BufferedReader(new FileReader(new File("programs\\Program_1.txt")));
//		         String line;
//		         while ((line = reader.readLine()) != null) {
//		        	 newProgram.add(line);     	 
//		         }
//								
//				firstTimeInMemory(newProgram);
//				System.out.println(Memory.toString());
//			}
//			if(cycle==arr2) {
//				//program2 in
//				ArrayList<String> newProgram = new ArrayList<String>();				
//				BufferedReader reader = new BufferedReader(new FileReader(new File("programs\\Program_2.txt")));
//		         String line;
//		         while ((line = reader.readLine()) != null) {
//		        	 newProgram.add(line);     	 
//		         }
//								
//				firstTimeInMemory(newProgram);
//			}
//			if(cycle==arr3) {
//				//program3 in
//				ArrayList<String> newProgram = new ArrayList<String>();				
//				BufferedReader reader = new BufferedReader(new FileReader(new File("programs\\Program_3.txt")));
//		         String line;
//		         while ((line = reader.readLine()) != null) {
//		        	 newProgram.add(line);     	 
//		         }
//								
//				firstTimeInMemory(newProgram);
//			}
//			
//			if(processInCPU ==null && !this.Ready_Queue.isEmpty()) {
//				//dispatch
//				int ID = Integer.parseInt(Ready_Queue.dequeue();
//				if(ID== Integer.parseInt(Memory[0])
//					Memory[1] = State.running;
//				else if(ID== Integer.parseInt(Memory[5])
//					Memory[6] = State.running;
//				else {
//					//in disk
//					swapExisting();
//					if(ID== Integer.parseInt(Memory[0])
//						Memory[1] = State.running;
//					else if(ID== Integer.parseInt(Memory[5])
//						Memory[6] = State.running;
//				}
//				processInCPU = ID ;
//				
//			}
//				
//			//get the running process	
//			if(Integer.parseInt(Memory[0]==processInCPU) {
//				//execute p1
//				executeInstruction((String)Memory[Integer.parseInt(Memory[2]],Integer.parseInt(Memory[0]);
//				Memory[2] = Integer.parseInt(Memory[2]+1 ;  //pc++ ;
//			}
//			else if(Integer.parseInt(Memory[5]==processInCPU) {
//				//execute p2
//				executeInstruction((String)Memory[Integer.parseInt(Memory[7]],Integer.parseInt(Memory[5]);
//				Memory[7] = Integer.parseInt(Memory[7]+1 ;
//				
//			}	
//			else System.out.print("spending a cycle doing nothing");									
//		this.cycle++ ;		
//		
//	}
//	
//	
//	//in the blocking method dispatch another
//
	public void display() {
		System.out.print("Memory: {");
		for(int j=0;j<39;j++)
			System.out.print(Memory[j]+",");
		System.out.print(Memory[39]+"}");
		System.out.println();
	}
	
	
	public static void printDisk() throws IOException {
		 BufferedReader reader = new BufferedReader(new FileReader(new File("Disk")));
         String line;
         ArrayList<String> program = new ArrayList<String>() ;
         while ((line = reader.readLine()) != null) {
        	 program.add(line);     	 
         }
         System.out.println("program in Disk: ");
         for(String a : program) {
        	 System.out.println(a);
         }
	}
	public void GO(int arr1 , int arr2 , int arr3) throws IOException {
		int cycles =0;
		int counter =0;
		boolean preempted = false ;
		while(cycles<40) {
			preempted=false;
			System.out.println("----------------");
			System.out.println("cycle:" + cycles);	
			
			if(cycles==arr1) {
				//program1 in 
				ArrayList<String> newProgram = new ArrayList<String>();				
				BufferedReader reader = new BufferedReader(new FileReader(new File("programs\\Program_1.txt")));
		         String line;
		         while ((line = reader.readLine()) != null) {
		        	 newProgram.add(line);     	 
		         }
							
				firstTimeInMemory(newProgram);
			}
			if(cycles==arr2) {
				//program2 in
				ArrayList<String> newProgram = new ArrayList<String>();				
				BufferedReader reader = new BufferedReader(new FileReader(new File("programs\\Program_2.txt")));
		         String line;
		         while ((line = reader.readLine()) != null) {
		        	 newProgram.add(line);     	 
		         }
								
				firstTimeInMemory(newProgram);
			}
			if(cycles==arr3) {
				//program3 in
				ArrayList<String> newProgram = new ArrayList<String>();				
				BufferedReader reader = new BufferedReader(new FileReader(new File("programs\\Program_3.txt")));
		         String line;
		         while ((line = reader.readLine()) != null) {
		        	 newProgram.add(line);     	 
		         }
								
				firstTimeInMemory(newProgram);
			}
		
			//takes clock cycles = time slice
		System.out.println("processInCPU = "+processInCPU);
		System.out.println("counter = "+counter);
			if(processInCPU !=null && counter< timeSlice &&!(blocked(processInCPU))) {
				//execute one more time if any
		
				if(Integer.parseInt(Memory[0]+"")==processInCPU && ! (Memory[1]+"").equals(State.Finished+"")) {
					//execute p1
					executeInstruction((String)Memory[Integer.parseInt(Memory[2]+"")],Integer.parseInt(Memory[0]+""));
//					if(Integer.parseInt(Memory[2]+"")==Integer.parseInt(Memory[4]+"")) {
//						Memory[1]=State.Finished;
//					}
					Memory[2] = Integer.parseInt(Memory[2]+"")+1 ;  //pc++ ;

				}
				else if(Integer.parseInt(Memory[5]+"")==processInCPU&&!(Memory[6]+"").equals(State.Finished+"")) {
					//execute p2
					executeInstruction((String)Memory[Integer.parseInt(Memory[7]+"")],Integer.parseInt(Memory[5]+""));
//					if(Integer.parseInt(Memory[7]+"")==Integer.parseInt(Memory[9]+"")) {
//						Memory[6]=State.Finished;
//					}
					Memory[7] = Integer.parseInt(Memory[7]+"")+1 ;

				}
				counter++;
			}
			if(processInCPU !=null && counter>=timeSlice&& !blocked(processInCPU) && !(Memory[1]+"").equals(State.Finished+"")){
				//the process took its time slice  counter>=timeSlice
				//preempt
				//reset counter
				//dec cycles in order to appear as if we are in the same cycle
				Ready_Queue.enqueue(processInCPU);
				if(Integer.parseInt(""+Memory[0])==processInCPU)
					Memory[1] = State.Ready;
				else if (Integer.parseInt(""+Memory[5])==processInCPU)
					Memory[6] = State.Ready;
				preempted=true ;
				printQueues();
				processInCPU=null ;	
				counter=0 ;	
			}else if(processInCPU !=null && (blocked(processInCPU)||finished(processInCPU))) {
				processInCPU=null ;	
				counter=0 ;	
			}
		//processInCPU==null
			if(processInCPU ==null && !this.Ready_Queue.isEmpty()&&!preempted) {
				//dispatch
				counter=0;
				
				int ID = (int)Ready_Queue.dequeue();
				if(ID== Integer.parseInt(Memory[0]+""))
					Memory[1] = State.running; 
				else if(ID== Integer.parseInt(Memory[5]+""))
					Memory[6] = State.running;
				else {
					//in disk
					swapExisting();
					if(ID== Integer.parseInt(""+Memory[0]))
						Memory[1] = State.running;
					else if(ID== Integer.parseInt(Memory[5]+""))
						Memory[6] = State.running;
				}
				processInCPU = ID ;
				if(Integer.parseInt(""+Memory[0])==processInCPU) {
					//execute p1
					executeInstruction(""+Memory[Integer.parseInt(Memory[2]+"")],Integer.parseInt(Memory[0]+""));
//					
					Memory[2] = Integer.parseInt(Memory[2]+"")+1 ;  //pc++ ;

				}
				else if(Integer.parseInt(""+Memory[5])==processInCPU) {
					//execute p2
					executeInstruction((String)Memory[Integer.parseInt(Memory[7]+"")],Integer.parseInt(Memory[5]+""));
//					
					Memory[7] = Integer.parseInt(Memory[7]+"")+1 ;

				}
				counter++;
				printQueues();
			}
			
			display();
			printDisk();
			cycles++;
		}	
	}
	
	public boolean blocked(int processInCPU){
		if(Memory[5]!=null  &&Integer.parseInt(Memory[0]+"")==processInCPU && ((State)Memory[1]).equals(State.Blocked)) {
			return true ;
		}
		else if(Memory[5]!=null&&Integer.parseInt(Memory[5]+"")==processInCPU && ((State)Memory[6]).equals(State.Blocked)) {
			return true ;			
		}
		return false ;
	}
	
	public boolean finished(int processInCPU) {
		if(Memory[5]!=null && Integer.parseInt(Memory[0]+"")==processInCPU && ((State)Memory[1]).equals(State.Finished)) {
			return true ;
		}
		else if(Memory[5]!=null&&Integer.parseInt(Memory[5]+"")==processInCPU && ((State)Memory[6]).equals(State.Finished)) {
			return true ;			
		}
		return false ;
	}
	

	public void swapExisting() throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(new File("Disk")));
	    String line;
	    ArrayList<String> programToBeSwapped = new ArrayList<String>();

	    while ((line = reader.readLine()) != null) {
	        programToBeSwapped.add(line);
	    }

	    reader.close(); // Close the reader after reading from the file

	    File file = new File("Disk");
	    FileWriter writer = new FileWriter(file);

	    if (!Memory[1].equals(State.running)) {
	        System.out.println("process " + Memory[0] + " is swapped into the disk and process " + programToBeSwapped.get(0) + " is swapped out from the disk");

	        for (int i = 0; i < 5; i++) {
	            writer.write(Memory[i] + "");
	            writer.write('\n');
	        }

	        for (int i = 10; i < 25; i++) {
	            writer.write(Memory[i] + "");
	            writer.write('\n');
	        }

	        writer.flush();
	        writer.close();

	        for (int i = 10; i < 25; i++) {
	            Memory[i] = null;
	        }

	        for (int i = 0; i < 5; i++) {
	            Memory[i] = programToBeSwapped.get(i);
	        }

	        for (int i = 10; i < 25; i++) {
	            Memory[i] = programToBeSwapped.get(i - 5);
	        }
	    } else if (!Memory[6].equals(State.running)) {
	        System.out.println("process " + Memory[5] + " is swapped into the disk and process " + programToBeSwapped.get(0) + " is swapped out from the disk");

	        for (int i = 5; i < 10; i++) {
	            writer.write(Memory[i] + "");
	            writer.write('\n');
	        }

	        for (int i = 25; i < 40; i++) {
	            writer.write(Memory[i] + "");
	            writer.write('\n');
	        }

	        writer.flush();
	        writer.close();

	        for (int i = 25; i < 40; i++) {
	            Memory[i] = null;
	        }

	        for (int i = 5; i < 10; i++) {
	            Memory[i] = programToBeSwapped.get(i - 5);
	        }

	        for (int i = 25; i < 40; i++) {
	            Memory[i] = programToBeSwapped.get(i - 20);
	        }
	    } else {
	        System.out.println("No process is available to swap.");
	    }
	}
	public static void main(String[]args) throws IOException {
	
		SimpleOS os = new SimpleOS();
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter the arrival time of process 1 : ");
		int a = sc.nextInt();
		System.out.println("please enter the arrival time of process 2 : ");
		int b = sc.nextInt();
		System.out.println("please enter the arrival time of process 3 : ");
		int c = sc.nextInt();
		System.out.println("please enter the no. of cycles for a quantum : ");
		int cycles = sc.nextInt();
		os.timeSlice=cycles;
				
		os.GO(a,b,c);

	}
	
	public static int programSize(String program) throws IOException {
		 BufferedReader reader = new BufferedReader(new FileReader(new File(program)));
         String line;
         ArrayList<String> programs = new ArrayList<String>() ;
         while ((line = reader.readLine()) != null) {
        	 programs.add(line);     	 
         }
         return programs.size();
	}
}