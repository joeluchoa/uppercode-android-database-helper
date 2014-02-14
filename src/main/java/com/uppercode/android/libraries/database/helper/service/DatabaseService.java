package com.uppercode.android.libraries.database.helper.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uppercode.android.libraries.database.helper.DatabaseHelper;
import com.uppercode.android.libraries.database.helper.json.DatabaseJsonHelper;
import com.uppercode.android.libraries.database.helper.model.IDatabaseModel;

public class DatabaseService<T extends IDatabaseModel> {

	private static final String TAG = DatabaseService.class.getSimpleName();

	private Dao<T, Integer> mDao;
	private DatabaseJsonHelper mJsonHelper;
	private Class<T> mClass;

	public DatabaseService(DatabaseHelper dbHelper, Class<T> cls) {
		mJsonHelper = dbHelper.getJsonHelper();
		mDao = dbHelper.getModelDao(cls);
		mClass = cls;
	}

	public T loadFromJson(String json) {
		try {
			T model = mJsonHelper.build(mClass).fromJson(json, mClass);
			return model;
		} catch (Exception e) {
			Log.e(TAG, "LoadFromJson error!", e);
		}
		return null;
	}

	public CreateOrUpdateStatus save(T model) {
		try {
			return mDao.createOrUpdate(model);
		} catch (SQLException e) {
			Log.e(TAG, "Save error!", e);
		}
		return null;
	}

	public void refresh(T model) {
		try {
			mDao.refresh(model);
		} catch (SQLException e) {
			Log.e(TAG, "Refresh error!", e);
		}
	}

	public int remove(T model) {
		try {
			return mDao.delete(model);
		} catch (SQLException e) {
			Log.e(TAG, "Remove error!", e);
		}
		return 0;
	}

	public T findById(Integer id) {
		try {
			return mDao.queryForId(id);
		} catch (SQLException e) {
			Log.e(TAG, "FindById error!", e);
		}
		return null;
	}

	public List<T> getAll() {
		try {
			return mDao.queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "GetAll error!", e);
		}
		return new ArrayList<T>();
	}

	public List<T> getAll(String... columns) {
		List<T> list = new ArrayList<T>();
		try {
			QueryBuilder<T, Integer> b = mDao.queryBuilder();
			PreparedQuery<T> q = b.selectColumns(columns).prepare();
			list = mDao.query(q);
		} catch (SQLException e) {
			Log.e(TAG, "GetAll error!", e);
		}
		return list;
	}

	public List<T> findBy(String column, Object value) {
		List<T> list = new ArrayList<T>();
		try {
			list = mDao.queryForEq(column, value);
		} catch (SQLException e) {
			Log.e(TAG, "FindBy error!", e);
		}
		return list;
	}

	public void removeAll() {
		try {
			for (T m : getAll()) {
				mDao.delete(m);
			}
		} catch (SQLException e) {
			Log.e(TAG, "RemoveAll error!", e);
		}
	}

	public Dao<T, Integer> getDao() {
		return mDao;
	}
}
