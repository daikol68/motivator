package de.daikol.motivator.model.user;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_USER")
public class User implements Serializable {

    @Id
    @SequenceGenerator(name = "USER_SEQUENCE_GENERATOR", sequenceName = "SEQ_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    @Column(name = "EMAIL", nullable = false)
    @XmlElement
    private String email;

    @Column(name = "NAME", nullable = false, unique = true)
    @XmlElement
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    @XmlElement
    private String password;

    @Column(name = "REGISTRATION", nullable = false)
    @XmlElement
    private boolean registration;

    @Column(name = "CODE", nullable = false)
    @XmlElement
    private String code;

    @Lob
    @Column(name = "PICTURE")
    @XmlElement
    private byte[] picture;

    @XmlElementWrapper
    // JPA ANNOTATIONS
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_FK", nullable = false)
    private List<Role> roles;


    public User() {
        this.roles = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles.clear();
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.registration = false;
        this.code = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(registration, user.registration)
                .append(email, user.email)
                .append(name, user.name)
                .append(password, user.password)
                .append(code, user.code)
                .append(picture, user.picture)
                .append(roles, user.roles)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(email)
                .append(name)
                .append(password)
                .append(registration)
                .append(code)
                .append(picture)
                .append(roles)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("email", email)
                .append("name", name)
                .append("password", "***")
                .append("registration", registration)
                .append("code", code)
                .append("picture", picture)
                .append("roles", roles)
                .toString();
    }
}