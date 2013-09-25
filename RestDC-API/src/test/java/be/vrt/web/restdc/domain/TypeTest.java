package be.vrt.web.restdc.domain;

import com.google.common.testing.EqualsTester;
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
public class TypeTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_build_invalid() throws Exception {
        new Type.TypeBuilder(null);
    }

    @Test
    public void test_build() throws Exception {
        Type t = new Type.TypeBuilder("typeName").withGenericTypeNames("one", "two").build();
        assertThat(t.getTypeName(), is("typeName"));
        assertThat(t.getGenericTypeNames(), is(arrayContaining("one", "two")));
    }

    @Test
    public void test_equality() throws Exception {
        Type ta = new Type.TypeBuilder("typeName").withGenericTypeNames("one", "two").build();
        Type tb = new Type.TypeBuilder("typeName").withGenericTypeNames("one", "two").build();
        Type tc = new Type.TypeBuilder("emaNepyt").withGenericTypeNames("one", "two").build();
        Type td = new Type.TypeBuilder("typeName").withGenericTypeNames("one").build();

        new EqualsTester().addEqualityGroup(ta, tb).addEqualityGroup(tc).addEqualityGroup(td).testEquals();
    }
}