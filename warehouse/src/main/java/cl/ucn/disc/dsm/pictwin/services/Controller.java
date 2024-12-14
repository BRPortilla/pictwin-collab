package cl.ucn.disc.dsm.pictwin.services;

import cl.ucn.disc.dsm.pictwin.model.Persona;
import cl.ucn.disc.dsm.pictwin.model.Pic;
import cl.ucn.disc.dsm.pictwin.model.PicTwin;
import cl.ucn.disc.dsm.pictwin.model.query.QPersona;
import cl.ucn.disc.dsm.pictwin.model.query.QPic;
import cl.ucn.disc.dsm.pictwin.model.query.QPicTwin;
import cl.ucn.disc.dsm.pictwin.utils.FileUtils;

import com.password4j.Password;

import io.ebean.Database;
import io.ebean.annotation.Transactional;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.Instant;
import java.util.List;


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

        Persona persona2 = this.register("benjamin.rivera01@alumnos.ucn.cl", "brivera123");
        log.debug("Persona registered: {}", persona2);


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

    /** Add a new Pic. */
    @Transactional
    public PicTwin addPic(
            @NonNull String ulidPersona,
            @NonNull Double latitude,
            @NonNull Double longitude,
            @NonNull File picture) {

        // read the file
        byte[] data = FileUtils.readAllBytes(picture);

        // find the Persona
        Persona persona = new QPersona().ulid.equalTo(ulidPersona).findOne();
        log.debug("Persona found: {}", persona);

        Pic twin = new QPic()
                .persona.ulid.notEqualTo(ulidPersona)
                .blocked.equalTo(false)
                .orderBy("views asc")
                .setMaxRows(1)
                .findOne();

        if (twin == null) {

            // Si no hay fotos disponibles, usa la foto por defecto
            File file = FileUtils.getResourceFile("default.jpg");

            // Crea un Twin usando la foto predeterminada
            twin = Pic.builder()
                    .latitude(-23.6500)
                    .longitude(-70.4000)
                    .photo(FileUtils.readAllBytes(file))
                    .date(Instant.now())
                    .persona(persona)
                    .views(0)
                    .blocked(false)
                    .reports(0)
                    .build();
        }

        log.debug("Twin to save: {}", twin);
        twin.setViews(twin.getViews() + 1);
        this.database.save(twin);

        // save the Pic
        Pic pic =
                Pic.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .reports(0)
                        .date(Instant.now())
                        .photo(data)
                        .blocked(false)
                        .views(0)
                        .persona(persona)
                        .build();
        log.debug("Pic to save: {}", pic);
        this.database.save(pic);

        // save the PicTwin
        PicTwin picTwin =
                PicTwin.builder()
                        .expiration(Instant.now().plusSeconds(7*24*60*60))
                        .expired(false)
                        .reported(false)
                        .persona(persona)
                        .pic(pic)
                        .twin(twin)
                        .build();
        log.debug("PicTwin to save: {}", picTwin);
        this.database.save(picTwin);

        return picTwin;
    }

    /** Get the PicTwins. */
    public List<PicTwin> getPicTwins(@NonNull String ulidPersona) {
        return new QPicTwin().persona.ulid.equalTo(ulidPersona).findList();
    }
}
