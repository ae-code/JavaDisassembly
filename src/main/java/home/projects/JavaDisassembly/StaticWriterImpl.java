package home.projects.JavaDisassembly;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

class StaticWriterImpl implements StaticWriter {
	private final Config m_config;
	
	StaticWriterImpl(Config config) {
		m_config = config;
	}

	@Override
	public Map<Statics, String> generateContent() throws HTMLGenerationException {
		Path destDir = FileSystems.getDefault().getPath(m_config.getDestinationDirectory()).resolve("static/");
		
		Map<Statics, String> mFiles = new EnumMap<Statics, String>(Statics.class);
		
		for (Statics content : Statics.values()) {
			InputStream is = getClass().getResourceAsStream(content.path());
			if (is == null)
				throw new HTMLGenerationException("Static content not found");
			BufferedInputStream bis = new BufferedInputStream(is);
			
			String sFilename = destDir.resolve(content.finalFilename()).toString();
			File file = new File(sFilename);
			file.getParentFile().mkdirs();
			FileOutputStream fos = null;
			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				while (bis.available() > 0) {
					bos.write(bis.read());
				}
				bos.close();
			} catch (IOException e) {
				throw new HTMLGenerationException("Unable to create: " + sFilename, e);
			}
			finally {
				try {
					is.close();
					if (fos != null)
						fos.close();
				} catch (IOException e) {
				}
			}
			
			mFiles.put(content, sFilename);
		}
		
		return mFiles;
	}

}
