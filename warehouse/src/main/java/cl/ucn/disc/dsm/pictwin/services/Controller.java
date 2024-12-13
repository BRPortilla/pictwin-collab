package cl.ucn.disc.dsm.pictwin.services;

import cl.ucn.disc.dsm.pictwin.model.Persona;
import cl.ucn.disc.dsm.pictwin.model.query.QPersona;

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

    /** The seed of the database. */
    public Boolean seed() {

        // find the Persona size
        int personaSize = new QPersona().findCount();
        log.debug("Personas in database: {}", personaSize);

        // if the Persona exist -> don't seed!
        if (personaSize != 0){
            return Boolean.FALSE;
        }

        log.debug("Can't find data, seeding the database ..");

        // seed the Persona
        Persona persona = this.register("durrutia.ucn.cl", "durrutia123");
        log.debug("Persona registered: {}", persona);

        log.debug("Database seeded.");

        return Boolean.TRUE;
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

    /** Login a user. */
    public Persona login(@NonNull final String email, @NonNull final String password) {

        // find the Persona
        Persona persona = new QPersona().email.equalTo(email).findOne();
        if (persona == null) {
            throw new RuntimeException("User not found");
        }

        // check the password
        if (!Password.check(password, persona.getPassword()).withBcrypt()) {
            throw new RuntimeException("Wrong password");
        }

        return persona;
    }
}
