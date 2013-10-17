package be.vrt.web.restdc.bootstrap;

import be.vrt.web.restdc.bootstrap.modules.BootstrapperModule;
import be.vrt.web.restdc.bootstrap.modules.JBossVFSModuleBootstrapper;
import be.vrt.web.restdc.bootstrap.modules.MockedBootstrapperModule;
import be.vrt.web.restdc.generator.DocumentSetGenerator;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.store.DocumentSetStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultRestDcBootstrapperTest {
    private DefaultRestDcBootstrapper bootstrapper;

    @Mock
    private DocumentSetStore documentSetStore;

    @Mock
    private DocumentSetGenerator documentSetGeneratorX;

    @Mock
    private DocumentSetGenerator documentSetGeneratorY;

    @Mock
    private BootstrapperModule moduleA;

    @Mock
    private BootstrapperModule moduleB;

    @Before
    public void setUp() throws Exception {
        List<DocumentSetGenerator> generators = new ArrayList<>();
        generators.add(documentSetGeneratorX);
        generators.add(documentSetGeneratorY);
        List<BootstrapperModule> modules = new ArrayList<>();
        modules.add(moduleA);
        modules.add(moduleB);
        bootstrapper = new DefaultRestDcBootstrapper(documentSetStore, generators, modules);
    }

    @Test
    public void testBootstrap() throws Exception {

        DocumentSet documentSetX = new DocumentSet.DocumentSetBuilder("x").build();
        DocumentSet documentSetY = new DocumentSet.DocumentSetBuilder("y").build();

        when(documentSetGeneratorX.generate()).thenReturn(documentSetX);
        when(documentSetGeneratorY.generate()).thenReturn(documentSetY);

        bootstrapper.bootstrap();

        verify(documentSetStore).storeDocumentSet(documentSetX);
        verify(documentSetStore).storeDocumentSet(documentSetY);
    }

    @Test
    public void test_bootstrapWithModule() throws Exception {
        bootstrapper.bootstrap();

        verify(moduleA).bootstrap(bootstrapper);
        verify(moduleB).bootstrap(bootstrapper);
    }

    @Test
    public void test_bootstrapWithAutodetectModules() throws Exception {
        List<BootstrapperModule> registeredModules = bootstrapper.getBootstrapperModules();

        bootstrapper.bootstrap();

        assertThat(registeredModules, hasSize(3));
        assertThat(registeredModules, hasItems(sameInstance(moduleA), sameInstance(moduleB), instanceOf(JBossVFSModuleBootstrapper.class)));
    }

    @Test
    public void test_ignoresNotFoundDefaultModules() throws Exception {
        BootstrapperModule mocked = mock(BootstrapperModule.class);
        bootstrapper.registerDefaultSubmodule("be.vrt.web.restdc.UnexistingClass", mocked.getClass());

        bootstrapper.bootstrap();

        List<BootstrapperModule> registeredModules = bootstrapper.getBootstrapperModules();
        assertThat(registeredModules, hasSize(3));
    }

    @Test
    public void test_includesFoundDefaultModules() throws Exception {
        bootstrapper.registerDefaultSubmodule(this.getClass().getName(), MockedBootstrapperModule.class);
        bootstrapper.bootstrap();

        List<BootstrapperModule> registeredModules = bootstrapper.getBootstrapperModules();
        assertThat(registeredModules, hasSize(4));

        MockedBootstrapperModule module = getMocked(registeredModules);
        assertThat(module.isCalled(), is(true));
    }

    private MockedBootstrapperModule getMocked(List<BootstrapperModule> registeredModules) {
        for (BootstrapperModule module : registeredModules) {
            if (module instanceof MockedBootstrapperModule) {
                return (MockedBootstrapperModule)module;
            }
        }
        return null;
    }
}
