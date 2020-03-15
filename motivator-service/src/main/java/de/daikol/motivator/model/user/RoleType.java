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
public enum RoleType {
    /**
     * The admin user.
     */
    ADMIN,
    /**
     * The user.
     */
    USER;
}