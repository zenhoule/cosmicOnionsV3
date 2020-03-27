package fr.inti.cosmiconions.repository;

import fr.inti.cosmiconions.domain.LeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeTypeRepository extends JpaRepository<LeType, Long> {

}
