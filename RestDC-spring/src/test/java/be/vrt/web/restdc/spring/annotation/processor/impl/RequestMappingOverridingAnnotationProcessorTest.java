package be.vrt.web.restdc.spring.annotation.processor.impl;

import be.vrt.web.restdc.domain.MimeType;
import be.vrt.web.restdc.domain.Parameter;
import be.vrt.web.restdc.domain.ParameterLocation;
import be.vrt.web.restdc.domain.RequestMethod;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.domain.Type;
import be.vrt.web.restdc.test.Dummy;
import be.vrt.web.restdc.test.controllers.TestController;
import be.vrt.web.restdc.test.controllers.TestControllerWithMime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestMappingOverridingAnnotationProcessorTest {
    private RequestMappingOverridingAnnotationProcessor processor;

    @Mock
    private ParameterNameDiscoverer parameterNameDiscoverer;

    @Before
    public void setUp() throws Exception {
        processor = new RequestMappingOverridingAnnotationProcessor(parameterNameDiscoverer);
    }

    @Test
    public void testProcessTestControllerMap() throws Exception {
        Method method = TestController.class.getMethod("map");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verifyZeroInteractions(parameterNameDiscoverer);

        assertThat(document.getUrl(), is("/test/dummy/map"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("Map").withGenericTypeNames(new String[]{"T extends Dummy", "super Dummy"}).build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerMap2() throws Exception {
        Method method = TestController.class.getMethod("map2");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verifyZeroInteractions(parameterNameDiscoverer);

        assertThat(document.getUrl(), is("/test/dummy/map2"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("Map").withGenericTypeNames("X", "Y extends Dummy").build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerMap3() throws Exception {
        Method method = TestController.class.getMethod("map3");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test/dummy/map3"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("Map").withGenericTypeNames(new String[]{"T extends X", "V extends Y extends Dummy"}).build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerMap4() throws Exception {
        Method method = TestController.class.getMethod("map4");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test/dummy/map4"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("Map").withGenericTypeNames(new String[]{"extends X", "super Y extends Dummy"}).build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerDelete() throws Exception {
        Method method = TestController.class.getMethod("delete", String.class);

        when(parameterNameDiscoverer.getParameterNames(method)).thenReturn(null);
        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verify(parameterNameDiscoverer).getParameterNames(method);
        verifyNoMoreInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test/dummy/{pathVar}"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.DELETE));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("void").build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        List<Parameter> parameters = document.getParameters();
        assertThat(parameters, hasSize(1));
        assertThat(parameters.get(0),
                   is(new Parameter.ParameterBuilder(new Type.TypeBuilder("String").build()).withName(null).withDescription(null).withParameterLocation(ParameterLocation.PATH)
                                                                                            .isRequired(true).build()));
    }

    @Test
    public void testProcessTestControllerSave() throws Exception {
        Method method = TestController.class.getMethod("saveDummy", Dummy.class);

        when(parameterNameDiscoverer.getParameterNames(method)).thenReturn(new String[]{"dummy"});
        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verify(parameterNameDiscoverer).getParameterNames(method);
        verifyNoMoreInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test/dummy"));
        assertThat(document.getRequestMethods(), hasSize(2));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.PUT, RequestMethod.POST));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("void").build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        List<Parameter> parameters = document.getParameters();
        assertThat(parameters, hasSize(1));
        assertThat(parameters.get(0),
                   is(new Parameter.ParameterBuilder(new Type.TypeBuilder("Dummy").build()).withName("dummy").withDescription(null).withParameterLocation(ParameterLocation.BODY)
                                                                                           .isRequired(true).build()));
    }

    @Test
    public void testProcessTestControllerGetDummies() throws Exception {
        Method method = TestController.class.getMethod("getDummies");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verifyZeroInteractions(parameterNameDiscoverer);

        assertThat(document.getUrl(), is("/test/dummy"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is("Gets a list of dummies"));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("List").withGenericTypeNames(new String[]{"Dummy"}).build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerGetDummy() throws Exception {
        Method method = TestController.class.getMethod("getDummy", String.class, String.class, String.class);

        when(parameterNameDiscoverer.getParameterNames(method)).thenReturn(new String[]{null, "requestVar", "headerVar"});
        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verify(parameterNameDiscoverer).getParameterNames(method);
        verifyNoMoreInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test/dummy/{pathVar}"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is("Gets a dummy"));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("Dummy").build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        List<Parameter> parameters = document.getParameters();
        assertThat(parameters, hasSize(3));
        assertThat(parameters.get(0),
                   is(new Parameter.ParameterBuilder(new Type.TypeBuilder("String").build()).withName(null).withDescription(null).withParameterLocation(ParameterLocation.PATH)
                                                                                            .isRequired(true).build()));
        assertThat(parameters.get(1),
                   is(new Parameter.ParameterBuilder(new Type.TypeBuilder("String").build()).withName("requestVar").withDescription("The request parameter description")
                                                                                            .withParameterLocation(ParameterLocation.PARAMETERS).isRequired(true).build()));
        assertThat(parameters.get(2),
                   is(new Parameter.ParameterBuilder(new Type.TypeBuilder("String").build()).withName("headerVarOverride").withDescription(null)
                                                                                            .withParameterLocation(ParameterLocation.HEADER).isRequired(true).build()));
    }

    @Test
    public void testProcessTestControllerMappingMethodEmptyMapping() throws Exception {
        Method method = TestController.class.getMethod("mappingMethodEmptyMapping");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestController.class.getAnnotation(RequestMapping.class), TestController.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("void").build()));
        assertThat(document.getConsumesMimeTypes(), is(nullValue()));
        assertThat(document.getProducesMimeTypes(), is(nullValue()));
        List<Parameter> parameters = document.getParameters();
        assertThat(parameters, is(nullValue()));
    }

    @Test
    public void testProcessTestControllerWithMimeGetDummies() throws Exception {
        Method method = TestControllerWithMime.class.getMethod("getDummies");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestControllerWithMime.class.getAnnotation(RequestMapping.class), TestControllerWithMime.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test2/dummies"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("List").withGenericTypeNames(new String[]{"extends Dummy"}).build()));
        Set<MimeType> consumesMimeTypes = document.getConsumesMimeTypes();
        assertThat(consumesMimeTypes, hasSize(2));
        assertThat(consumesMimeTypes, hasItems(MimeType.APPLICATION_JSON, MimeType.getMimeType("bladie/daa")));

        Set<MimeType> producesMimeTypes = document.getProducesMimeTypes();
        assertThat(producesMimeTypes, hasSize(2));
        assertThat(producesMimeTypes, hasItems(MimeType.APPLICATION_XML, MimeType.getMimeType("bladie/die")));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerWithMimeGetDummiesSuper() throws Exception {
        Method method = TestControllerWithMime.class.getMethod("getDummiesSuper");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestControllerWithMime.class.getAnnotation(RequestMapping.class), TestControllerWithMime.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test2/dummiesSuper"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.GET));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("List").withGenericTypeNames(new String[]{"super Dummy"}).build()));
        Set<MimeType> consumesMimeTypes = document.getConsumesMimeTypes();
        assertThat(consumesMimeTypes, hasSize(1));
        assertThat(consumesMimeTypes, hasItems(MimeType.APPLICATION_JSON));
        Set<MimeType> producesMimeTypes = document.getProducesMimeTypes();
        assertThat(producesMimeTypes, hasSize(1));
        assertThat(producesMimeTypes, hasItems(MimeType.APPLICATION_XML));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerWithMimeGetDummiesVar() throws Exception {
        Method method = TestControllerWithMime.class.getMethod("getDummiesVar");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestControllerWithMime.class.getAnnotation(RequestMapping.class), TestControllerWithMime.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test2/dummiesT"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.DELETE));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("List").withGenericTypeNames(new String[]{"T"}).build()));
        Set<MimeType> consumesMimeTypes = document.getConsumesMimeTypes();
        assertThat(consumesMimeTypes, hasSize(1));
        assertThat(consumesMimeTypes, hasItems(MimeType.APPLICATION_JSON));
        Set<MimeType> producesMimeTypes = document.getProducesMimeTypes();
        assertThat(producesMimeTypes, hasSize(1));
        assertThat(producesMimeTypes, hasItems(MimeType.APPLICATION_XML));
        assertThat(document.getParameters(), is(nullValue()));
    }

    @Test
    public void testProcessTestControllerWithMimeGetDummiesVarExtends() throws Exception {
        Method method = TestControllerWithMime.class.getMethod("getDummiesVarExtends");

        ResourceDocument document = processor
                .process(method.getAnnotation(RequestMapping.class), method, TestControllerWithMime.class.getAnnotation(RequestMapping.class), TestControllerWithMime.class);

        verifyZeroInteractions(parameterNameDiscoverer);
        assertThat(document.getUrl(), is("/test2/dummiesTextend"));
        assertThat(document.getRequestMethods(), hasSize(1));
        assertThat(document.getRequestMethods(), hasItems(RequestMethod.HEAD));
        assertThat(document.getDescription(), is(nullValue()));
        assertThat(document.getReturnType(), is(new Type.TypeBuilder("List").withGenericTypeNames(new String[]{"T extends Dummy"}).build()));
        Set<MimeType> consumesMimeTypes = document.getConsumesMimeTypes();
        assertThat(consumesMimeTypes, hasSize(1));
        assertThat(consumesMimeTypes, hasItems(MimeType.APPLICATION_JSON));
        Set<MimeType> producesMimeTypes = document.getProducesMimeTypes();
        assertThat(producesMimeTypes, hasSize(1));
        assertThat(producesMimeTypes, hasItems(MimeType.APPLICATION_XML));
        assertThat(document.getParameters(), is(nullValue()));
    }
}
