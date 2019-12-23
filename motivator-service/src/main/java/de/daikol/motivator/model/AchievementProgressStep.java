package de.daikol.motivator.model;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This class is used to model the progress.
 */
//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_ACHIEVEMENT_PROGRESS")
public class AchievementProgressStep implements Serializable {

    /**
     * The id of the progress step.
     */
    @Id
    @SequenceGenerator(name = "ACHIEVEMENT_PROGRESSSTEP_SEQUENCE_GENERATOR", sequenceName = "SEQ_ACHIEVEMENT_PROGRESSSTEP", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACHIEVEMENT_PROGRESSSTEP_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    /**
     * The date the progress was taken.
     */
    @Column(name = "ACHIEVEMENT_ID", nullable = false)
    @XmlElement
    private long achievementId;

    /**
     * The date the progress was taken.
     */
    @Column(name = "USER_ID", nullable = false)
    @XmlElement
    private long userId;

    /**
     * The date the progress was taken.
     */
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date creationDate;

    /**
     * Indicates whether the reporter applied the progress or not.
     */
    @Column(name = "APPLIED", nullable = false)
    @XmlElement
    @Enumerated(EnumType.STRING)
    private ApplyStatus applied;

    /**
     * The date the progress step was applied.
     */
    @Column(name = "APPLIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date appliedDate;

    /**
     * The comment to the apply status.
     */
    @Column(name = "APPLIED_COMMENT")
    @XmlElement
    private String appliedComment;

    public AchievementProgressStep() {
        // nothing to do
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(long achievementId) {
        this.achievementId = achievementId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ApplyStatus getApplied() {
        return applied;
    }

    public void setApplied(ApplyStatus applied) {
        this.applied = applied;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Date appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getAppliedComment() {
        return appliedComment;
    }

    public void setAppliedComment(String appliedComment) {
        this.appliedComment = appliedComment;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
        this.applied = ApplyStatus.OPEN;
    }


}