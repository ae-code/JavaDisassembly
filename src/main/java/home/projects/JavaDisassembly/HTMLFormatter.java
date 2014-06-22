package home.projects.JavaDisassembly;

import java.io.Writer;

interface HTMLFormatter {
	void formatHTML(DisassemblyData data, Writer out) throws HTMLGenerationException;

	void setRelativePath(String s);
}
