package home.projects.JavaDisassembly;

public class DisassemblerException extends JavaDisassemblyException {

	private static final long serialVersionUID = -2969204427015181113L;

	public DisassemblerException() {
		super("Disassembler Error");
	}

	public DisassemblerException(String msg) {
		super("Disassembler Error: " + msg);
	}

	public DisassemblerException(String msg, Exception e) {
		super("Disassembler Error: " + msg, e);
	}
}
