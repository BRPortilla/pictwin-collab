package cl.ucn.disc.dsm.pictwin;

import cl.ucn.disc.dsm.pictwin.model.Persona;
import cl.ucn.disc.dsm.pictwin.model.PicTwin;
import cl.ucn.disc.dsm.pictwin.services.Controller;
import cl.ucn.disc.dsm.pictwin.utils.FileUtils;

import io.ebean.DB;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/** The Main. */
@Slf4j
public class TheMain {

    /** Starting point. */
    public static void main(String[] args) {

        log.debug("Starting TheMain ..");

        // the controller
        Controller c = new Controller(DB.getDefault());

        // seed the database
        if (c.seed()){
            log.debug("Seeded the database.");
        }

        //FIXME: when trying to logging-in with this account, it says it doesn't exist.
        log.debug("Registering Persona ..");
        Persona p = c.register("bairon.rojas@alumnos.ucn.cl","brojas123");
        log.debug("Persona: {}", p);

        //File is obtainable from the resources folder. There is the image with the name "default.jpg".
        File file = FileUtils.getResourceFile("default.jpg");
        log.debug("File: {}", file);

        PicTwin pt = c.addPic(p.getUlid(), -23.6509, -70.9975, file);
        log.debug("PicTwin: {}", pt);

        Persona p2 = c.login("bairon.rojas@alumnos.ucn.cl", "brojas123");
        log.debug("Persona: {}", p2);

        List<PicTwin> pts = c.getPicTwins(p.getUlid());
        for (PicTwin ptt: pts){
            log.debug("PicTwin: {}", ptt);
        }

        log.debug("Done.");
    }
}
