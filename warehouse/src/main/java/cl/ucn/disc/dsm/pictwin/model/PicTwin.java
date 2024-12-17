package cl.ucn.disc.dsm.pictwin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.ebean.annotation.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

/** The PicTwin. */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@Builder
@Entity
public class PicTwin {

    /** The Expiration. */
    @NotNull private Instant expiration;

    /** The Expired. */
    @Builder.Default @NotNull private Boolean expired = Boolean.FALSE;

    /** The Reported. */
    @Builder.Default @NotNull private Boolean reported = Boolean.FALSE;

    /** The Persona relationship. */
    @JsonBackReference
    @ManyToOne(optional = false)
    private Persona persona;

    /** The Pic relationship. */
    @ToString.Exclude
    @ManyToOne(optional = false)
    private Pic pic;

    /** The Pic relationship. */
    @ToString.Exclude
    @ManyToOne(optional = false)
    private Pic twin;
}
