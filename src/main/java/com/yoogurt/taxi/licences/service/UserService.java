package com.yoogurt.taxi.licences.service;

import com.yoogurt.taxi.licences.dal.beans.UserInfo;
import com.yoogurt.taxi.licences.dal.model.SessionUser;
import com.yoogurt.taxi.licences.form.LoginForm;

import java.util.List;

public interface UserService {

    SessionUser login(LoginForm form);

    List<UserInfo> getUserList(String username, Integer type, Integer status);

    UserInfo getUserByUsername(String username);
}
