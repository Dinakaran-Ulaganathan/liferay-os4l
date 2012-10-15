/**
 * 
 */

package com.liferay.opensourceforlife.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.liferay.opensourceforlife.landingpage.LandingPageType;
import com.liferay.opensourceforlife.landingpage.LandingPageTypeFactory;
import com.liferay.opensourceforlife.util.CustomLandingPageConstant;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class CustomLandingPageAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		try {
			doRun(request, response);
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	protected void doRun(
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		long companyId = PortalUtil.getCompanyId(request);

		String path =
			PrefsPropsUtil.getString(
				companyId, PropsKeys.DEFAULT_LANDING_PAGE_PATH);

		if (_log.isInfoEnabled()) {
			_log.info(PropsKeys.DEFAULT_LANDING_PAGE_PATH + StringPool.EQUAL +
				path);
		}

		// Check for override.default.landing.page.path property value
		boolean overrideDefaultLandingPagePath =
			PrefsPropsUtil.getBoolean(
				companyId,
				CustomLandingPageConstant.OVERRIDE_DEFAULT_LANDING_PAGE_PATH,
				Boolean.TRUE);

		if (overrideDefaultLandingPagePath) {
			path = getCustomLandingPage(request);
		}

		HttpSession session = request.getSession();
		session.setAttribute(WebKeys.LAST_PATH, new LastPath(
			StringPool.BLANK, path));

	}

	/**
	 * Returns custom landing page path after user login
	 * 
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private String getCustomLandingPage(HttpServletRequest request)
		throws PortalException, SystemException {

		String customLandingPagePath = StringPool.BLANK;
		long companyId = PortalUtil.getCompanyId(request);

		String landingPageTypeSelection =
			PrefsPropsUtil.getString(
				companyId, CustomLandingPageConstant.CUSTOM_LANDING_PAGE_TYPE,
				CustomLandingPageConstant.DEFAULT_LANDING_PAGE_TYPE);

		if (landingPageTypeSelection != null && !landingPageTypeSelection.trim().isEmpty()) {

			// Check for include.language property value
			boolean includeLanguage =
				PrefsPropsUtil.getBoolean(
					companyId, CustomLandingPageConstant.INCLUDE_LANGUAGE,
					Boolean.FALSE);

			LandingPageType landingPageType =
				LandingPageTypeFactory.getLandingPageTypeInstance(landingPageTypeSelection);
			customLandingPagePath =
				landingPageType.getLandingPagePath(request, includeLanguage);
		}

		return customLandingPagePath;
	}

	private static Log _log =
		LogFactoryUtil.getLog(CustomLandingPageAction.class);
}
