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
public class ParameterTest {

    @Test(expected = IllegalArgumentException.class)
    public void test_build_invalid_type() throws Exception {
        new Parameter.ParameterBuilder(null).build();
    }

    @Test
    public void test_builder_returns_equal_objects() throws Exception {
        Type t = new Type.TypeBuilder("String").build();
        Parameter.ParameterBuilder builder = new Parameter.ParameterBuilder(t).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY);
        Parameter p = builder.build();
        Parameter q = builder.build();
        assertThat(p, is(not(sameInstance(q))));
        assertThat(p, is(equalTo(q)));
        assertThat(p.hashCode(), is(q.hashCode()));
    }

    @Test
    public void test_different_builders_return_equal_objects() throws Exception {
        Type t = new Type.TypeBuilder("String").build();
        Parameter p = new Parameter.ParameterBuilder(t).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        Parameter q = new Parameter.ParameterBuilder(t).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        assertThat(p, is(equalTo(q)));
        assertThat(p.hashCode(), is(q.hashCode()));
    }

    @Test
    public void test_builderBuildsCorrectValues() throws Exception {
        Type t = new Type.TypeBuilder("String").build();
        Parameter p = new Parameter.ParameterBuilder(t).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        assertThat(p.getDescription(), is("desc"));
        assertThat(p.getName(), is("name"));
        assertThat(p.getParameterLocation(), is(ParameterLocation.BODY));
        assertThat(p.getType(), is(t));
        assertThat(p.isRequired(), is(true));
        assertThat(p.toString(), is("Parameter{name=name, description=desc, type=Type{typeName=String, genericTypeNames=[]}, parameterLocation=BODY, required=true}"));
    }

    @Test
    public void test_equals() throws Exception {
        Type ta = new Type.TypeBuilder("String").build();
        Type tb = new Type.TypeBuilder("Other").build();
        Parameter pa = new Parameter.ParameterBuilder(ta).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        Parameter pb = new Parameter.ParameterBuilder(ta).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        Parameter pc = new Parameter.ParameterBuilder(tb).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        Parameter pd = new Parameter.ParameterBuilder(ta).withDescription("csed").withName("name").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        Parameter pe = new Parameter.ParameterBuilder(ta).withDescription("desc").withName("eman").isRequired(true).withParameterLocation(ParameterLocation.BODY).build();
        Parameter pf = new Parameter.ParameterBuilder(ta).withDescription("desc").withName("name").isRequired(false).withParameterLocation(ParameterLocation.BODY).build();
        Parameter pg = new Parameter.ParameterBuilder(ta).withDescription("desc").withName("name").isRequired(true).withParameterLocation(ParameterLocation.HEADER).build();

        new EqualsTester().addEqualityGroup(pa, pb).addEqualityGroup(pc).addEqualityGroup(pd).addEqualityGroup(pe).addEqualityGroup(pf).addEqualityGroup(pg).testEquals();


    }
}