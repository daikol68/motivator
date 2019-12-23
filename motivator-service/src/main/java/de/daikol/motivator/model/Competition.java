package de.daikol.motivator.model;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used to model a competition.
 */
//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_COMPETITION")
public class Competition implements Serializable, Comparable<Competition> {

    /**
     * The id of the competition.
     */
    @Id
    @SequenceGenerator(name = "COMPETITION_SEQUENCE_GENERATOR", sequenceName = "SEQ_COMPETITION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPETITION_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    /**
     * The date the competition was created.
     */
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date creationDate;

    /**
     * The name of the achievement.
     */
    @Column(name = "NAME", nullable = false)
    @XmlElement
    private String name;

    /**
     * The picture for the user.
     */
    @Lob
    @Column(name = "PICTURE")
    @XmlElement
    private byte[] picture;

    /**
     * The list of competitors that belong to the competition.
     */
    @XmlElementWrapper
    // JPA ANNOTATIONS
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "competition")
    private List<Competitor> competitors;

    /**
     * The list of achievements that belong to the competition.
     */
    @XmlElementWrapper
    // JPA ANNOTATIONS
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "COMPETITION_FK", nullable = false)
    private List<Achievement> achievements;

    /**
     * The rewards that belong to the competition.
     */
    @XmlElementWrapper
    // JPA ANNOTATIONS
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "COMPETITION_FK", nullable = false)
    private List<Reward> rewards;

    public Competition() {
        this.competitors = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.rewards = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors.clear();
        if (competitors != null) {
            this.competitors.addAll(competitors);
        }
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements.clear();
        if (achievements != null) {
            this.achievements.addAll(achievements);
        }
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards.clear();
        if (rewards != null) {
            this.rewards.addAll(rewards);
        }
    }

    @PrePersist
    protected void onCreate() {
        final Date now = new Date();
        this.creationDate = now;
    }

    @Override
    public int compareTo(Competition o) {
        return creationDate.compareTo(o.creationDate);
    }
}