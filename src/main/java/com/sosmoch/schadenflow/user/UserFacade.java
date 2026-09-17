package com.sosmoch.schadenflow.user;

import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
    UserView createUser(CreateUserCommand command);
    Optional<UserView> findUserById(UUID id);
    Optional<UserView> findUserByEmail(String email);

}
