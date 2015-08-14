/**
 * 
 */
package me.chongchong.norway.bean;

import org.springframework.context.SmartLifecycle;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public abstract class AutoStartBean implements SmartLifecycle {

	private boolean isRunning = false;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.Lifecycle#start()
	 */
	@Override
	final public void start() {
		isRunning = true;
		doStart();
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.Lifecycle#stop()
	 */
	@Override
	final public void stop() {
		isRunning = false;
		doStop();
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.Lifecycle#isRunning()
	 */
	@Override
	final public boolean isRunning() {
		return isRunning;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.SmartLifecycle#isAutoStartup()
	 */
	@Override
	final public boolean isAutoStartup() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.SmartLifecycle#stop(java.lang.Runnable)
	 */
	@Override
	final public void stop(Runnable callback) {
		stop();
		callback.run();
	}

	protected abstract void doStart();
	protected abstract void doStop();

}
