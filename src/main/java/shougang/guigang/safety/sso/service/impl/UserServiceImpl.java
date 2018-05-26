package shougang.guigang.safety.sso.service.impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import shougang.guigang.safety.mapper.TbBasicpersonnelinformationMapper;
import shougang.guigang.safety.pojo.TbBasicpersonnelinformation;
import shougang.guigang.safety.pojo.TbBasicpersonnelinformationExample;
import shougang.guigang.safety.pojo.TbBasicpersonnelinformationExample.Criteria;
import shougang.guigang.safety.sso.component.JedisClient;
import shougang.guigang.safety.sso.service.UserService;
import shougang.guigang.sanzuoyequ.common.pojo.SafetyResult;
import shougang.guigang.sanzuoyequ.common.utils.CookieUtils;
import shougang.guigang.sanzuoyequ.common.utils.ExceptionUtil;
import shougang.guigang.sanzuoyequ.common.utils.JsonUtils;

/**
 * user register,login,logout service
 * @author admin
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbBasicpersonnelinformationMapper basicpersonnelinformationMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public SafetyResult login(String personId, String personPassword, HttpServletRequest request,
			HttpServletResponse response) {
		TbBasicpersonnelinformationExample example = new TbBasicpersonnelinformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andPersonIdEqualTo(personId);
		List<TbBasicpersonnelinformation> list = basicpersonnelinformationMapper.selectByExample(example);
		if (list == null || list.isEmpty()) {
			return SafetyResult.build(400, "用户名错误！");
		}
		TbBasicpersonnelinformation basicpersonnelinformation = list.get(0);
		if (!basicpersonnelinformation.getPersonPassword().equals(personPassword)) {
			return SafetyResult.build(400, "密码错误！");
		}
		String token = UUID.randomUUID().toString();

		jedisClient.set(REDIS_SESSION_KEY + ":" + token, JsonUtils.objectToJson(basicpersonnelinformation));
		jedisClient.expire(REDIS_SESSION_KEY + ":" + token, SESSION_EXPIRE);

		CookieUtils.setCookie(request, response, "SAN_SAFER", token);

		return SafetyResult.ok(token);
	}
	
	@Override
	public SafetyResult loginManager(String personId, String personPassword, HttpServletRequest request,
			HttpServletResponse response) {
		TbBasicpersonnelinformationExample example = new TbBasicpersonnelinformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andPersonIdEqualTo(personId);
		List<TbBasicpersonnelinformation> list = basicpersonnelinformationMapper.selectByExample(example);
		if (list == null || list.isEmpty()) {
			return SafetyResult.build(400, "用户名错误！");
		}
		TbBasicpersonnelinformation basicpersonnelinformation = list.get(0);
		if (!basicpersonnelinformation.getPersonPassword().equals(personPassword)) {
			return SafetyResult.build(400, "密码错误！");
		}
		
		if (basicpersonnelinformation.getPersonIsadmin() != 1) {
			return SafetyResult.build(400, "非管理员账号登录！");
		}
		String token = UUID.randomUUID().toString();
		
		jedisClient.set(REDIS_SESSION_KEY + ":" + token, JsonUtils.objectToJson(basicpersonnelinformation));
		jedisClient.expire(REDIS_SESSION_KEY + ":" + token, SESSION_EXPIRE);
		
		CookieUtils.setCookie(request, response, "SAN_SAFER", token);
		
		return SafetyResult.ok(token);
	}

	@Override
	public SafetyResult getUserByToken(String token) {
		String json = jedisClient.get(REDIS_SESSION_KEY + ":" + token);

		if (StringUtils.isBlank(json)) {
			return SafetyResult.build(400, "用户已过期！");
		}

		TbBasicpersonnelinformation basicpersonnelinformation = JsonUtils.jsonToPojo(json,
				TbBasicpersonnelinformation.class);
		jedisClient.expire(REDIS_SESSION_KEY + ":" + token, SESSION_EXPIRE);
		return SafetyResult.ok(basicpersonnelinformation);
	}

	@Override
	public SafetyResult logout(String token) {
		jedisClient.expire(REDIS_SESSION_KEY + ":" + token, 0);
		return SafetyResult.ok();
	}

	@Override
	public SafetyResult checkData(String personId, String personPassword) {
		if(personId == null || personId == "") {
			return SafetyResult.build(400, "必须通过首页登录！");
		}
		TbBasicpersonnelinformationExample example = new TbBasicpersonnelinformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andPersonIdEqualTo(personId);
		List<TbBasicpersonnelinformation> list = basicpersonnelinformationMapper.selectByExample(example);
		if (list == null || list.isEmpty()) {
			return SafetyResult.build(400, "必须通过首页登录！");
		}
		TbBasicpersonnelinformation basicpersonnelinformation = list.get(0);
		if (!basicpersonnelinformation.getPersonPassword().equals(personPassword)) {
			return SafetyResult.build(400, "密码错误！");
		}
		return SafetyResult.ok(true);
	}

	@Override
	public SafetyResult modifyPassword(String personId, String personPassword) {
		try {
			TbBasicpersonnelinformationExample example = new TbBasicpersonnelinformationExample();
			Criteria criteria = example.createCriteria();
			criteria.andPersonIdEqualTo(personId);
			TbBasicpersonnelinformation basicpersonnelinformation = new TbBasicpersonnelinformation();
			basicpersonnelinformation.setPersonPassword(personPassword);
			basicpersonnelinformationMapper.updateByExampleSelective(basicpersonnelinformation, example);
			return SafetyResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SafetyResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
