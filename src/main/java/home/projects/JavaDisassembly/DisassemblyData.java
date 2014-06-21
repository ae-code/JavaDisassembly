package home.projects.JavaDisassembly;

import java.util.ArrayList;
import java.util.List;

class DisassemblyData {
	class Metadata {
		String sClassFile;
		String sLastModified;
		String sSize;
		String sMD5;
		String sSource;
	}
	
	class Constant {
		int iIndex;
		String sType;
		String sValue;
		String sComment;
	}
	
	class Function {
		class Instruction {
			int iOffset;
			String sOpcode;
			String sArgs;
			String sComment;
		}
		
		int iIndex;
		String sSignature;
		List<Instruction> lCode = new ArrayList<Instruction>();
	}
	
	Metadata metadata = new Metadata();
	List<Constant> constants = new ArrayList<Constant>();
	List<Function> functions = new ArrayList<Function>();
	List<String> members = new ArrayList<String>();
	List<String> raw = null;
}
