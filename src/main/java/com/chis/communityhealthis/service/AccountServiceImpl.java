package com.chis.communityhealthis.service;

import com.chis.communityhealthis.model.AccountBean;
import com.chis.communityhealthis.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepo accountRepo;

    @Override
    public AccountBean addAccount(AccountBean accountBean) {
        return accountRepo.saveAndFlush(accountBean);
    }

    @Override
    public AccountBean getAccount(String username) {
        AccountBean accountBean = accountRepo.findAccountByAccountUsername(username);
        Assert.notNull(accountBean, "Account not found");
        return accountBean;
    }
}
