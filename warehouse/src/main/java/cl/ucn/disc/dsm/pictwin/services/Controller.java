package cl.ucn.disc.dsm.pictwin.services;

import cl.ucn.disc.dsm.pictwin.model.Persona;

import com.password4j.Password;

import io.ebean.Database;
import io.ebean.annotation.Transactional;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


/** The Controller. */
@Slf4j
public class Controller {

    /** The Database. */
    private final Database database;

    /** The Constructor. */
    public Controller(@NonNull final Database database) {
        this.database = database;
    }

    /** Register a new user. */
    @Transactional
    public Persona register(@NonNull final String email, @NonNull final String password) {

        // hash the password
        String hashedPassword = Password.hash(password).withBcrypt().getResult();
        log.debug("Hashed password: {}", hashedPassword);

        // build the Persona
        Persona persona =
                Persona.builder()
                        .email(email)
                        .password(hashedPassword)
                        .strikes(0)
                        .blocked(false)
                        .build();

        // save the Persona
        this.database.save(persona);
        log.debug("Persona saved: {}", persona);

        return persona;
    }
}
