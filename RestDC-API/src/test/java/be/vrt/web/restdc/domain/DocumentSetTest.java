package be.vrt.web.restdc.domain;

import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentSetTest {
    private DocumentSet sut;

    @Test
    public void test_build() throws Exception {
        ResourceDocument rd = new ResourceDocument.ResourceDocumentBuilder().build();
        DocumentSet ds = new DocumentSet.DocumentSetBuilder("id").addDocument(rd).addAll(Arrays.asList(rd, rd)).build();
        assertThat(ds.getId(), is("id"));
        assertThat(ds.getDocuments(), hasSize(3));
        assertThat(ds.getDocuments(), is(Arrays.asList(rd, rd, rd)));
    }

    @Test
    public void test_build_toString() throws Exception {
        DocumentSet ds = new DocumentSet.DocumentSetBuilder("id").build();
        assertThat(ds.toString(), is("DocumentSet{id='id', documents=[]}"));
    }
}