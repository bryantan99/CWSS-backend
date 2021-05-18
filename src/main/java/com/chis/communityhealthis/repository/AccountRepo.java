package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.model.AccountBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<AccountBean, String> {
    AccountBean findAccountByAccountUsername(String username);
}
