package de.daikol.motivator.model;


import java.io.Serializable;

/**
 * This enumeration lists all status for an achievement.
 */
public enum AchievementStatus implements Serializable {
    /**
     * Indicates the achievement is not finished yet.
     */
    OPEN,
    /**
     * Indicates the achievement has been already fulfilled.
     */
    CLOSED
}