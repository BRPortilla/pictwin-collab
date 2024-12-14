package cl.ucn.disc.dsm.pictwin.web.routes;

import cl.ucn.disc.dsm.pictwin.services.Controller;
import cl.ucn.disc.dsm.pictwin.web.Route;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PersonaLogin extends Route {

    /** El constructor. */
    public PersonaLogin(Controller controller){
        super(Method.POST, "/api/personas");

        this.handler = buildHandler(controller);
    }

    private Handler buildHandler(Controller controller) {
        return ctx -> {
            
        };
    }

}
