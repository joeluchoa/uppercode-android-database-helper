package com.uppercode.android.libraries.database.helper.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.uppercode.android.libraries.database.helper.model.IDatabaseModel;

public class DefaultJsonAdapter<T extends IDatabaseModel> implements JsonSerializer<T>,
		JsonDeserializer<T> {

	private Class<?> mClass;

	public DefaultJsonAdapter(Class<?> cls) {
		mClass = cls;
	}

	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (json != null) {
			try {
				@SuppressWarnings("unchecked")
				T result = (T) mClass.newInstance();
				result.setId(json.getAsInt());
				return result;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		return src == null ? null : new JsonPrimitive(src.getId());
	}

}