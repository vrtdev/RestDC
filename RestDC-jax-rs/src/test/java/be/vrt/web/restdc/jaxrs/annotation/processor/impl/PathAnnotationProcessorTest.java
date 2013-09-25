package be.vrt.web.restdc.jaxrs.annotation.processor.impl;

import be.vrt.web.restdc.annotation.processor.OverridingAnnotationProcessor;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.test.resources.EmptyResource;
import be.vrt.web.restdc.test.resources.TestResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class PathAnnotationProcessorTest {
    private PathAnnotationProcessor pap;

    @Mock
    private OverridingAnnotationProcessor<Path,ResourceDocument,Method,Class<?>> overridingProcessor;

    @Before
    public void setUp() throws Exception {
        pap = new PathAnnotationProcessor(overridingProcessor);
    }

    @Test
    public void test_processWithTestResource() throws Exception {
        Path path = TestResource.class.getAnnotation(Path.class);
        List<ResourceDocument> rds = pap.process(path, TestResource.class);
        assertThat(rds, is(notNullValue()));
        verify(overridingProcessor, times(10)).process(Mockito.any(Path.class), Mockito.any(Method.class), eq(path), eq(TestResource.class));
    }

    @Test
    public void test_processWithEmptyResource() throws Exception {
        Path path = EmptyResource.class.getAnnotation(Path.class);
        List<ResourceDocument> rds = pap.process(path, EmptyResource.class);
        assertThat(rds, is(nullValue()));
        verifyZeroInteractions(overridingProcessor);
    }
}
