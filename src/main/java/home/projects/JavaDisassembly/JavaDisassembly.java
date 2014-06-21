package home.projects.JavaDisassembly;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JavaDisassembly {
	final private Config m_config;
	private StatusHandler m_status = null;

	public JavaDisassembly(Config c) throws InvalidConfig {
		m_config = c;
		if (m_config == null)
			throw new InvalidConfig();
		else if (!m_config.isValid())
			throw new InvalidConfig();
	}
	
	public void setStatusHandler(StatusHandler status) {
		m_status = status;
	}

	public void generate() throws JavaDisassemblyException {
		updateStatus("Generating list of class files");
		List<String> lFiles = SourceListGenerator.getFiles(m_config.getSourceDirectory());
		updateStatus("Found " + lFiles.size() + " class files");
		
		updateStatus("Generating disassembly");
		DisassemblyDataMap data = new DisassemblyDataMap();
		DisassemblyManager disassemblyManager = getDisassemblyManager();
		disassemblyManager.disassembleAll(lFiles, data);
		updateStatus("Completed generating disassembly");
		
		updateStatus("Generating static content");
		StaticWriter staticWriter = getStaticContentWriter();
		final Map<Statics, String> mStaticFiles = staticWriter.generateContent();
		updateStatus("Completed generating static content");
		
		updateStatus("Generating HTML files");
		HTMLWriter htmlWriter = getHTMLWriter(mStaticFiles);
		Iterator<Entry<String, DisassemblyData>> itData = data.getIterator();
		Map<String,String> mFiles = new HashMap<String,String>();
		while (itData.hasNext()) {
			Entry<String, DisassemblyData> entry = itData.next();
			String sFilename = htmlWriter.generateHTML(entry.getKey(), entry.getValue());
			mFiles.put(entry.getValue().metadata.sClassFile, sFilename);
			updateStatus("Wrote: " + sFilename);
		}
		updateStatus("Completed generating HTML files");

		updateStatus("Writing index page");
		String sIndexPage = htmlWriter.generateIndex(mFiles);
		updateStatus("Completed writing index page: " + sIndexPage);
	}
	
	private void updateStatus(String msg) {
		if (m_status != null)
			m_status.updateStatus(msg);
	}
	
	private DisassemblyManager getDisassemblyManager() {
		if (m_config.getParallelizationFactor() <= 1)
			return new SerialDisassemblyManager(m_config, m_status);
		else
			return new ThreadedDisassemblyManager(m_config, m_status);
	}
	
	private StaticWriter getStaticContentWriter() {
		return new StaticWriterImpl(m_config);
	}
	
	private HTMLWriter getHTMLWriter(Map<Statics, String> mStaticFiles) throws InvalidConfig {
		return new HTMLWriterImpl(m_config, getHTMLFormatter(mStaticFiles), getHTMLIndexFormatter(mStaticFiles));
	}
	
	private HTMLFormatter getHTMLFormatter(Map<Statics, String> mStaticFiles) throws InvalidConfig {
		return new HTMLFormatterImpl(m_config, mStaticFiles);
	}

	private HTMLIndexFormatter getHTMLIndexFormatter(Map<Statics, String> mStaticFiles) throws InvalidConfig {
		return new HTMLIndexFormatterImpl(m_config, mStaticFiles);
	}

}
