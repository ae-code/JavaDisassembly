package home.projects.JavaDisassembly;

import java.util.List;

abstract class DisassemblyManager {
	protected final Config m_config;
	protected final StatusHandler m_status;
	
	DisassemblyManager(Config config, StatusHandler status) {
		m_config = config;
		m_status = status;
	}
	
	protected void updateStatus(String msg) {
		if (m_status != null)
			m_status.updateStatus(msg);
	}
	
	protected Disassembler getDisassembler() {
		return new LocalJavapDisassembler();
	}
	
	abstract void disassembleAll(List<String> files, DisassemblyDataMap results) throws DisassemblerException;
}
