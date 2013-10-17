package be.vrt.web.restdc.bootstrap;

import be.vrt.web.restdc.bootstrap.modules.BootstrapperModule;
import be.vrt.web.restdc.bootstrap.modules.JBossVFSModuleBootstrapper;
import be.vrt.web.restdc.generator.DocumentSetGenerator;
import be.vrt.web.restdc.store.DocumentSetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default REST DC bootstrapper implementation. It loads {@link be.vrt.web.restdc.domain.DocumentSet}s
 * from the configured list of {@link be.vrt.web.restdc.generator.DocumentSetGenerator}s and stores them in the configured {@link
 * be.vrt.web.restdc.store.DocumentSetStore}.
 * <p/>
 * When you need to configure extras to be able to bootstrap, you can also register
 *
 * @author Mike Seghers
 */
public class DefaultRestDcBootstrapper implements RestDcBootstrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRestDcBootstrapper.class);

    private DocumentSetStore documentSetStore;

    private List<DocumentSetGenerator> documentSetGenerators;
    private List<BootstrapperModule> bootstrapperModules;

    private Map<String, Class<? extends BootstrapperModule>> defaultBootstrapperModuleRegistry;

    /**
     * Initializes a bootstrapper with a DocumentSetStore and a list of DocumentSetGenerators.
     *
     * @param documentSetStore the DocumentSet store
     * @param generators       the DocumentSet generator
     * @param modules          the Bootstrapper modules
     */
    public DefaultRestDcBootstrapper(final DocumentSetStore documentSetStore, final List<DocumentSetGenerator> generators, final List<BootstrapperModule> modules) {
        this.documentSetStore = documentSetStore;
        this.documentSetGenerators = Collections.unmodifiableList(generators);
        this.bootstrapperModules = new ArrayList<>(modules);
        registerDefaultBootstrapperModules();
    }

    private void registerDefaultBootstrapperModules() {
        defaultBootstrapperModuleRegistry = new HashMap<>(); defaultBootstrapperModuleRegistry.put("org.jboss.vfs.VirtualFile", JBossVFSModuleBootstrapper.class);
    }

    @Override
    public void bootstrap() {
        loadAvailableBootstrapperModules();

        for (BootstrapperModule module : bootstrapperModules) {
            module.bootstrap(this);
        }

        for (DocumentSetGenerator generator : documentSetGenerators) {
            documentSetStore.storeDocumentSet(generator.generate());
        }
    }

    private void loadAvailableBootstrapperModules() {
        for (Map.Entry<String, Class<? extends BootstrapperModule>> entry : defaultBootstrapperModuleRegistry.entrySet()) {
            try {
                Class.forName(entry.getKey());
                try {
                    bootstrapperModules.add(entry.getValue().newInstance());
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("The registered BootstrapperModule " + entry.getValue() + " for " + entry
                            .getKey() + " could not be initialized. Does it have a default constructor? If it has, check if it throws an exception.", e);
                }
                LOGGER.info("JBoss Virtual File System found on class path, adding JBossVFSModuleBootstrapper as module.");
            } catch (ClassNotFoundException e) {
                LOGGER.info("JBoss Virtual File System not found on class path, not adding JBossVFSModuleBootstrapper as module.");
            }
        }
    }

    List<BootstrapperModule> getBootstrapperModules() {
        return Collections.unmodifiableList(bootstrapperModules);
    }


    public void registerDefaultSubmodule(String classToBind, Class<? extends BootstrapperModule> bootstrapperModuleClass) {
        defaultBootstrapperModuleRegistry.put(classToBind, bootstrapperModuleClass);
    }
}
