package com.usi.comm.log;

public final class LogFactory {

	public <T> Log<T> getLog(Class<T> cls) {
		return new Log<T>(cls);
	}
}
