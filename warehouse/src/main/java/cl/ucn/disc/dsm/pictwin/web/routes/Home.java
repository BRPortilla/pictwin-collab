package cl.ucn.disc.dsm.pictwin.web.routes;

import cl.ucn.disc.dsm.pictwin.web.Route;

/** The Home route. */
public final class Home extends Route{

    /** The Constructor. */
    public Home(){
        super(Method.GET,"/");

        this.handler = ctx -> {
            ctx.result("Welcome to PicTwin");
        };

    }
}
