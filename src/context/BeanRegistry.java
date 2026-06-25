package context;

import java.util.HashMap;
import java.util.Map;

public class BeanRegistry {

    private Map<Class<?>, Object> beans;

    public BeanRegistry() {

        beans = new HashMap<>();
    }

    public <T> void register(

            Class<T> clazz,

            T object
    ) {

        beans.put(clazz, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(

            Class<T> clazz
    ) {

        return (T) beans.get(clazz);
    }
}
