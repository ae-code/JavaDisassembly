package home.projects.JavaDisassembly;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LocalJavapDisassembler implements Disassembler {
	public DisassemblyData invoke(String sSourceDirectory, String sClass) throws DisassemblerException {
		Path root = FileSystems.getDefault().getPath(sSourceDirectory);
		String sFullClass = root.resolve(sClass).toString();
		
		ProcessBuilder pb = new ProcessBuilder("javap", "-c", "-v", sFullClass);
		try {
			Process p = pb.start();

			InputStream is = p.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			p.waitFor();
			
			List<String> raw = getOutput(bis);
			
			DisassemblyData output = new DisassemblyData();
			parseRawDisassembly(raw, output);
			
			output.metadata.sClassFile = root.relativize(FileSystems.getDefault().getPath(output.metadata.sClassFile)).toString();
			
			return output;
			
		} catch (IOException|InterruptedException e) {
			throw new DisassemblerException(sClass);
		}
	}
	
	List<String> getOutput(InputStream is) {
		List<String> raw = new ArrayList<String>();
		
		Scanner s = null;
		try {
			s = new Scanner(is).useDelimiter("\n");
			while (s.hasNext())
				raw.add(s.next());
		}
		finally {
			if (s != null)
				s.close();
		}
		
		return raw;
	}
	
	void parseRawDisassembly(List<String> raw, DisassemblyData output) throws DisassemblerException {
		output.raw = raw;
		
		// parse metadata
		if (raw.size() < 4)
			throw new DisassemblerException("Invalid disassembly format");
		if (!raw.get(0).startsWith("Classfile"))
			throw new DisassemblerException("Invalid disassembly format");
		output.metadata.sClassFile = raw.get(0).substring(10);
		Pattern patternLine2 = Pattern.compile("\\s*Last modified (.*); size (.*)");
		Matcher matcherLine2 = patternLine2.matcher(raw.get(1));
		matcherLine2.matches();
		output.metadata.sLastModified = matcherLine2.group(1);
		output.metadata.sSize = matcherLine2.group(2);
		output.metadata.sMD5 = raw.get(2).substring("  MD5 checksum ".length());
		output.metadata.sSource = raw.get(3).substring("  Compiled from \"".length());
		output.metadata.sSource = output.metadata.sSource.substring(0, output.metadata.sSource.length()-1);
	
		// parse constant pool & functions
		boolean bInConstantPool = false;
		boolean bInCode = false;
		DisassemblyData.Function currFunc = null;
		Pattern patternConstant = Pattern.compile("#([0-9]+) = (\\S*)\\s*(\\S*)\\s*(//\\s+(.*))?");
		Pattern patternFunction = Pattern.compile("^\\s\\s(\\S.*)");
		Pattern patternCode = Pattern.compile("([0-9]+): (\\S+) ([^/]+) (//(.*))?");
		for (String sLine : raw) {
			if (!bInConstantPool) {
				if (sLine.startsWith("Constant pool:"))
					bInConstantPool = true;
			}
			else if (sLine.startsWith("{"))
				bInCode = true;
			else if (bInConstantPool && !bInCode){
				Matcher matcherConstant = patternConstant.matcher(sLine);
				matcherConstant.find();
				DisassemblyData.Constant constant = output.new Constant();
				constant.iIndex = Integer.parseInt(matcherConstant.group(1));
				constant.sType = matcherConstant.group(2);
				constant.sValue = matcherConstant.group(3);
				if (matcherConstant.group(5) == null)
					constant.sComment = new String();
				else
					constant.sComment = matcherConstant.group(5);
				output.constants.add(constant);
			}
			else if (bInCode) {
				Matcher matcherFunction = patternFunction.matcher(sLine);
				Matcher matcherCode = patternCode.matcher(sLine);
				if (sLine.startsWith("}") || matcherFunction.matches()) {
					if (currFunc != null) {
						if ((currFunc.lCode.size() > 0) || (currFunc.sSignature.contains("("))) {
							currFunc.iIndex = output.functions.size();
							output.functions.add(currFunc);
						}
						else {
							output.members.add(currFunc.sSignature);
						}
					}

					if (!sLine.startsWith("}")) {
						currFunc = output.new Function();
						currFunc.sSignature = matcherFunction.group(1);
					}
				}
				else if (matcherCode.find()){
					DisassemblyData.Function.Instruction instruction = currFunc.new Instruction();
					instruction.iOffset = Integer.parseInt(matcherCode.group(1));
					instruction.sOpcode = matcherCode.group(2);
					if (matcherCode.group(3) == null)
						instruction.sArgs = new String();
					else
						instruction.sArgs = matcherCode.group(3);
					if (matcherCode.group(5) == null)
						instruction.sComment = new String();
					else
						instruction.sComment = matcherCode.group(5);
					currFunc.lCode.add(instruction);
				}
			}
		}
	}
}
