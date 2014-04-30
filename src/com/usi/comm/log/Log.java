package com.usi.comm.log;

public class Log<T> {
	private final String className;

	Log(Class<T> c) {
		className = c.getSimpleName();
	}

	public void d(String s) {
		android.util.Log.d(className, s);
	}
}
