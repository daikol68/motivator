package de.daikol.motivator.model;


import java.io.Serializable;

/**
 * This enumeration lists all possible apply status.
 */
public enum ApplyStatus implements Serializable {
    OPEN,
    CONFIRMED,
    REFUSED,
    CLOSED;
}