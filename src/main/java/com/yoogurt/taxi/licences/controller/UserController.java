package com.yoogurt.taxi.licences.controller;

import com.yoogurt.taxi.licences.dal.enums.StatusCode;
import com.yoogurt.taxi.licences.dal.model.ResponseObj;
import com.yoogurt.taxi.licences.dal.model.SessionUser;
import com.yoogurt.taxi.licences.form.LoginForm;
import com.yoogurt.taxi.licences.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/web/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/i/login")
    public ResponseObj login(@RequestBody @Valid LoginForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseObj.fail(StatusCode.FORM_INVALID, result.getAllErrors().get(0).getDefaultMessage());
        }
        SessionUser user = userService.login(form);
        if (user != null) {
            return ResponseObj.success(user);
        }
        return ResponseObj.fail(StatusCode.BIZ_FAILED, "登录失败，请核对登录账号和密码");
    }

    @GetMapping(value = "/list")
    public ResponseObj getUserList() {
        return ResponseObj.success(userService.getUserList("admin", 0, 10));
    }
}
