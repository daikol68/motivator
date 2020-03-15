package de.daikol.motivator.model;

import de.daikol.motivator.model.user.RoleType;
import de.daikol.motivator.model.user.User;

import javax.persistence.*;
import javax.xml.bind.annotation.*;


//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_COMPETITOR")
public class Competitor {

    @Id
    @SequenceGenerator(name = "COMPETITOR_SEQUENCE_GENERATOR", sequenceName = "SEQ_COMPETITOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPETITOR_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    @Column(name = "POINTS", nullable = false)
    @XmlElement
    private int points;

    @Column(name = "ROLE", nullable = false)
    @XmlElement
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "STATUS", nullable = false)
    @XmlElement
    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMPETITION_FK", nullable = false)
    private Competition competition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_FK", nullable = false)
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public CompetitionStatus getStatus() {
        return status;
    }

    public void setStatus(CompetitionStatus status) {
        this.status = status;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
