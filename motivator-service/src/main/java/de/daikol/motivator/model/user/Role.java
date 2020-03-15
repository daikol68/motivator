package de.daikol.motivator.model.user;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * This class is used to model the user.
 */
//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_ROLE")
public class Role implements Serializable {

    /**
     * The id of the achievement.
     */
    @Id
    @SequenceGenerator(name = "ROLE_SEQUENCE_GENERATOR", sequenceName = "SEQ_ROLE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    /**
     * The username.
     */
    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    @XmlElement
    private RoleType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}
