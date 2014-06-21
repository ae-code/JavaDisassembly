package home.projects.JavaDisassembly;

import java.io.Writer;
import java.util.Map;

interface HTMLIndexFormatter {
	void formatHTML(Map<String,String> mFiles, Writer out) throws HTMLGenerationException;
}
