package be.vrt.web.restdc.bootstrap.modules;

import org.jboss.vfs.VirtualFile;
import org.reflections.ReflectionsException;
import org.reflections.vfs.SystemDir;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.ZipDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * @author Mike Seghers
 */
class JBossUrlType implements Vfs.UrlType {

    private static final Logger LOGGER = LoggerFactory.getLogger(JBossUrlType.class);

    public boolean matches(URL url) {
        return url.getProtocol().equals("vfs");
    }

    public Vfs.Dir createDir(URL url) {
        VirtualFile content;
        try {
            content = (VirtualFile) url.openConnection().getContent();
        } catch (Throwable e) {
            throw new ReflectionsException("could not open url connection as VirtualFile [" + url + "]", e);
        }

        Vfs.Dir dir = null;
        try {
            dir = createDir(new java.io.File(content.getPhysicalFile().getParentFile(), content.getName()));
        } catch (IOException e) {
            LOGGER.warn("Ignoring IOException in DIR creation for VFS, via URL " + url, e);
        }
        if (dir == null) {
            try {
                dir = createDir(content.getPhysicalFile());
            } catch (IOException e) {
                LOGGER.warn("Ignoring IOException in DIR creation for VFS, via URL " + url, e);
            }
        }
        return dir;
    }

    Vfs.Dir createDir(java.io.File file) {
        try {
            return file.exists() && file.canRead() ? file.isDirectory() ? new SystemDir(file) : new ZipDir(new JarFile(file)) : null;
        } catch (IOException e) {
            LOGGER.warn("Ignoring IOException in DIR creation for VFS, via File " + file, e);
        }
        return null;
    }
}