package be.vrt.web.restdc.bootstrap.modules;

import org.jboss.vfs.VFS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reflections.vfs.Vfs;

import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
public class JBossUrlTypeTest {
    private JBossUrlType type;

    @Before
    public void setUp() throws Exception {
       System.setProperty("java.protocol.handler.pkgs", "org.jboss.vfs.protocol");
        type = new JBossUrlType();
    }

    @Test
    public void test_matches_vfs() throws Exception {
        boolean matches = type.matches(new URL("vfs:/VFSURL"));
        assertThat(matches, is(true));
    }

    @Test
    public void test_doesNotMatch_vfs() throws Exception {
        boolean matches = type.matches(new URI("http://www.invalid.com").toURL());
        assertThat(matches, is(false));
    }

    @Test
    public void test_createDir() throws Exception {
        URL realFileURL = this.getClass().getResource("/");
        URL vfsURL = new URL("vfs", null, realFileURL.getFile());

        Vfs.Dir dir = type.createDir(vfsURL);
        assertThat(dir, is(notNullValue()));
    }

    @Test
    public void test_createDir_notADir() throws Exception {
        URL realFileURL = this.getClass().getResource("/some.txt");
        URL vfsURL = new URL("vfs", null, realFileURL.getFile());

        Vfs.Dir dir = type.createDir(vfsURL);
        assertThat(dir, is(nullValue()));
    }

    @Test
    public void test_createDir_doesNotExist() throws Exception {
        URL vfsURL = new URL("vfs:/this/folder/will/not/exist/on/your/file/system");

        Vfs.Dir dir = type.createDir(vfsURL);
        assertThat(dir, is(nullValue()));
    }
}
