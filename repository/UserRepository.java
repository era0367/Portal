package knu.myhealthhub.portalnew.repository;

import knu.myhealthhub.portalnew.login_model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameContaining(String username);
}
