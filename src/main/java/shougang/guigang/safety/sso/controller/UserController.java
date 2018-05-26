package shougang.guigang.safety.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shougang.guigang.safety.sso.service.UserService;
import shougang.guigang.sanzuoyequ.common.pojo.SafetyResult;
import shougang.guigang.sanzuoyequ.common.utils.ExceptionUtil;

/**
 * user register,login,logout controller
 * @author admin
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	private SafetyResult login(@RequestParam("username") String personId,@RequestParam("password") String personPassowrd, HttpServletRequest request, HttpServletResponse response){
		try {
			SafetyResult result = userService.login(personId, personPassowrd, request, response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return SafetyResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping(value = "/loginManager", method = RequestMethod.POST)
	@ResponseBody
	private SafetyResult loginManager(@RequestParam("username") String personId,@RequestParam("password") String personPassowrd, HttpServletRequest request, HttpServletResponse response){
		try {
			SafetyResult result = userService.loginManager(personId, personPassowrd, request, response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return SafetyResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping("/token/{token}")
	@ResponseBody
	private Object getUserByToken(@PathVariable String token, String callback) {
		try {
			SafetyResult result = userService.getUserByToken(token);
			if (StringUtils.isNotBlank(callback)) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return SafetyResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping("/logout/{token}")
	private String logout(@PathVariable String token) {
		try {
			userService.logout(token);
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect";
		}
	}
	
	@RequestMapping("/check/{personId}/{personPassword}")
	@ResponseBody
	private Object checkData(@PathVariable String personId, @PathVariable String personPassword, String callback) {
		try {
			SafetyResult result = userService.checkData(personId, personPassword);
			if(StringUtils.isNotBlank(callback)) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return SafetyResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	private SafetyResult modifyPassword(@RequestParam("personId") String personId, @RequestParam("password") String personPassword) {
		try {
			SafetyResult result = userService.modifyPassword(personId, personPassword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return SafetyResult.build(500, "修改密码失败！");
		}
	}
}
