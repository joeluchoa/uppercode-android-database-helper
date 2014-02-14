package com.uppercode.android.libraries.database.helper;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String TAG = DatabaseHelper.class.getSimpleName();

	private Class<?>[] mTables = null;

	public DatabaseHelper(Context context, String dbName, int dbVersion, Class<?>[] tables) {
		super(context, dbName, null, dbVersion);
		this.mTables = tables;
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
}
