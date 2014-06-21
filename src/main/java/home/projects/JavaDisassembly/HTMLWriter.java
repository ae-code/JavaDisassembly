package home.projects.JavaDisassembly;

import java.util.Map;

interface HTMLWriter {
	String generateHTML(String sClass, DisassemblyData data) throws HTMLGenerationException;

	String generateIndex(Map<String, String> mFiles) throws HTMLGenerationException;
}
