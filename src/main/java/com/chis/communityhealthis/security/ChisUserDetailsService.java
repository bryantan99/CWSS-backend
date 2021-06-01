package com.chis.communityhealthis.security;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.model.UserDtoModel;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.signup.PersonalDetailForm;
import com.chis.communityhealthis.service.AccountService;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ChisUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountBean accountBean = accountService.getAccountWithRoles(username);
        if (accountBean == null) {
            throw new UsernameNotFoundException("Username" + username + " was not found.");
        }
        UserDetailsModel model = new UserDetailsModel(accountBean);
        return model;
    }

    public boolean createAccount(AccountRegistrationForm form) {
        return accountService.addAccount(form);
    }

}
