package de.daikol.motivator.model;


import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class ProgressStepData implements Serializable {

    private long id;

    private Date creationDate;

    private long userid;

    private long competition;

    private String achievementName;

    private byte[] achievementPicture;

    private ApplyStatus applied;

    private Date appliedDate;

    private String appliedComment;

    public ProgressStepData() {
        // nothing to do
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getCompetition() {
        return competition;
    }

    public void setCompetition(long competition) {
        this.competition = competition;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public byte[] getAchievementPicture() {
        return achievementPicture;
    }

    public void setAchievementPicture(byte[] achievementPicture) {
        this.achievementPicture = achievementPicture;
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

}