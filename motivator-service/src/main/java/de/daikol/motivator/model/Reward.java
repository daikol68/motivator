package de.daikol.motivator.model;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used to model a reward.
 */
//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_REWARD")
public class Reward implements Serializable, Cloneable {

    /**
     * The id of the reward.
     */
    @Id
    @SequenceGenerator(name = "REWARD_SEQUENCE_GENERATOR", sequenceName = "SEQ_REWARD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REWARD_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    /**
     * The date the reward was created.
     */
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date creationDate;

    /**
     * The name of the reward.
     */
    @Column(name = "NAME", nullable = false)
    @XmlElement
    private String name;

    /**
     * A short description of the reward.
     */
    @Column(name = "DESCRIPTION")
    @XmlElement
    private String description;

    /**
     * The picture used to symbolize the reward.
     */
    @Column(name = "PICTURE")
    @Lob
    @XmlElement
    private byte[] picture;

    /**
     * The points the competition looses if the reward is taken.
     */
    @Column(name = "POINTS", nullable = false)
    @XmlElement
    private int points;

    /**
     * The status of the reward.
     */
    @Column(name = "STATUS", nullable = false)
    @XmlElement
    @Enumerated(EnumType.STRING)
    private RewardStatus status;

    /**
     * The progress for an achievement.
     */
    @XmlElementWrapper
    @XmlElement(name = "progress", type = RewardProgressStep.class)
    // JPA ANNOTATIONS
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "REWARD_FK", nullable = false)
    private List<RewardProgressStep> progressSteps;

    public Reward() {
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public RewardStatus getStatus() {
        return status;
    }

    public void setStatus(RewardStatus status) {
        this.status = status;
    }

    public List<RewardProgressStep> getProgressSteps() {
        return progressSteps;
    }

    public void setProgressSteps(List<RewardProgressStep> progressSteps) {
        this.progressSteps.clear();
        if (progressSteps != null) {
            this.progressSteps.addAll(progressSteps);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    @Override
    public Reward clone() {
        Reward clone = new Reward();
        clone.setId(getId());
        clone.setCreationDate(getCreationDate());
        clone.setName(getName());
        clone.setDescription(getDescription());
        clone.setPoints(getPoints());
        clone.setPicture(getPicture());
        clone.setStatus(getStatus());
        return clone;
    }

}