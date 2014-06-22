package home.projects.JavaDisassembly;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;

class HTMLFormatterImpl implements HTMLFormatter {
	private final Map<Statics, String> m_mStaticFiles;
	private Mustache m_template = null;
	private String m_sRelativePath = "";
	
	HTMLFormatterImpl(Config config, Map<Statics, String> mStaticFiles) throws InvalidConfig {		
		m_mStaticFiles = mStaticFiles;

		InputStream is = getClass().getResourceAsStream(Templates.CLASS_PAGE.path());
		if (is == null)
			throw new InvalidConfig("Default class page template not found");
		
		InputStreamReader isr = new InputStreamReader(is);

		MustacheFactory factory = new DefaultMustacheFactory();
		try {
			m_template = factory.compile(isr, "Class Page");
		} catch (MustacheException e) {
			throw new InvalidConfig("Class page template not found", e);
		} finally {
			try {
				is.close();
			}
			catch (Exception e) {
			}
		}
	}
	
	@Override
	public void setRelativePath(String s) {
		m_sRelativePath = s;
	}
	
	class TemplateData {
		public TemplateData(DisassemblyData disData, Map<Statics, String> mStaticFiles) {
			data = disData;
			
			staticFiles = new HashMap<String,String>();
			Iterator<Entry<Statics,String>> it = mStaticFiles.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Statics,String> itVal = it.next();

				staticFiles.put(itVal.getKey().staticName(), m_sRelativePath + itVal.getValue());
			}
		}
		
		DisassemblyData data;
		Map<String,String> staticFiles;
	}
	
	@Override
	public void formatHTML(DisassemblyData data, Writer out) throws HTMLGenerationException {
		TemplateData templateData = new TemplateData(data, m_mStaticFiles);
		
		try {
			m_template.execute(out, templateData);
		} catch (MustacheException e) {
			throw new HTMLGenerationException("Error Writing", e);
		}
	}

}
