package be.vrt.web.restdc.bootstrap;

/**
 *
 * @author Mike Seghers
 */
public interface RestDcBootstrapper {
    /**
     * Bootstraps REST DC, depending on the system configuration.
     */
    void bootstrap();
}
