package org.demo.read.projection.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * A repository to persist {@link AccountProjection} entities in the database.
 *
 * @author mmroczkowski
 */
@Repository
public interface AccountProjectionRepository extends JpaRepository<AccountProjection, UUID> {
}