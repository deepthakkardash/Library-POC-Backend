package exception;

public class ResourceNotFoundException extends RuntimeException{
	public ResourceNotFoundException(String msg)
	{
		super(msg);
		System.out.println("Resource not found Exception");
	}
}
