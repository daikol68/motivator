package de.daikol.motivator.model;


import java.io.Serializable;

/**
 * This enumeration lists all possible status for a reward.
 */
public enum RewardStatus implements Serializable {
    /**
     * This status indicates that the reward has not been used yet.
     */
    OPEN,
    /**
     * This status indicates the reward has already been taken.
     */
    CLOSED
}