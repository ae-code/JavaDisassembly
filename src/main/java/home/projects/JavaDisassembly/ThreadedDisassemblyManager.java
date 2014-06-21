package home.projects.JavaDisassembly;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ThreadedDisassemblyManager extends DisassemblyManager {
	class DisassemblyRunner implements Runnable {

		private final String m_sClass;
		private final int m_iCurrent;
		private final int m_iTotal;
		private final DisassemblyDataMap m_results;

		public DisassemblyRunner(String sClass, DisassemblyDataMap results, int iCurrent, int iTotal) {
			m_sClass = sClass;
			m_results = results;
			m_iCurrent = iCurrent;
			m_iTotal = iTotal;
		}

		@Override
		public void run() {
			updateStatusSync("Starting Disassembly " + m_iCurrent + " of " + m_iTotal + ": " + m_sClass);
			Disassembler disassembler = getDisassembler();
			try {
				DisassemblyData data = disassembler.invoke(m_config.getSourceDirectory(), m_sClass);
				final int numResults = m_results.setResult(m_sClass, data);
				updateStatusSync("Completed Disassembly " + numResults + " of " + m_iTotal + ": " + m_sClass);
			} catch (Exception e) {
				if (e instanceof DisassemblerException)
					m_exception = (DisassemblerException)e;
				else
					m_exception = new DisassemblerException("Error", e);
				m_pool.shutdownNow();
			}
		}
		
	}

	private ExecutorService m_pool;
	private DisassemblerException m_exception = null;

	ThreadedDisassemblyManager(Config config, StatusHandler status) {
		super(config, status);
		m_pool = Executors.newFixedThreadPool(config.getParallelizationFactor());
	}

	@Override
	void disassembleAll(List<String> files, DisassemblyDataMap results) throws DisassemblerException {
		for (int i = 0; i < files.size(); ++i) {
			DisassemblyRunner runner = new DisassemblyRunner(files.get(i), results, i+1, files.size());
			m_pool.execute(runner);
		}
		
		m_pool.shutdown();
		try {
			m_pool.awaitTermination(99, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			if (m_exception != null)
				throw m_exception;
			else
				throw new DisassemblerException();
		}
		if (m_exception != null)
			throw m_exception;
	}

	synchronized void updateStatusSync(String msg) {
		updateStatus(msg);
	}
}
