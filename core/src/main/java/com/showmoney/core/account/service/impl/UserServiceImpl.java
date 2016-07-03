/*
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * Module Name : com.showmoney.core.account.service.impl.UserServiceImpl
 * Module Description :
 * 
 * Date Created : 2008/4/24
 * Original Author : jeffma
 * Team : 
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * MODIFICATION HISTORY
 * ------------------------------------------------------------------------------
 * Date Modified Modified by Comments
 * ------------------------------------------------------------------------------
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package com.showmoney.core.account.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.showmoney.core.account.dao.UserDao;
import com.showmoney.core.account.entity.Group;
import com.showmoney.core.account.entity.Permission;
import com.showmoney.core.account.entity.Role;
import com.showmoney.core.account.entity.User;
import com.showmoney.core.account.entity.UserPermission;
import com.showmoney.core.account.service.GroupService;
import com.showmoney.core.account.service.RoleService;
import com.showmoney.core.account.service.UserService;
import com.showmoney.core.common.dao.ObjectDao;
import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.entity.UserDetails;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.TemplateService;
import com.showmoney.core.common.service.impl.DomainServiceImpl;
import com.showmoney.core.common.utils.DateUtils;
import com.showmoney.core.common.utils.StringUtils;
import com.showmoney.core.message.entity.Mail;
import com.showmoney.core.message.service.MailService;

/**
 * @author jeffma
 */
public class UserServiceImpl extends DomainServiceImpl<User> implements UserService {

	public final static int PASSWORD_LENGTH = 8;

	/** passwordEncoder, default: PlaintextPasswordEncoder() */
	@Autowired
	private PasswordEncoder passwordEncoder;
	/** templateService */
	@Autowired
	protected TemplateService templateService;
	/** mailService */
	@Autowired
	protected MailService mailService;
	/** groupService */
	@Autowired
	private GroupService groupService;
	/** roleService */
	@Autowired
	private RoleService roleService;
	private UserDao dao;

	private String charSet = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKMNPQRSTUVWXYZ";
	/**
	 * 若有設定, 則搜尋出的 user.groupName 必須以此開頭
	 */
	private String companyCode = null;
	/** maxError, default: 999 */
	private int maxError = 999;

	/** default constructors */
	public UserServiceImpl() {
		super();
		setDefaultSort(new String[] { "loginId asc" });
	}

	/**
	 * @param companyCode
	 *           the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @param charSet
	 *           the charSet to set
	 */
	public void setCharSet(String charSet) {
		if (charSet != null) {
			this.charSet = charSet;
		}
	}

	/**
	 * @param maxError
	 *           the maxError to set
	 */
	public void setMaxError(int maxError) {
		this.maxError = maxError;
	}

	/**
	 * @param roleService
	 *           the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @param templateService
	 *           the templateService to set
	 */
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	/**
	 * @param mailService
	 *           the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * @param passwordEncoder
	 *           the passwordEncoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * @param groupService
	 *           the groupService to set
	 */
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * @param userDao
	 */
	@Override
	public void setDao(ObjectDao<User> userDao) {
		if (userDao instanceof UserDao) {
			this.dao = (UserDao) userDao;
		}
		super.setDao(userDao);
	}

