package com.nagoya.dao.util;

import java.util.Calendar;
import java.util.Date;

import com.nagoya.model.dbo.DBO;

public final class DefaultDBOFiller {

	private DefaultDBOFiller() {
		// defeat instantiation
	}

	public static void fillDefaultDataObjectValues(DBO dbo) {
		Date dateTime = Calendar.getInstance().getTime();

		if (StringUtil.isNullOrBlank(dbo.getCreationUser())) {
			dbo.setCreationUser("auto");
		}

		if (dbo.getCreationDate() == null) {
			dbo.setCreationDate(dateTime);
		}

		if (StringUtil.isNullOrBlank(dbo.getModificationUser())) {
			dbo.setModificationUser("auto");
		}

		dbo.setModificationDate(dateTime);
	}

}
