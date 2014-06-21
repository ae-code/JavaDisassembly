package home.projects.JavaDisassembly;

class HTMLGenerationException extends JavaDisassemblyException {

	private static final long serialVersionUID = 3414939584472851613L;

	public HTMLGenerationException() {
		super("Error generating HTML");
	}

	public HTMLGenerationException(String msg) {
		super("Error generating HTML: " + msg);
	}

	public HTMLGenerationException(String msg, Exception e) {
		super("Error generating HTML: " + msg, e);
	}
}
