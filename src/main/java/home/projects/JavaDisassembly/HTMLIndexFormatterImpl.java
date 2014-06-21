package home.projects.JavaDisassembly;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;

class HTMLIndexFormatterImpl implements HTMLIndexFormatter {
	private final Map<Statics, String> m_mStaticFiles;
	private Mustache m_template = null;
	

	public HTMLIndexFormatterImpl(Config m_config, Map<Statics, String> mStaticFiles) throws InvalidConfig {
		m_mStaticFiles = mStaticFiles;

		InputStream is = getClass().getResourceAsStream(Templates.LISTING_PAGE.path());
		if (is == null)
			throw new InvalidConfig("Default index page template not found");
		
		InputStreamReader isr = new InputStreamReader(is);

		MustacheFactory factory = new DefaultMustacheFactory();
		try {
			m_template = factory.compile(isr, "Index Page");
		} catch (MustacheException e) {
			throw new InvalidConfig("Index page template not found", e);
		} finally {
			try {
				is.close();
			}
			catch (Exception e) {
			}
		}
	}

	class FileListing {
		FileListing(String c, String f) {
			className = c;
			file = f;
		}
		
		final String file;
		final String className;
	}
	
	class TemplateData {
		public TemplateData(Map<String,String> mFiles, Map<Statics, String> mStaticFiles) {
			files = new ArrayList<FileListing>();
			List<String> lClasses = new ArrayList<String>(mFiles.keySet());
			Collections.sort(lClasses);
			for (String sClass : lClasses) {
				FileListing listing = new FileListing(sClass, mFiles.get(sClass));
				files.add(listing);
			}
			
			staticFiles = new HashMap<String,String>();
			Iterator<Entry<Statics,String>> it = mStaticFiles.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Statics,String> itVal = it.next();
				staticFiles.put(itVal.getKey().staticName(), itVal.getValue());
			}
		}
		
		List<FileListing> files;
		Map<String,String> staticFiles;
	}
	
	@Override
	public void formatHTML(Map<String, String> mFiles, Writer out) throws HTMLGenerationException {
		TemplateData templateData = new TemplateData(mFiles, m_mStaticFiles);
		
		try {
			m_template.execute(out, templateData);
		} catch (MustacheException e) {
			throw new HTMLGenerationException("Error Writing", e);
		}
	}

}
