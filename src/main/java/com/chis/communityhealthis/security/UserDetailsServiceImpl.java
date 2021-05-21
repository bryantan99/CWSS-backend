package com.chis.communityhealthis.security;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.model.UserDtoModel;
import com.chis.communityhealthis.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountBean accountBean = accountService.getAccountWithRoles(username);
        if (accountBean == null) {
            throw new UsernameNotFoundException("Username" + username + " was not found.");
        }
        UserDetailsModel model = new UserDetailsModel(accountBean);
        return model;
    }

    public AccountBean save(UserDtoModel userDtoModel) {
        AccountBean bean = new AccountBean();
        bean.setUsername(userDtoModel.getUsername());
        bean.setPw(bcryptEncoder.encode(userDtoModel.getPassword()));
        bean.setIsActive("N");
        accountService.addAccount(bean);
        return bean;
    }

}
