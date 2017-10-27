package ar.edu.itba.iot.carne_iot.server.models;

import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;

import javax.persistence.*;
import java.util.Objects;

/**
 * Class representing a user of the application.
 */
@Entity
@Table(name = "sessions", indexes = {
        @Index(name = "sessions_user_id_jti_unique_index", columnList = "user_id, jti", unique = true),
})
public class Session {

    /**
     * The user's id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The {@link User} owning this blacklisted JWT
     */
    @JoinColumn(columnDefinition = "integer", name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    /**
     * The JWT id (The JWT represents the session).
     */
    @Column(name = "jti")
    private long jti;

    /**
     * Indicates if the token is valid (i.e not blacklisted)
     */
    @Column(name = "valid")
    private boolean valid;

    /* package */ Session() {
        // For Hibernate.
    }

    /**
     * Constructor.
     *
     * @param owner The {@link User} that owns this session.
     * @param jti   The JWT id (The JWT represents the session).
     * @throws NullPointerException If the {@code owner} is {@code null}.
     */
    public Session(User owner, long jti)
            throws ValidationException {
        Objects.requireNonNull(owner, "The session owner must not be null");
        this.owner = owner;
        this.jti = jti;
        this.valid = true;
    }

    /**
     * @return The user's id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The {@link User} that owns this session.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @return The JWT id (The JWT represents the session).
     */
    public long getJti() {
        return jti;
    }

    /**
     * @return {@code true} if it is a valid session, or {@code false} otherwise.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Makes this session invalid (i.e blacklists the JWT representing this session).
     */
    public void blacklist() {
        this.valid = false;
    }

    // ================================
    // equals, hashcode and toString
    // ================================

    /**
     * Equals based on the {@code id}.
     *
     * @param o The object to be compared with.
     * @return {@code true} if they are the equals, or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;

        Session session = (Session) o;

        return id == session.id;
    }

    /**
     * @return This user's hashcode, based on the {@code id}.
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Session: [ID: " + id + ']';
    }
}