	@Transactional(readOnly = false)
	public User changePassword(User user, String newPassword, String oldPassword) throws CoreException {
		if (user == null) {
			throw new CoreException("errors.account.user.empty");
		}
		if (!user.isEnabled()) {
			throw new CoreException("errors.account.user.status", user.getLoginId());
		}
		if (validatePassword(user, oldPassword)) {
			user.setPassword(passwordEncoder.encodePassword(newPassword, null));
			user.setNeedChangePassword(false);
			// 直接 update 不用 resetPermission
			super.save(user);
		} else {
			throw new CoreException("errors.account.user.password");
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.UserService#resetPassword(com.showmoney.core.account.entity.User, boolean)
	 */
	@Transactional(readOnly = false)
	public User resetPassword(User user, String password, boolean sendEmail) throws CoreException {
		if (StringUtils.isBlank(password)) {
			password = getRandPassword();
		}
		user.setPassword(passwordEncoder.encodePassword(password, null));
		user.setNeedChangePassword(true);
		logger.debug("reset password, user:{}, password:{}", user.getLoginId(), password);
		// 直接 update 不用 resetPermission
		super.save(user);
		if (sendEmail) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userObj", user);
			m.put("newPassword", password);
			Mail mail = templateService.formatToMail("account/User.resetPassword", m);
			mail.addTo(user.getNameNative(), user.getEmail());
			mailService.save(mail);
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.UserService#validatePassword(com.showmoney.core.account.entity.User,
	 * java.lang.String)
	 */
	@Transactional(readOnly = false)
	public boolean validatePassword(User user, String password) throws CoreException {
		boolean valid = false;
		boolean update = false;

		valid = passwordEncoder.isPasswordValid(user.getPassword(), password, null);
		logger.debug("userId:" + user.getUsername() + ",input:" + password + ",passwd:" + user.getPassword());
		if (valid && (user.getErrorCount() > 0)) {
			user.setErrorCount(0);
			update = true;
		} else if (!valid) {
			user.setErrorCount(user.getErrorCount() + 1);
			if (user.getErrorCount() >= maxError) {
				user.setAccountNonLocked(false);
			}
			update = true;
		}

		if (update) {
			super.save(user);
		}
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.UserService#getByGroup(java.lang.String)
	 */
	public List<User> getByGroup(String groupKey) throws CoreException {
		CommonCriteria cri = new CommonCriteria();
		cri.addEq("group.code", groupKey);
		List<User> users = super.getList(0, -1, cri, null);
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.UserService#getByRole(java.lang.String,
	 * com.showmoney.core.account.entity.Role.Type)
	 */
	@SuppressWarnings("unchecked")
	public List<User> getByRole(String roleKey, Role.Type type, String groupCode) throws CoreException {
		List<User> result = null;
		Role role = roleService.get(roleKey, type);
		if (role != null) {
			List<Object> attrs = new ArrayList<Object>();
			String hql = "from User u where ? in elements(u.roles) and u.accountNonLocked=? and u.accountExpireDate<?";
			attrs.add(role);
			attrs.add(Boolean.TRUE);
			attrs.add(DateUtils.getCurrentTime());
			if (StringUtils.isNotBlank(groupCode)) {
				hql += " and u.groupName=?";
				attrs.add(groupCode);
			}

			result = getDao().getQueryByList(hql + " order by u.loginId", attrs, 0, -1);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#save(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public User save(User entity) throws CoreException {
		if (StringUtils.isNotBlank(companyCode) && !entity.getGroupCode().startsWith(companyCode)) {
			throw new CoreException("errors.account.user.permissionDeny", entity.getUsername(), companyCode);
		}
		resetPermissions(entity);
		return super.save(entity);
	}

	/**
	 * @param user
	 */
	private void resetPermissions(User user) {
		Set<String> newPermissionNames = new HashSet<String>();

		// check roles
		for (Role role : user.getRoles()) {
			logger.debug("role:{}", role);
			if (role == null) {
				break;
			}
			for (Permission permission : role.getPermissions().values()) {
				newPermissionNames.add(permission.getName());
				if (user.getPermissions().get(permission.getName()) == null) {
					user.getPermissions().put(permission.getName(),
							new UserPermission(user, permission.getName(), permission.getType()));
				}
			}
		}

		// check group
		Role role = user.getGroup().getRole();
		for (Permission permission : role.getPermissions().values()) {
			newPermissionNames.add(permission.getName());
			if (user.getPermissions().get(permission.getName()) == null) {
				user.getPermissions().put(permission.getName(),
						new UserPermission(user, permission.getName(), permission.getType()));
			}
		}

		List<String> removeList = new ArrayList<String>();
		for (UserPermission p : user.getPermissions().values()) {
			if (!newPermissionNames.contains(p.getPermissionKey())) {
				removeList.add(p.getPermissionKey());
			}
		}
		for (String key : removeList) {
			user.getPermissions().remove(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#get(java.io.Serializable)
	 */
	@Override
	public User get(Serializable oid) throws CoreException {
		User user = super.get(oid);
		if (user != null) {
			if (StringUtils.isNotBlank(companyCode) && !user.getGroupCode().startsWith(companyCode)) {
				logger.error("錯誤 companyCode, username:{}, groupName:{}, companyCode:{}", new Object[] {
						user.getUsername(), user.getGroupCode(), companyCode });
				return null;
			}
			Hibernate.initialize(user.getRoles());
			Hibernate.initialize(user.getPermissions());
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserDetails entity = null;
		try {
			entity = get(username);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RecoverableDataAccessException(e.getMessage());
		}
		if (entity == null) {
			throw new UsernameNotFoundException(username);
		}
		return entity;
	}

	public String getRandPassword() {
		StringBuffer sb = new StringBuffer(PASSWORD_LENGTH);
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		String clone = String.valueOf(charSet);
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int j = rand.nextInt() % clone.length();
			j = j < 0 ? -j : j;
			sb.append(clone.charAt(j));
			StringUtils.remove(clone, clone.charAt(j));
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.UserService#getByPermission(java.lang.String,
	 * com.showmoney.core.account.entity.Permission.Type)
	 */
	@SuppressWarnings("unchecked")
	public List<User> getByPermission(String permissionKey, Permission.Type type, String groupCode) {
		List<User> result = null;
		if (StringUtils.isNotBlank(permissionKey)) {
			List<String> param = new ArrayList<String>();
			StringBuffer hql = new StringBuffer()
					.append("from User u where u.permissions[?] is not null and u.accountNonLocked=?");
			param.add(type.name() + "_" + permissionKey);
			param.add("Y");
			if (StringUtils.isNotBlank(groupCode)) {
				param.add(groupCode);
				hql.append(" and u.groupName = ?");
			}
			if (StringUtils.isNotBlank(companyCode)) {
				hql.append(" and u.groupName like '" + companyCode + "%'");
			}
			result = getDao().getQueryByList(hql.toString(), param, 0, -1);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.account.service.UserService#createUser(com.showmoney.core.account.entity.User,
	 * java.lang.String, java.lang.String, boolean)
	 */
	@Transactional(readOnly = false)
	public User createUser(User entity, String password, String groupCode, boolean sendEmail) throws CoreException {
		if (StringUtils.isNotBlank(companyCode) && !groupCode.startsWith(companyCode)) {
			throw new CoreException("errors.account.user.permissionDeny", entity.getUsername(), groupCode, companyCode);
		}
		entity.setLoginId(entity.getUsername().toLowerCase());

		if (get(entity.getUsername()) != null) {
			throw new CoreException("帐号重覆", entity.getUsername());
		}

		Group group = groupService.get(groupCode);
		if (group == null) {
			throw new CoreException("群组/部门不存在,请确认", groupCode);
		}
		entity.setGroup(group);
		entity.getRoles().add(group.getRole());

		if (StringUtils.isBlank(password)) {
			password = getRandPassword();
		}
		entity.setPassword(passwordEncoder.encodePassword(password, null));
		resetPermissions(entity);
		entity = super.save(entity);

		if (sendEmail) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userObj", entity);
			m.put("password", password);
			Mail mail = templateService.formatToMail("account/User.create", m);
			mail.addTo(entity.getNameNative(), entity.getEmail());
			mailService.save(mail);
		}

		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#getList(int, int,
	 * com.showmoney.core.common.dao.impl.CommonCriteria, java.lang.String[])
	 */
	@Override
	public List<User> getList(int firstResult, int maxResults, CommonCriteria criteria, String[] sortOrder)
			throws CoreException {
		return this.getListByRole(firstResult, maxResults, criteria, sortOrder, null, null);
	}

	public List<User> getListByRole(int firstResult, int maxResults, CommonCriteria criteria, String[] sortOrder,
			Role.Type roleType, String roleKey) throws CoreException {
		if (StringUtils.isNotBlank(companyCode)) {
			if (criteria == null) {
				criteria = new CommonCriteria();
			}

			if (criteria.getRlike().get("groupName") != null) {
				if (!StringUtils.startsWith(criteria.getRlike().get("groupName"), companyCode)) {
					logger.error("錯誤 companyCode, input:{}, companyCode:{}", criteria.getRlike().get("groupName"),
							companyCode);
					return null;
				}
			} else {
				criteria.addRlike("groupName", companyCode);
			}
		}

		if ((roleType == null) && (roleKey == null)) {
			return super.getList(firstResult, maxResults, criteria, sortOrder);
		} else {
			return dao.getListPageableByRole(criteria, sortOrder, firstResult, maxResults, roleType, roleKey);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.showmoney.core.common.service.impl.DomainServiceImpl#getListSize(com.showmoney.core.common.dao.impl.
	 * CommonCriteria)
	 */
	@Override
	public Number getListSize(CommonCriteria criteria) throws CoreException {
		return this.getListSizeByRole(criteria, null, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.showmoney.core.account.service.UserService#getListSizeByRole(com.showmoney.core.common.dao.impl.CommonCriteria
	 * , com.showmoney.core.account.entity.Role.Type, java.lang.String)
	 */
	public Number getListSizeByRole(CommonCriteria criteria, Role.Type roleType, String roleKey) throws CoreException {
		if (StringUtils.isNotBlank(companyCode)) {
			if (criteria == null) {
				criteria = new CommonCriteria();
			}

			if (criteria.getRlike().get("groupName") != null) {
				if (!StringUtils.startsWith(criteria.getRlike().get("groupName"), companyCode)) {
					logger.error("錯誤 companyCode, input:{}, companyCode:{}", criteria.getRlike().get("groupName"),
							companyCode);
					return null;
				}
			} else {
				criteria.addRlike("groupName", companyCode);
			}
		}

		if ((roleType == null) && (roleKey == null)) {
			return super.getListSize(criteria);
		} else {
			return dao.getListSizeByRole(criteria, roleType, roleKey);
		}
	}

}
