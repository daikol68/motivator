package de.daikol.motivator.model;

import de.daikol.motivator.model.user.User;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This class is used competition model news.
 */
//XML ANNOTATIONS
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
//JPA ANNOTATIONS
@Entity
@Table(name = "TB_MESSAGE")
public class Message implements Serializable, Cloneable, Comparable<Message> {

    /**
     * The id of the news.
     */
    @Id
    @SequenceGenerator(name = "NEWS_SEQUENCE_GENERATOR", sequenceName = "SEQ_NEWS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEWS_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private long id;

    /**
     * The date the news was created.
     */
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date creationDate;

    /**
     * A short description of the news.
     */
    @Column(name = "TEXT", nullable = false)
    @XmlElement
    private String text;

    /**
     * The user that created the news.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SENDER")
    private User sender;

    /**
     * The user that is assigned to the news.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECEIVER", nullable = false)
    private User receiver;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    @XmlElement
    private MessageType type;

    @Column(name = "READ", nullable = false)
    @XmlElement
    private boolean read;

    @Column(name = "DELETED", nullable = false)
    @XmlElement
    private boolean deleted;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }


    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
        this.read = false;
        this.deleted = false;
    }

    @Override
    public Message clone() {
        Message clone = new Message();
        clone.setCreationDate(creationDate);
        clone.setText(text);
        clone.setSender(sender);
        clone.setReceiver(receiver);
        clone.setType(type);
        clone.setRead(read);
        clone.setDeleted(deleted);
        return clone;
    }

    @Override
    public int compareTo(Message o) {
        return creationDate.compareTo(o.getCreationDate());
    }

    /**
     * This enumeration is used competition list all possible types of news.
     */
    public enum MessageType {
        CHAT,
        UPDATE;
    }

}
