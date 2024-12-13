package cl.ucn.disc.dsm.pictwin.web.routes;

import cl.ucn.disc.dsm.pictwin.services.Controller;
import cl.ucn.disc.dsm.pictwin.web.Route;

import io.javalin.http.Handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/** The PicTwins of Persona route. */
@Slf4j
public final class PersonaPicTwins extends Route {

    /** The Constructor. */
    public PersonaPicTwins(@NonNull final Controller controller) {
        super(Route.Method.GET, "api/personas/{ulid}/pictwins");

        this.handler = buildHandler(controller);
    }

    /** Build the handler. */
    private static Handler buildHandler(Controller controller){
        return ctx -> {
            String ulid = ctx.pathParam("ulid");
            log.debug("Detected ulid={} for Persona.", ulid);

            ctx.json(controller.getPicTwins(ulid));
        };
    }
}
