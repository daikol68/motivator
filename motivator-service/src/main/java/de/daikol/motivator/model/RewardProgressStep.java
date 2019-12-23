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
@Table(name = "TB_REWARD_PROGRESS")
public class RewardProgressStep implements Serializable {

    /**
     * The id of the progress step.
     */
    @Id
    @SequenceGenerator(name = "REWARD_PROGRESSSTEP_SEQUENCE_GENERATOR", sequenceName = "SEQ_REWARD_PROGRESSSTEP", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REWARD_PROGRESSSTEP_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

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

    public RewardProgressStep() {
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }


}