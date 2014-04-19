package notes.data.cache;

import notes.businessobjects.XMLSerializable;

import java.util.List;

/**
 * A data storing object that provides atomic data accessing operations to upper layer. It serves as a runtime
 * in-memory database between the layer of DAO and disk storage.
 * <p/>
 * Author: Rui Du
 * Date: 4/15/14
 * Time: 3:06 AM
 */
public interface Cache<T> extends XMLSerializable<Cache> {

    public T insert(T object);

    public void remove(Long id);

    public T update(T object);

    public T find(Long id);

    public List<T> findAll();
}
