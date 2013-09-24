package be.vrt.web.restdc.bootstrap;

/**
 * A bootstrapper launches REST DC's document generator.
 *
 * @author Mike Seghers
 */
public interface RestDcBootstrapper {
    /**
     * Bootstraps REST DC, depending on the system configuration.
     */
    void bootstrap();
}
