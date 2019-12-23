package de.daikol.motivator.model;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used to model an achievement.
 */
//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_ACHIEVEMENT")
public class Achievement implements Serializable, Cloneable {

    /**
     * The id of the achievement.
     */
    @Id
    @SequenceGenerator(name = "ACHIEVEMENT_SEQUENCE_GENERATOR", sequenceName = "SEQ_ACHIEVEMENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACHIEVEMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    /**
     * The date the achievement was created.
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
     * A short description of the achievement.
     */
    @Column(name = "DESCRIPTION")
    @XmlElement
    private String description;

    /**
     * The picture used to symbolize the achievement.
     */
    @Column(name = "PICTURE")
    @Lob
    @XmlElement
    private byte[] picture;

    /**
     * The status of the achievement.
     */
    @Column(name = "STATUS", nullable = false)
    @XmlElement
    @Enumerated(EnumType.STRING)
    private AchievementStatus status;

    /**
     * The points the competition gains if the achievement gets closed.
     */
    @Column(name = "POINTS", nullable = false)
    @XmlElement
    private int points;

    /**
     * The points the competition gains if the achievement gets closed.
     */
    @Column(name = "REVIEW_TYPE", nullable = false)
    @XmlElement
    @Enumerated(EnumType.STRING)
    private AchievementReviewType type;

    /**
     * The number of progress steps the competitor has to take.
     */
    @Column(name = "PROGRESSSTEPS_FINISH", nullable = false)
    @XmlElement
    private int progressStepsFinish;

    /**
     * The progress for an achievement.
     */
    @XmlElementWrapper
    // JPA ANNOTATIONS
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ACHIEVEMENT_FK", nullable = false)
    private List<AchievementProgressStep> progressSteps;

    public Achievement() {
        progressSteps = new ArrayList<>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public AchievementStatus getStatus() {
        return status;
    }

    public void setStatus(AchievementStatus status) {
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public AchievementReviewType getType() {
        return type;
    }

    public void setType(AchievementReviewType type) {
        this.type = type;
    }

    public List<AchievementProgressStep> getProgressSteps() {
        return progressSteps;
    }

    public void setProgressSteps(List<AchievementProgressStep> progressSteps) {
        this.progressSteps.clear();
        if (progressSteps != null) {
            this.progressSteps.addAll(progressSteps);
        }
    }

    public int getProgressStepsFinish() {
        return progressStepsFinish;
    }

    public void setProgressStepsFinish(int progressStepsCount) {
        this.progressStepsFinish = progressStepsCount;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    @Override
    public Achievement clone() {
        Achievement clone = new Achievement();
        clone.setId(getId());
        clone.setCreationDate(getCreationDate());
        clone.setName(getName());
        clone.setDescription(getDescription());
        clone.setPoints(getPoints());
        clone.setPicture(getPicture());
        clone.setStatus(getStatus());
        clone.setProgressStepsFinish(getProgressStepsFinish());
        clone.setType(getType());
        return clone;
    }
}
