package cl.ucn.disc.dsm.pictwin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.ebean.annotation.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

/** The Pic. */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@Builder
@Entity
public class Pic extends BaseModel {

    /** The Latitude. */
    @NotNull private Double latitude;

    /** The Longitude. */
    @NotNull private Double longitude;

    /** The number of Reports. */
    @Builder.Default @NotNull private Integer reports = 0;

    /** The Date. */
    @NotNull private Instant date;

    /** The Photo. */
    @NotNull @Lob private byte[] photo;

    /** The Blocked. */
    @Builder.Default @NotNull private Boolean blocked = Boolean.FALSE;

    /** The Number of views. */
    @Builder.Default @NotNull private Integer views = 0;

    /** The Persona relationship. */
    @JsonBackReference
    @ManyToOne(optional = false)
    private Persona persona;

    /** Setter for views. */
    public void setViews(Integer views) {
        if (views == null || views < 0) {
            throw new IllegalArgumentException("Views cannot be null or negative.");
        }
        this.views = views;
    }
}
