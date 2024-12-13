package cl.ucn.disc.dsm.pictwin.model;

import io.ebean.annotation.Index;
import io.ebean.annotation.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

/** The Persona. */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@Builder
@Entity
public class Persona extends BaseModel {

    /** The email. */
    @NotNull
    @Index(unique = true)
    private String email;

    /** The password. */
    @NotNull
    @Column(length = 72)
    private String password;

    /** The number of strikes. */
    @Builder.Default @NotNull private Integer strikes = 0;

    /** The blocked. */
    @Builder.Default @NotNull private Boolean blocked = Boolean.FALSE;

    /** The blocked date. */
    private Instant blockedAt;

    /** The list of pics */
    @ToString.Exclude
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    private List<Pic> pics;

    /** The list of PicTwins. */
    @ToString.Exclude
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    private List<PicTwin> picTwins;
}
