package cl.ucn.disc.dsm.pictwin;

import cl.ucn.disc.dsm.pictwin.services.Controller;
import cl.ucn.disc.dsm.pictwin.web.Route;
import cl.ucn.disc.dsm.pictwin.web.routes.Home;
import cl.ucn.disc.dsm.pictwin.web.routes.PersonaLogin;
import cl.ucn.disc.dsm.pictwin.web.routes.PersonaPic;
import cl.ucn.disc.dsm.pictwin.web.routes.PersonaPicTwins;

import io.ebean.DB;
import io.javalin.Javalin;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/** The Server. */
@Slf4j
public class TheServer {

    /** The Javalin web server. */
    private static Javalin createJavalin() {

        // create and configure the Javalin web server
        return Javalin.create(
                config -> {
                    // enable the extensive logging
                    config.requestLogger.http(
                            (ctx, ms) -> {
                                log.debug("Served {} in {} ms", ctx.fullUrl(), ms);
                            });

                    // enable the compression
                    config.http.gzipOnlyCompression(9);

                    // graceful shutdown
                    config.jetty.modifyServer(server -> server.setStopTimeout(5_000));
                });
    }

    /** Add the Routes of Javalin. */
    private static void addRoute(final @NonNull Route route, final @NonNull Javalin javalin){

        log.debug(
                "Adding route {} with verb {} in path: {}",
                route.getClass().getSimpleName(),
                route.getMethod(),
                route.getPath());

        switch (route.getMethod()){
            case GET:
                javalin.get(route.getPath(), route.getHandler());
                break;
            case POST:
                javalin.post(route.getPath(), route.getHandler());
                break;
            case PUT:
                javalin.put(route.getPath(), route.getHandler());
                break;
            default:
                throw new IllegalArgumentException("Method not supported:" + route.getMethod());
        }
    }

    public static void main(String[] args){

        log.debug("Configuring controller...");
        Controller controller = new Controller(DB.getDefault());

        if(controller.seed()) {
            log.debug("Database seeded.");
        }

        log.debug("Configure TheServer...");

        Javalin javalin = createJavalin();

        log.debug("Adding routes...");

        //GET -> /
        addRoute(new Home(), javalin);

        //GET -> /api/personas/{ulid}/pic
        addRoute(new PersonaPicTwins(controller), javalin);

        //TODO: IMPLEMENTAR LAS RUTAS.

        //POST -> /api/personas
        addRoute(new PersonaLogin(controller), javalin);

        //POST -> /api/personas/{ulid}/pic
        addRoute(new PersonaPic(controller), javalin);

        //shutdown latch
        CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    javalin.stop();

                                    DB.getDefault().shutdown();

                                    latch.countDown();
                                }));

        log.debug("Starting the server...");
        javalin.start(7000);

        try {
            latch.await();
        } catch (InterruptedException e){
            log.debug("Server shutdown interrupted", e);
            Thread.currentThread().interrupt();
        }

        log.debug("Done.");
    }
}
