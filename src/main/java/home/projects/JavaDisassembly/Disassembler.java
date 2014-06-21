package home.projects.JavaDisassembly;

interface Disassembler {
	public DisassemblyData invoke(String sSourceDirectory, String sClass) throws DisassemblerException;
}
