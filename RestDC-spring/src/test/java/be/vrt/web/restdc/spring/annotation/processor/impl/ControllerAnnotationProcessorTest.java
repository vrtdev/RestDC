package be.vrt.web.restdc.spring.annotation.processor.impl;

import be.vrt.web.restdc.annotation.processor.OverridingAnnotationProcessor;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.test.Dummy;
import be.vrt.web.restdc.test.controllers.TestController;
import be.vrt.web.restdc.test.controllers.TestControllerNoMapping;
import be.vrt.web.restdc.test.controllers.TestControllerOnlyMethodsHaveMapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerAnnotationProcessorTest {
    private ControllerAnnotationProcessor processor;

    @Mock
    private OverridingAnnotationProcessor<RequestMapping, ResourceDocument, Method, Class<?>> requestMappingAnnotationProcessor;

    @Before
    public void setUp() throws Exception {
        processor = new ControllerAnnotationProcessor(requestMappingAnnotationProcessor);
    }

    @Test
    public void testProcessOnTestController() throws Exception {
        Controller controller = TestController.class.getAnnotation(Controller.class);
        Method getDummyMethod = TestController.class.getMethod("getDummy", String.class, String.class, String.class);
        Method getDummiesMethod = TestController.class.getMethod("getDummies");
        Method saveDummyMethod = TestController.class.getMethod("saveDummy", Dummy.class);
        Method deleteMethod = TestController.class.getMethod("delete", String.class);
        Method mapMethod = TestController.class.getMethod("map");
        Method map2Method = TestController.class.getMethod("map2");
        Method map3Method = TestController.class.getMethod("map3");
        Method map4Method = TestController.class.getMethod("map4");
        Method mappingMethodEmptyMapping = TestController.class.getMethod("mappingMethodEmptyMapping");

        ResourceDocument resourceDocument = new ResourceDocument.ResourceDocumentBuilder().build();

        when(requestMappingAnnotationProcessor
                .process(Mockito.any(RequestMapping.class), Mockito.any(Method.class), Mockito
                        .any(RequestMapping.class), Matchers.eq(TestController.class))).thenReturn(resourceDocument);

        List<ResourceDocument> process = processor.process(controller, TestController.class);

        verify(requestMappingAnnotationProcessor)
                .process(getDummyMethod.getAnnotation(RequestMapping.class), getDummyMethod, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(getDummiesMethod.getAnnotation(RequestMapping.class), getDummiesMethod, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(saveDummyMethod.getAnnotation(RequestMapping.class), saveDummyMethod, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(deleteMethod.getAnnotation(RequestMapping.class), deleteMethod, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(mapMethod.getAnnotation(RequestMapping.class), mapMethod, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(map2Method.getAnnotation(RequestMapping.class), map2Method, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(map3Method.getAnnotation(RequestMapping.class), map3Method, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor)
                .process(map4Method.getAnnotation(RequestMapping.class), map4Method, TestController.class
                        .getAnnotation(RequestMapping.class), TestController.class);
        verify(requestMappingAnnotationProcessor).process(mappingMethodEmptyMapping
                .getAnnotation(RequestMapping.class), mappingMethodEmptyMapping, TestController.class
                .getAnnotation(RequestMapping.class), TestController.class);
        Mockito.verifyNoMoreInteractions(requestMappingAnnotationProcessor);

        assertThat(process, hasSize(9));
    }

    @Test
    public void testProcessOnControllerNoMapping() throws Throwable {
        Controller controller = TestControllerNoMapping.class.getAnnotation(Controller.class);

        List<ResourceDocument> process = processor.process(controller, TestControllerNoMapping.class);

        verifyZeroInteractions(requestMappingAnnotationProcessor);
        assertThat(process, is(nullValue()));
    }

    @Test
    public void testProcessOnControllerOnlyMethodsHaveMapping() throws Throwable {
        Controller controller = TestControllerOnlyMethodsHaveMapping.class.getAnnotation(Controller.class);
        Method method = TestControllerOnlyMethodsHaveMapping.class.getMethod("method");


        List<ResourceDocument> process = processor.process(controller, TestControllerOnlyMethodsHaveMapping.class);

        ResourceDocument resourceDocument = new ResourceDocument.ResourceDocumentBuilder().build();

        when(requestMappingAnnotationProcessor
                .process(Mockito.any(RequestMapping.class), Mockito.any(Method.class), Mockito
                        .any(RequestMapping.class), Matchers.eq(TestController.class))).thenReturn(resourceDocument);

        verify(requestMappingAnnotationProcessor)
                .process(method.getAnnotation(RequestMapping.class), method, TestControllerOnlyMethodsHaveMapping.class
                        .getAnnotation(RequestMapping.class), TestControllerOnlyMethodsHaveMapping.class);
        verifyNoMoreInteractions(requestMappingAnnotationProcessor);
        assertThat(process, hasSize(1));
    }
}
