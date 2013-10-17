package be.vrt.web.restdc.bootstrap.modules;

import be.vrt.web.restdc.bootstrap.RestDcBootstrapper;
import org.reflections.vfs.Vfs;

/**
 * @author Mike Seghers
 */
public class JBossVFSModuleBootstrapper implements BootstrapperModule {


    @Override
    public void bootstrap(final RestDcBootstrapper bootstrapper) {
        Vfs.addDefaultURLTypes(new JBossUrlType());
    }
}
