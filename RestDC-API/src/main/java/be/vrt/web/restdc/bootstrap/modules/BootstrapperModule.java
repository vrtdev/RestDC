package be.vrt.web.restdc.bootstrap.modules;

import be.vrt.web.restdc.bootstrap.RestDcBootstrapper;

/**
 * A bootstrapper module makes it possible to plug in custom bootstrapping logic.
 *
 * @author Mike Seghers
 */
public interface BootstrapperModule {
    void bootstrap(RestDcBootstrapper bootstrapper);
}
