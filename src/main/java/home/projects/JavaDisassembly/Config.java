package home.projects.JavaDisassembly;

public class Config {
	private String m_sSourceDirectory;
	private String m_sDestinationDirectory;
	private int m_iParallelizationFactor;

	public Config() {
		m_iParallelizationFactor = Runtime.getRuntime().availableProcessors();
	}
	
	public boolean isValid() {
		return (!m_sSourceDirectory.isEmpty() &&
				!m_sDestinationDirectory.isEmpty());
	}
	
	public String getSourceDirectory() {
		return m_sSourceDirectory;
	}
	public void setSourceDirectory(String m_sSourceDirectory) {
		this.m_sSourceDirectory = m_sSourceDirectory;
	}

	public String getDestinationDirectory() {
		return m_sDestinationDirectory;
	}

	public void setDestinationDirectory(String m_sDestinationDirectory) {
		this.m_sDestinationDirectory = m_sDestinationDirectory;
	}

	public int getParallelizationFactor() {
		return m_iParallelizationFactor;
	}

	public void setParallelizationFactor(int m_iParallelizationFactor) {
		this.m_iParallelizationFactor = m_iParallelizationFactor;
	}

}
