package home.projects.JavaDisassembly.Drivers;

import home.projects.JavaDisassembly.StatusHandler;

class CommandLineStatusHandler implements StatusHandler {

	@Override
	public void updateStatus(String msg) {
		System.out.println(msg);
	}

}
