package home.projects.JavaDisassembly;

public class InvalidConfig extends JavaDisassemblyException {

	private static final long serialVersionUID = -5605404037637725848L;

	public InvalidConfig() {
		super("Invalid Config");
	}

	public InvalidConfig(String sMsg) {
		super("Invalid Config: " + sMsg);
	}

	public InvalidConfig(String sMsg, Throwable cause) {
		super("Invalid Config: " + sMsg, cause);
	}
}
