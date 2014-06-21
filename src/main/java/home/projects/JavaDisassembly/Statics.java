package home.projects.JavaDisassembly;

enum Statics {
	STYLESHEET("StaticContent/style.css", "style.css", "stylesheet"),
	JQUERY("StaticContent/jquery-1.11.1.min.js", "jquery.js", "jquery"),
	SCRIPT("StaticContent/script.js", "script.js", "script");
	
	private final String m_sPath;
	private final String m_sFinalFilename;
	private final String m_sName;
	
	Statics(String sPath, String sFinalFilename, String sName) {
		m_sPath = sPath;
		m_sFinalFilename = sFinalFilename;
		m_sName = sName;
	}
	
	String path() {
		return m_sPath;
	}

	String finalFilename() {
		return m_sFinalFilename;
	}
	
	String staticName() {
		return m_sName;
	}
}
