
public class Mutex {
	private String resourceName  ;
	private int mutexValue  ;
	private Integer process_ID  ;

	  public Mutex(String input1, int input2) {
	        this.resourceName = input1;
	        this.mutexValue = input2;
	        this.process_ID = null;
	    }

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getMutexValue() {
		return mutexValue;
	}

	public void setMutexValue(int mutex) {
		this.mutexValue = mutex;
	}

	public int getProcess_ID() {
		return process_ID;
	}

	public void setProcess_ID(int process_ID) {
		this.process_ID = process_ID;
	}

	    // Getters and setters for the inputs
	 
	}
	    

