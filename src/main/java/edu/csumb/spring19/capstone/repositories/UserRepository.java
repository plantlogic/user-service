package edu.csumb.spring19.capstone.repositories;

import edu.csumb.spring19.capstone.models.PLUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<PLUser, String> {
    Optional<PLUser> findByUsernameIgnoreCase(String username);

    void deleteByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
}
