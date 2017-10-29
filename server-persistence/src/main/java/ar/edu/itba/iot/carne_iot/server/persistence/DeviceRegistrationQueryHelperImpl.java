package ar.edu.itba.iot.carne_iot.server.persistence;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.DeviceRegistrationQueryHelper;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.UserQueryHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Concrete implementation of a {@link DeviceRegistrationQueryHelper}.
 */
@Component
public class DeviceRegistrationQueryHelperImpl implements DeviceRegistrationQueryHelper {

    @Override
    public Specification<DeviceRegistration> createDeviceSpecification(User owner) {
        Assert.notNull(owner, "The owner can not be null.");

        return (root, query, cb) -> {
            // Filter owner
            final Path<User> ownerPath = root.get(root.getModel()
                    .getDeclaredSingularAttribute("owner", User.class));
            Predicate ownerPredicate = cb.equal(ownerPath, owner);

            // Only those active
            final Path<Boolean> activePath = root.get(root.getModel()
                    .getDeclaredSingularAttribute("active", Boolean.class));
            Predicate activePredicate = cb.equal(activePath, true);

            return cb.and(ownerPredicate, activePredicate);
        };
    }

    @Override
    public void validatePageable(Pageable pageable) throws InvalidPropertiesException {
        final Set<String> properties = Stream.concat(
                // Can be sorted by Device fields
                Arrays.stream(Device.class.getDeclaredFields()).map(Field::getName),
                // Also can be sorted by nickname
                Stream.of("nickname")
        ).collect(Collectors.toSet());

        PersistenceHelper.validatePageable(pageable, properties);
    }

    @Override
    public Sort adaptSort(Sort sort) {
        Assert.notNull(sort, "The sort must not be null.");

        final Set<String> deviceProperties = Arrays.stream(Device.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        final Spliterator<Sort.Order> spliterator = Spliterators
                .spliteratorUnknownSize(sort.iterator(), Spliterator.ORDERED);

        final List<Sort.Order> adaptedProperties = StreamSupport.stream(spliterator, false)
                .filter(order -> deviceProperties.contains(order.getProperty()))
                .map(order -> order.withProperty("device." + order.getProperty()))
                .collect(Collectors.toList());

        return new Sort(adaptedProperties);
    }
}
