package knu.myhealthhub.portalnew.repository;

import knu.myhealthhub.portalnew.login_model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
