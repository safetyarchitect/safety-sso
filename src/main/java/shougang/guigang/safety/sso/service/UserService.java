package shougang.guigang.safety.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shougang.guigang.sanzuoyequ.common.pojo.SafetyResult;

public interface UserService {

	SafetyResult login(String personId, String personPassowrd, HttpServletRequest request,
			HttpServletResponse response);
	
	SafetyResult loginManager(String personId, String personPassowrd, HttpServletRequest request,
			HttpServletResponse response);

	SafetyResult getUserByToken(String token);

	SafetyResult logout(String token);
	
	SafetyResult checkData(String personName, String personPassword);
	
	SafetyResult modifyPassword(String personId, String personPassword);
}
