package ar.edu.itba.iot.carne_iot.server.persistence.custom_repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * Defines Behaviour for an Extended {@link JpaRepository}, which also extends {@link JpaSpecificationExecutor}
 * in order to perform queries using {@link Specification}.
 * It also adds a method to get an {@link Optional} of an entity by its id, and by a given {@link Specification}.
 */
@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@code null}.
     * @return A <b>nullable</b> {@link Optional} of the specific type {@code T}
     * containing the entity with the given {@code id}, or {@code null} if none is found.
     * @throws IllegalArgumentException If {@code id} is {@code null}.
     */
    default Optional<T> findById(ID id) {
        return Optional.ofNullable(this.findOne(id));
    }

    /**
     * Retrieves an entity by the given {@link Specification}.
     *
     * @param spec The {@link Specification} that the resulting entity must match.
     * @return A <b>nullable</b> {@link Optional} of the specific type {@code T}
     * containing the entity that matched the given {@link Specification}, or {@code null} if none is found.
     */
    default Optional<T> findBySpec(Specification<T> spec) {
        return Optional.ofNullable(this.findOne(spec));
    }
}
