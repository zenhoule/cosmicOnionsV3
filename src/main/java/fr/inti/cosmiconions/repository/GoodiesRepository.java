package fr.inti.cosmiconions.repository;

import fr.inti.cosmiconions.domain.Goodies;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Goodies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodiesRepository extends JpaRepository<Goodies, Long> {

    @Query("select goodies from Goodies goodies where goodies.user.login = ?#{principal.username}")
    List<Goodies> findByUserIsCurrentUser();

}
