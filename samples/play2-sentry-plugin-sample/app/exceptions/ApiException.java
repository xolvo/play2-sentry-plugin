package exceptions;

public class ApiException extends Exception {
	private static final long serialVersionUID = 452584463493097343L;
	
	protected Integer code = -1;

	public ApiException() {
		super();
	}
	
	public ApiException(Integer code, String message) {
		super(message);
		this.code = code;
	}
}
