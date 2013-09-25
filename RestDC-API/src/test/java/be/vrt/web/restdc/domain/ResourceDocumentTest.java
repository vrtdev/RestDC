package be.vrt.web.restdc.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * @author mike
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceDocumentTest {


    @Test
    public void test_build() throws Exception {
        Type rt = new Type.TypeBuilder("name").build();
        Set<MimeType> cts = new HashSet<>(Arrays.asList(MimeType.APPLICATION_JS, MimeType.APPLICATION_JSON));
        Set<MimeType> pts = new HashSet<>(Arrays.asList(MimeType.APPLICATION_XML, MimeType.APPLICATION_PDF));
        Set<RequestMethod> rm = new HashSet<>(Arrays.asList(RequestMethod.DELETE, RequestMethod.POST));
        Type t = new Type.TypeBuilder("name").build();
        Parameter p = new Parameter.ParameterBuilder(t).withName("one").build();
        Parameter q = new Parameter.ParameterBuilder(t).withName("two").build();
        Parameter r = new Parameter.ParameterBuilder(t).withName("three").build();
        List<Parameter> pms = Arrays.asList(p, q);

        ResourceDocument rd = new ResourceDocument.ResourceDocumentBuilder().withDescription("desc").withReturnType(rt).withUrl("url").addAllConsumingMimeTypes(cts)
                                                                            .addAllParameters(pms).addAllProducingMimeTypes(pts).addAllRequestMethods(rm)
                                                                            .addConsumingMimeType(MimeType.IMAGE_GIF).addProducingMimeType(MimeType.IMAGE_JPEG).addParameter(r)
                                                                            .addRequestMethod(RequestMethod.GET).addConsumingMimeTypesWithStrings("image/png")
                                                                            .addProducingMimeTypesWithStrings("image/tiff").build();
        assertThat(rd.getConsumesMimeTypes(), hasItems(MimeType.APPLICATION_JS, MimeType.APPLICATION_JSON, MimeType.IMAGE_GIF, MimeType.IMAGE_PNG));
        assertThat(rd.getProducesMimeTypes(), hasItems(MimeType.APPLICATION_XML, MimeType.APPLICATION_PDF, MimeType.IMAGE_JPEG, MimeType.IMAGE_TIFF));
        assertThat(rd.getParameters(), hasItems(p, q));
        assertThat(rd.getRequestMethods(), hasItems(RequestMethod.DELETE, RequestMethod.POST, RequestMethod.GET));
        assertThat(rd.getDescription(), is("desc"));
        assertThat(rd.getReturnType(), is(sameInstance(rt)));
        assertThat(rd.getUrl(), is("url"));
        assertThat(rd.toString(), is(notNullValue()));

    }
}