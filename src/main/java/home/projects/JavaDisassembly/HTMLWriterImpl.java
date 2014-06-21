package home.projects.JavaDisassembly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

class HTMLWriterImpl implements HTMLWriter {
	private final Config m_config;
	private final HTMLFormatter m_formatter;
	private final HTMLIndexFormatter m_indexFormatter;
	private final Path m_root;
	
	HTMLWriterImpl(Config config, HTMLFormatter formatter, HTMLIndexFormatter indexFormatter) {
		m_config = config;
		m_formatter = formatter;
		m_indexFormatter = indexFormatter;
		m_root = FileSystems.getDefault().getPath(m_config.getDestinationDirectory());
	}
	
	private String getFilename(String sClass) {
		String sHtml = sClass.replaceFirst("class$", "html");
		return m_root.resolve(sHtml).toString();
	}
	
	@Override
	public String generateHTML(String sClass, DisassemblyData data) throws HTMLGenerationException {
		String sFilename = getFilename(sClass);
		
		File file = new File(sFilename);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new HTMLGenerationException("Unable to create: " + sFilename, e);
		}
		
		try (FileWriter fileOut = new FileWriter(file)) {
			m_formatter.formatHTML(data, fileOut);
		} catch (IOException e) {
			throw new HTMLGenerationException("Error writing: " + sFilename, e);
		}			
		
		return sFilename;
	}

	@Override
	public String generateIndex(Map<String, String> mFiles) throws HTMLGenerationException {
		String sFilename = getIndexFilename();
		
		File file = new File(sFilename);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new HTMLGenerationException("Unable to create: " + sFilename, e);
		}
		
		try (FileWriter fileOut = new FileWriter(file)) {
			m_indexFormatter.formatHTML(mFiles, fileOut);
		} catch (IOException e) {
			throw new HTMLGenerationException("Error writing: " + sFilename, e);
		}			
		
		return sFilename;
	}

	private String getIndexFilename() {
		String sHtml = "index.html";
		return m_root.resolve(sHtml).toString();
	}
}
