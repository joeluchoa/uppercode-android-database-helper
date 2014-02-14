package com.uppercode.android.libraries.database.helper.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uppercode.android.libraries.database.helper.model.IDatabaseModel;

public class DatabaseJsonHelper {

	private Class<?>[] mModels = new Class<?>[0];

	public DatabaseJsonHelper() {
	}

	public DatabaseJsonHelper(Class<?>[] models) {
		this.mModels = models;
	}

	public GsonBuilder getBuilder() {
		return getBuilder(null);
	}

	public GsonBuilder getBuilder(Class<?> toExclude) {
		GsonBuilder b = config(new GsonBuilder());
		for (Class<?> cls : mModels) {
			if (!cls.equals(toExclude)) {
				b.registerTypeAdapter(cls, new DefaultJsonAdapter<IDatabaseModel>(cls));
			}
		}
		return b;
	}

	public Gson build() {
		return build(null);
	}

	public Gson build(Class<?> toExclude) {
		return getBuilder(toExclude).create();
	}

	protected GsonBuilder config(GsonBuilder builder) {
		return builder;
	}
}
