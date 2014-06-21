package home.projects.JavaDisassembly;

enum Templates {
	CLASS_PAGE("TemplateContent/class.mustache"),
	LISTING_PAGE("TemplateContent/listing.mustache");
	
	private final String m_sPath;
	
	Templates(String sPath) {
		m_sPath = sPath;
	}
	
	String path() {
		return m_sPath;
	}
}
