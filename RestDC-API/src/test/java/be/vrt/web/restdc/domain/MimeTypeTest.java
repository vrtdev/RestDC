package be.vrt.web.restdc.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author mike
 */
@RunWith(MockitoJUnitRunner.class)
public class MimeTypeTest {
    private MimeType sut;

    @Test
    public void test_getMimeType() throws Exception {
        MimeType json = MimeType.getMimeType("application/json", "json");
        assertThat(json, is(sameInstance(MimeType.APPLICATION_JSON)));
    }

    @Test
    public void test_getNonExisting() throws Exception {
        MimeType junk = MimeType.getMimeType("junk/trunk", "js", "jt", "jx");
        assertThat(junk, is(notNullValue()));
        assertThat(junk.getInternetMediaType(), is("junk/trunk"));
        assertThat(junk.getPreferredFileExtension(), is("js"));
        assertThat(junk.toString(), is("MimeType{internetMediaType='junk/trunk', fileExtensions=[js, jt, jx]}"));
    }

    @Test
    public void test_get_no_extensions() throws Exception {
        MimeType junk = MimeType.getMimeType("junk/shmunk");
        assertThat(junk, is(notNullValue()));
        assertThat(junk.getInternetMediaType(), is("junk/shmunk"));
        assertThat(junk.getPreferredFileExtension(), is(nullValue()));
        assertThat(junk.toString(), is("MimeType{internetMediaType='junk/shmunk', fileExtensions=[]}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_null_mime() throws Exception {
        MimeType.getMimeType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_blank_mime() throws Exception {
        MimeType.getMimeType("     \t    \n");
    }
}