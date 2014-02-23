package com.uppercode.android.libraries.database.helper.json.analiselogica;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.uppercode.android.libraries.database.helper.json.DatabaseJsonHelper;

public class AlDatabaseJsonHelper extends DatabaseJsonHelper {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";

	public AlDatabaseJsonHelper(Class<?>[] models) {
		super(models);
	}

	@Override
	protected GsonBuilder config(GsonBuilder builder) {
		builder.setDateFormat(DEFAULT_DATE_FORMAT);
		builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		builder.serializeNulls();
		return super.config(builder);
	}

}
