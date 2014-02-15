package com.uppercode.android.libraries.database.helper;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.uppercode.android.libraries.database.helper.json.DatabaseJsonHelper;
import com.uppercode.android.libraries.database.helper.model.IDatabaseModel;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String TAG = DatabaseHelper.class.getSimpleName();

	public static final int LOCAL_ID_START_VALUE = 1234567890;
	public static final int WEB_ID_MAX_VALUE = LOCAL_ID_START_VALUE - 1;

	private Class<?>[] mTables = null;
	private DatabaseJsonHelper mJsonHelper;

	public DatabaseHelper(Context context, String dbName, int dbVersion, Class<?>[] tables) {
		super(context, dbName, null, dbVersion);
		this.mTables = tables;
		mJsonHelper = new DatabaseJsonHelper(mTables);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		createAllTables(connectionSource);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		resetDatabase(connectionSource);
	}

	protected void resetDatabase(ConnectionSource connectionSource) {
		dropAllTables(connectionSource);
		createAllTables(connectionSource);
	}

	protected void createAllTables(ConnectionSource connectionSource) {
		try {
			for (Class<?> table : mTables) {
				TableUtils.createTable(connectionSource, table);
				setStartIdValue(table);
			}
		} catch (java.sql.SQLException e) {
			Log.e(TAG, "Can't create database", e);
		}
	}

	protected void dropAllTables(ConnectionSource connectionSource) {
		try {
			for (Class<?> table : mTables) {
				TableUtils.dropTable(connectionSource, table, true);
			}
		} catch (SQLException e) {
			Log.e(TAG, "Can't upgrade database", e);
		}
	}

	public <D extends Dao<T, ?>, T> D getModelDao(Class<T> cls) {
		try {
			return getDao(cls);
		} catch (SQLException e) {
			Log.e(TAG, "Error to get DAO for class " + cls.getSimpleName(), e);
		}
		return null;
	}

	public DatabaseJsonHelper getJsonHelper() {
		return mJsonHelper;
	}

	public static <T extends IDatabaseModel> boolean registerFromWeb(T m) {
		return m.getId() <= WEB_ID_MAX_VALUE;
	}

	public static <T extends IDatabaseModel> boolean localRegister(T m) {
		return m.getId() >= LOCAL_ID_START_VALUE;
	}

	private <T> void setStartIdValue(Class<T> cls) {
		try {
			T m = cls.newInstance();
			((IDatabaseModel) m).setId(LOCAL_ID_START_VALUE);
			getModelDao(cls).create(m);
			getModelDao(cls).delete(m);
		} catch (InstantiationException e) {
			Log.e(TAG, "Error setStartIdValue " + cls.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Error setStartIdValue " + cls.getSimpleName(), e);
		} catch (SQLException e) {
			Log.e(TAG, "Error setStartIdValue " + cls.getSimpleName(), e);
		}
	}
}
