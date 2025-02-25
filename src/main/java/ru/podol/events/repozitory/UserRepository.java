package ru.podol.events.repozitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
