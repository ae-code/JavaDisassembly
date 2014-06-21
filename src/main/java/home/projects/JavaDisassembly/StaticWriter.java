package home.projects.JavaDisassembly;

import java.util.Map;

interface StaticWriter {
	Map<Statics, String> generateContent() throws HTMLGenerationException;
}
