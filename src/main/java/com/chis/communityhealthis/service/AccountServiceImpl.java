package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.repository.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountDao accountDao;

    @Override
    public void addAccount(AccountBean accountBean) {
        accountDao.add(accountBean);
    }

    @Override
    public AccountBean getAccount(String username) {
        return accountDao.findAccount(username);
    }

    @Override
    @Transactional
    public AccountBean getAccountWithRoles(String username) {
        return accountDao.findAccountWithRoles(username);
    }
}
