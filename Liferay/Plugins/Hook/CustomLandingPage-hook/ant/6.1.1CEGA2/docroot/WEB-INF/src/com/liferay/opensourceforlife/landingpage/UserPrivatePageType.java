/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author tejas.kanani
 */
public class UserPrivatePageType extends LandingPageType {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath
	 * (javax.portlet.PortletRequest)
	 */
	@Override
	public String getLandingPagePath(
		HttpServletRequest request, boolean includeLanguage)
		throws PortalException, SystemException {

		String userPrivatePagePath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);

		if (currentUser.hasPrivateLayouts()) {
			userPrivatePagePath =
				CustomLandingPageUtil.getDisplayURL(request, true);
		}
		return userPrivatePagePath;
	}
}
