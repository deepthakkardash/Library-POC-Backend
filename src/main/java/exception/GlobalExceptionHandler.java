package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.libraryManagement.in.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex){
		System.out.println("Global Exception");
		ApiResponse<String> res=new ApiResponse<String>("failed", "An unexpected error occurred", null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<String>> handleAll(Exception e)
	{
		ApiResponse<String> res=new ApiResponse<String>("failed", "An unexpected error occurred", null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}
}
