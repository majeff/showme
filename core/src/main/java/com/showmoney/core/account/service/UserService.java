/*
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * Module Name : com.showmoney.core.account.service.UserService
 * Module Description :
 * 
 * Date Created : Apr 16, 2008
 * Original Author : jeffma
 * Team : 
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * MODIFICATION HISTORY
 * ------------------------------------------------------------------------------
 * Date Modified Modified by Comments
 * ------------------------------------------------------------------------------
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package com.showmoney.core.account.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.showmoney.core.account.entity.Permission;
import com.showmoney.core.account.entity.Role;
import com.showmoney.core.account.entity.User;
import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.exception.CoreException;
import com.showmoney.core.common.service.DomainService;

/**
 * @author jeffma
 */
public interface UserService extends DomainService<User>, UserDetailsService {

	/**
	 * 變更密碼
	 * 
	 * @param user
	 * @param newPassword
	 * @param oldPassword
	 * @throws CoreException
	 *            errors.account.password 密码错误<br>
	 *            errors.account.empty 帐号不存在<br>
	 *            errors.account.status 状态不正确
	 */
	public User changePassword(User user, String newPassword, String oldPassword) throws CoreException;

	/**
	 * @param user
	 * @param sendEmail
	 *           是否寄送電子郵件
	 * @return
	 * @throws CoreException
	 */
	public User resetPassword(User user, String password, boolean sendEmail) throws CoreException;

	/**
	 * 驗證密碼
	 * 
	 * @param user
	 * @param password
	 * @param targetIP
	 * @return
	 * @throws CoreException
	 */
	public boolean validatePassword(User user, String password) throws CoreException;

	public User createUser(User entity, String password, String groupCode, boolean sendEmail) throws CoreException;

	public List<User> getByRole(String roleKey, Role.Type type, String groupCode) throws CoreException;

	public List<User> getByPermission(String permissionKey, Permission.Type type, String groupCode) throws CoreException;

	/**
	 * @param groupKey
	 * @return
	 * @throws CoreException
	 */
	public List<User> getByGroup(String groupKey) throws CoreException;

	public String getRandPassword();

	public List<User> getListByRole(int firstResult, int maxResults, CommonCriteria criteria, String[] sortOrder,
			Role.Type roleType, String roleKey) throws CoreException;

	public Number getListSizeByRole(CommonCriteria criteria, Role.Type roleType, String roleKey) throws CoreException;

}
