package ar.edu.itba.iot.carne_iot.server.persistence;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.UserQueryHelper;
import org.hibernate.criterion.MatchMode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Concrete implementation of a {@link UserQueryHelper}.
 */
@Component
public class UserQueryHelperImpl implements UserQueryHelper {

    @Override
    public Specification<User> createUserSpecification(String fullName, LocalDate minBirthDate, LocalDate maxBirthDate,
                                                       String username, String email) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new LinkedList<>();
            // Filter full name
            Optional.ofNullable(fullName)
                    .map(str -> PersistenceHelper
                            .toLikePredicate(cb, root, "fullName", str, MatchMode.ANYWHERE, false))
                    .ifPresent(predicates::add);
            // Filter birth date
            final Path<LocalDate> birthDatePath = root.get(root.getModel()
                    .getDeclaredSingularAttribute("birthDate", LocalDate.class));
            Optional.ofNullable(minBirthDate).map(date -> cb.greaterThanOrEqualTo(birthDatePath, date))
                    .ifPresent(predicates::add);
            Optional.ofNullable(maxBirthDate).map(date -> cb.lessThanOrEqualTo(birthDatePath, date))
                    .ifPresent(predicates::add);
            // Filter username
            Optional.ofNullable(username)
                    .map(str -> PersistenceHelper
                            .toLikePredicate(cb, root, "username", str, MatchMode.ANYWHERE, false))
                    .ifPresent(predicates::add);
            // Filter email
            Optional.ofNullable(email)
                    .map(str -> PersistenceHelper
                            .toLikePredicate(cb, root, "email", str, MatchMode.ANYWHERE, false))
                    .ifPresent(predicates::add);

            return predicates.stream().reduce(cb.and(), cb::and);
        };
    }

    @Override
    public void validatePageable(Pageable pageable) throws InvalidPropertiesException {
        PersistenceHelper.validatePageable(pageable, User.class);
    }
}
