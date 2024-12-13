package cl.ucn.disc.dsm.pictwin.web.routes;

import cl.ucn.disc.dsm.pictwin.model.PicTwin;
import cl.ucn.disc.dsm.pictwin.model.query.QPersona;
import cl.ucn.disc.dsm.pictwin.services.Controller;
import cl.ucn.disc.dsm.pictwin.model.Persona;
import cl.ucn.disc.dsm.pictwin.web.Route;

import io.javalin.http.Handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;

/** The Pic of Persona route. */
@Slf4j
public final class PersonaPic extends Route {

    /** The Constructor. */
    public PersonaPic(@NonNull final Controller controller) {
        super(Route.Method.POST, "/api/persona/{ulid}/pic");

        this.handler = buildHandler(controller);
    }

    /** Build the Handler. */
    private static Handler buildHandler(Controller controller) {
        return ctx -> {
            // the Persona ulid
            String ulid = ctx.pathParam("ulid");

            log.debug("Detected ulid={} for Persona.", ulid);

            // Validate the Persona exists
            Persona persona = new QPersona().ulid.equalTo(ulid).findOne();
            if (persona == null) {
                ctx.status(HttpStatus.NOT_FOUND_404);
                return;
            }

            // Get request parameters
            Double latitude;
            Double longitude;
            try {
                latitude = Double.valueOf(ctx.formParam("latitude"));
                longitude = Double.valueOf(ctx.formParam("longitude"));
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(HttpStatus.BAD_REQUEST_400);
                return;
            }

            // Get uploaded file from form
            var uploadedFiles = ctx.uploadedFiles("pic");
            if (uploadedFiles.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST_400);
                return;
            }

            // Use the first file uploaded
            var fileUpload = uploadedFiles.get(0);

            // Create temporary file to process
            File pic = File.createTempFile("uploaded_", ".img");
            try (var fos = new FileOutputStream(pic);
                 var inputStream = fileUpload.content()) {
                inputStream.transferTo(fos);
            }

            // Call the controller to add the photo
            PicTwin picTwin;
            try {
                picTwin = controller.addPic(
                        ulid,
                        latitude,
                        longitude,
                        pic
                );
            } catch (Exception e) {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
                pic.delete();
                return;
            }

            log.debug("Photo uploaded");
            ctx.status(HttpStatus.OK_200);

            // Delete temporary file
            pic.delete();
        };
    }
}
