package home.projects.JavaDisassembly;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class SourceListGenerator {

	public static class AccumulateFiles extends SimpleFileVisitor<Path> {
		public AccumulateFiles(Path root, List<String> files) {
			super();
			
			m_root = root;
			m_files = files;
		}
		
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
	        if (attr.isRegularFile()) {
	        	if (file.toString().endsWith(".class"))
	        		m_files.add(m_root.relativize(file).toString());
	        }

	        return CONTINUE;
	    }

	    @Override
	    public FileVisitResult visitFileFailed(Path file,
	                                       IOException exc) {
	        System.err.println(exc);
	        return CONTINUE;
	    }
	    
		private Path m_root;
	    private List<String> m_files;
	}

	public static List<String> getFiles(String sRoot) throws InvalidConfig {
		Path pathRoot;
		try {
			pathRoot = FileSystems.getDefault().getPath(sRoot);
		}
		catch (InvalidPathException e) {
			throw new InvalidConfig("Bad Source Path", e);
		}
		
		List<String> lFiles = new ArrayList<String>();
		AccumulateFiles af = new AccumulateFiles(pathRoot, lFiles);
		
		try {
			Files.walkFileTree(pathRoot, af);
		} catch (IOException e) {
			throw new InvalidConfig("Unable To Get File List", e);
		}
		
		return lFiles;
	}
}
