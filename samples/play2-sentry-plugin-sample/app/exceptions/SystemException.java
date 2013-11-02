package exceptions;

public class SystemException extends ApiException {
	private static final long serialVersionUID = -3718989223920085439L;

	public SystemException() {
		super(-2, "System error");
	}

}
