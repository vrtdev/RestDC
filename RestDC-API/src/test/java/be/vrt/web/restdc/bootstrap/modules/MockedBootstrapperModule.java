package be.vrt.web.restdc.bootstrap.modules;

import be.vrt.web.restdc.bootstrap.RestDcBootstrapper;

/**
 * @author Mike Seghers
 */
public class MockedBootstrapperModule implements BootstrapperModule {
    private boolean called;

    public boolean isCalled() {
        return called;
    }

    @Override
    public void bootstrap(RestDcBootstrapper bootstrapper) {
        called = true;
    }
}
