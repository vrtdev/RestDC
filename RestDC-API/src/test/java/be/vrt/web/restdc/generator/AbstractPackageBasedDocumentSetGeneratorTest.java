package be.vrt.web.restdc.generator;

import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.generator.impl.AbstractPackageBasedDocumentSetGenerator;
import be.vrt.web.restdc.test.SimpleAnnotatedClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractPackageBasedDocumentSetGeneratorTest {

    public @interface Dummy {

    }

    @Mock
    private AnnotationProcessor<Dummy, List<ResourceDocument>, Class<?>> processor;

    private AbstractPackageBasedDocumentSetGenerator<Dummy> generator;

    @Before
    public void setUp() throws Exception {
        generator = new AbstractPackageBasedDocumentSetGenerator<Dummy>("be.vrt.web.restdc.test", Dummy.class, processor) {
            @Override
            protected String getIdPrefix() {
                return "dummy";
            }
        };
    }

    @Test
    public void test_generate() throws Exception {
        DocumentSet documentSet = generator.generate();
        assertThat(documentSet.getId(), is("dummy in package be.vrt.web.restdc.test"));
        verify(processor).process(Mockito.any(Dummy.class), Mockito.eq(SimpleAnnotatedClass.class));
    }

    @Test
    public void test_generate_processorReturnsResourceDocument() throws Exception {
        ResourceDocument resourceDocument = new ResourceDocument.ResourceDocumentBuilder().build();
        when(processor.process(Mockito.any(Dummy.class), Mockito.eq(SimpleAnnotatedClass.class))).thenReturn(Arrays.asList(resourceDocument));
        DocumentSet documentSet = generator.generate();
        assertThat(documentSet.getId(), is("dummy in package be.vrt.web.restdc.test"));
        assertThat(documentSet.getDocuments(), hasSize(1));
    }


}
