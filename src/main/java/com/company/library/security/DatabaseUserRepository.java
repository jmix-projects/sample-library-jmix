package com.company.library.security;

import com.company.library.entity.User;
import io.jmix.securitydata.user.AbstractDatabaseUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component("libr_UserRepository")
public class DatabaseUserRepository extends AbstractDatabaseUserRepository<User> {

    @Override
    protected Class<User> getUserClass() {
        return User.class;
    }
}