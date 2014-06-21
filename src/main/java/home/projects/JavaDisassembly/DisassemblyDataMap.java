package home.projects.JavaDisassembly;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class DisassemblyDataMap {
	private Map<String, DisassemblyData> m_mData = new HashMap<String, DisassemblyData>();
	
	synchronized int setResult(String sClass, DisassemblyData data) {
		m_mData.put(sClass, data);
		return m_mData.size();
	}
	
	synchronized int getNumResults() {
		return m_mData.size();
	}
	
	Iterator<Entry<String, DisassemblyData>> getIterator() {
		return m_mData.entrySet().iterator();
	}
}
