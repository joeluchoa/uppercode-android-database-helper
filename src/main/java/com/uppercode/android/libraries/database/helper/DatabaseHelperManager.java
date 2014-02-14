package com.uppercode.android.libraries.database.helper;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DatabaseHelperManager {

	private static final String TAG = DatabaseHelperManager.class.getSimpleName();
	private static volatile AtomicInteger sCount = new AtomicInteger();

	public static DatabaseHelper get(Context context, Class<? extends DatabaseHelper> helperClass) {
		sCount.incrementAndGet();
		return OpenHelperManager.getHelper(context, helperClass);
	}

	public static int release() {
		OpenHelperManager.releaseHelper();
		return sCount.decrementAndGet();
	}

	public static void releaseAll() {
		try {
			while (release() > -1)
				;
			sCount.set(0);
		} catch (IllegalStateException e) {
			Log.w(TAG, "DatabaseHelper closed!");
		}
	}
}
