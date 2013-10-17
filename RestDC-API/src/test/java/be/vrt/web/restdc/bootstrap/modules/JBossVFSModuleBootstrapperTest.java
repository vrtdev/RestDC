package be.vrt.web.restdc.bootstrap.modules;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reflections.vfs.Vfs;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
public class JBossVFSModuleBootstrapperTest {

    private JBossVFSModuleBootstrapper bootstrapper;

    @Test
    public void test_bootstrap() throws Exception {
        bootstrapper = new JBossVFSModuleBootstrapper();
        bootstrapper.bootstrap(null);
        assertThat(Vfs.getDefaultUrlTypes(), hasItem(CoreMatchers.<JBossUrlType>instanceOf(JBossUrlType.class)));
    }
}
