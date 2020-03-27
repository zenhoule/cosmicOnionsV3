package fr.inti.cosmiconions.repository;

import fr.inti.cosmiconions.domain.Don;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Don entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonRepository extends JpaRepository<Don, Long> {

    @Query("select don from Don don where don.user.login = ?#{principal.username}")
    List<Don> findByUserIsCurrentUser();

}
