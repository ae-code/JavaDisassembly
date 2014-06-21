package home.projects.JavaDisassembly;

public class JavaDisassemblyException extends Exception {

	private static final long serialVersionUID = -8335456735621623769L;

	public JavaDisassemblyException(String msg) {
		super(msg);
	}

	public JavaDisassemblyException(String msg, Throwable cause) {
		super(msg, cause);
	}

	@Override
	public String getMessage() {
		return "JavaDisassembly: " + super.getMessage();
	}
}
