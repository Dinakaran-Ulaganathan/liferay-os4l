/**
 * Returns custom landing path for first site which user belongs to
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.opensourceforlife.util.CustomLandingPageConstant;
import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * @author tejas.kanani
 */
public class SiteType extends LandingPageType {

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

		String sitePath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);

		List<Group> userSites = getSites(currentUser.getUserId());

		if (userSites != null && !userSites.isEmpty()) {
			// If user is member of more than one sites then it will take
			// first site from list
			String siteFriendlyURL = userSites.get(0).getFriendlyURL();
			sitePath =
				CustomLandingPageUtil.getLanguage(request) +
					PortalUtil.getPathFriendlyURLPublic() + siteFriendlyURL;
		}
		return sitePath;
	}

	private List<Group> getSites(long userId)
		throws PortalException, SystemException {

		List<Group> sites = new ArrayList<Group>();

		for (Group group : GroupLocalServiceUtil.getUserGroups(userId)) {
			if (group.isRegularSite() &&
				!CustomLandingPageConstant.GUEST_GROUP_FRIENDLY_URL.equalsIgnoreCase(group.getFriendlyURL())) {
				sites.add(group);
				break;
			}
		}
		return sites;
	}
}
