package home.projects.JavaDisassembly;

import java.util.List;

class SerialDisassemblyManager extends DisassemblyManager {

	SerialDisassemblyManager(Config config, StatusHandler status) {
		super(config, status);
	}

	@Override
	void disassembleAll(List<String> files, DisassemblyDataMap results) throws DisassemblerException {
		Disassembler disassembler = getDisassembler();

		for (int i = 0; i < files.size(); ++i) {
			String sClass = files.get(i);
			
			updateStatus("Disassembling " + (i+1) + " of " + files.size() + ": " + sClass);
			DisassemblyData data = disassembler.invoke(m_config.getSourceDirectory(), sClass);
			results.setResult(sClass, data);
		}
	}

}
