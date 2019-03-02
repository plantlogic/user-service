package edu.csumb.spring19.capstone.repositories;

import edu.csumb.spring19.capstone.models.PLUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<PLUser, String> {
    @Override
    Optional<PLUser> findById(String username);

    @Override
    void delete(PLUser toDelete);
}
