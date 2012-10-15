/**
 * 
 */

package com.liferay.opensourceforlife.util;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * @author tejas.kanani
 */
public class CustomLandingPageUtil {

	public static String getLanguage(User user, boolean includeLanguage) {

		String language = StringPool.BLANK;

		if (includeLanguage) {
			language = StringPool.SLASH + user.getLocale().getLanguage();
		}

		return language;
	}

	public static String getDisplayURL(
		HttpServletRequest request, boolean isPrivateLayout)
		throws PortalException, SystemException {

		String displayURL = StringPool.BLANK;

		Group group =
			GroupLocalServiceUtil.getUserGroup(
				PortalUtil.getCompanyId(request), PortalUtil.getUserId(request));

		int publicLayoutsPageCount = group.getPublicLayoutsPageCount();

		if (publicLayoutsPageCount > 0) {
			StringBundler sb = new StringBundler(5);

			sb.append(PortalUtil.getPathMain());
			sb.append("/my_sites/view?groupId=");
			sb.append(group.getGroupId());

			if (isPrivateLayout) {
				sb.append("&privateLayout=1");
			}
			else {
				sb.append("&privateLayout=0");
			}

			displayURL = sb.toString();
		}

		return displayURL;
	}
}
