package notes.data.cache;

import notes.businessobjects.XMLSerializable;
import notes.dao.DuplicateRecordException;

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

    /**
     * Insert a new record of type T into the cache.
     * Note: A new object with type T will be created and stored in the cache.
     *
     * @param object The object that contains the data to insert.
     * @return {@code T} The new object stored in the cache.
     * @throws DuplicateRecordException
     */
    public T insert(T object) throws DuplicateRecordException;

    /**
     * Remove a record of type T with a particular id from the cache.
     * Note: It is not guaranteed that there is an existing object with the given id stored in the cache.
     *
     * @param id The id of the object that is going to be removed.
     * @return {@code T} The removed object; null if no object is found in the cache.
     */
    public T remove(Long id);

    /**
     * Update a record of type T in the cache.
     *
     * @param object The object that contains the data to update.
     * @return {@code T} The updated object stored in the cache.
     * @throws DuplicateRecordException
     */
    public T update(T object) throws DuplicateRecordException;

    /**
     * Find an object in the cache with the given id.
     *
     * @param id The id of the object.
     * @return {@code T} The found object; null if there is no object found in the cache.
     */
    public T find(Long id);

    /**
     * Find all object of type T stored in the cache.
     *
     * @return {@code List} A list of objects that are found in the cache; an empty list if no record is found.
     *         Note: the objects in the list is not sorted.
     */
    public List<T> findAll();

    /**
     * Initialize the cache.
     */
    public void initialize();

    /**
     * To tell if the data stored in the cache has been changed.
     *
     * @return true if the data in the cache has been changed, false otherwise.
     */
    public boolean isCacheChanged();
}
