package be.vrt.web.restdc.reflection;

import be.vrt.web.restdc.domain.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author mike
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeReflectionUtilTest {


    @Test
    public void test_getTypeFromSimpleClass() throws Exception {
        Type t = TypeReflectionUtil.getTypeFromReflectionType(String.class);
        assertThat(t.getTypeName(), is("String"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(0)));
    }

    @Test
    public void test_getTypeFromGenericClass() throws Exception {
        Type t = TypeReflectionUtil.getTypeFromReflectionType(List.class);
        assertThat(t.getTypeName(), is("List"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(1)));
        assertThat(t.getGenericTypeNames()[0], is("E"));
    }

    @Test
    public void test_getTypeFromExtendedGenericClass() throws Exception {
        Type t = TypeReflectionUtil.getTypeFromReflectionType(SpecialList.class);
        assertThat(t.getTypeName(), is("SpecialList"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(1)));
        assertThat(t.getGenericTypeNames()[0], is("T extends CharSequence"));
    }

    @Test
    public void test_getTypeFromSpecificExtendedGenericClass() throws Exception {
        Type t = TypeReflectionUtil.getTypeFromReflectionType(SpecificList.class);
        assertThat(t.getTypeName(), is("SpecificList"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(0)));
    }

    @Test
    public void test_parameterizedMethodReturnType() throws Exception {
        Method m = SpecialList.class.getMethod("numberMethod", Number.class);
        Type t = TypeReflectionUtil.getTypeFromReflectionType(m.getReturnType());

        assertThat(t.getTypeName(), is("Number"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(0)));
    }

    @Test
    public void test_parameterizedMethodParamater() throws Exception {
        Method m = SpecialList.class.getMethod("numberMethod", Number.class);
        Type t = TypeReflectionUtil.getTypeFromReflectionType(m.getGenericParameterTypes()[0]);

        assertThat(t.getTypeName(), is("E extends Number"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(0)));
    }

    @Test
    public void test_wildcard() throws Exception {
        Method m = SpecialList.class.getMethod("wildcardReturn");
        Type t = TypeReflectionUtil.getTypeFromReflectionType(m.getGenericReturnType());

        assertThat(t.getTypeName(), is("List"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(1)));
        assertThat(t.getGenericTypeNames()[0], is("extends Date"));
    }

    @Test
    public void test_wildcardSuper() throws Exception {
        Method m = SpecialList.class.getMethod("wildcardSuperReturn");
        Type t = TypeReflectionUtil.getTypeFromReflectionType(m.getGenericReturnType());

        assertThat(t.getTypeName(), is("List"));
        assertThat(t.getGenericTypeNames(), is(arrayWithSize(1)));
        assertThat(t.getGenericTypeNames()[0], is("super Date"));
    }

    private class SpecialList<T extends CharSequence> extends ArrayList<T> {
        public <E extends Number> E numberMethod(final E e) {
            return e;
        }

        public List<? extends Date> wildcardReturn() {
            return null;
        }

        public List<? super Date> wildcardSuperReturn() {
            return null;
        }

    }

    private class SpecificList extends SpecialList<String> {

    }
}