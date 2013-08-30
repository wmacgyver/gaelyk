package groovyx.gaelyk.query;

import groovy.lang.Closure;
import groovyx.gaelyk.extensions.DatastoreExtensions;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Index;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;

/**
 * Coerced list.
 * 
 * @author Vladimir Orany
 * 
 * @param <T> coerced class
 */
class CoercedQueryResultList<T> extends AbstractList<T> implements QueryResultListWithQuery<T>, Serializable {

    private static final String ERROR_MESSAGE = "You cannot modify this list. Copy the list by calling .collect() method first or use non-mutating version if you want do so";
    private static final long serialVersionUID = 3169182603336643619L;
    private final Query                     query;
    private final QueryResultList<Entity>   originalList;
    private final Class<T>                  coercedClass;

    private CoercedQueryResultList(Query query, QueryResultList<Entity> originalList, Class<T> coercedClass){
        if(originalList == null) {
            throw new IllegalArgumentException("Original list cannot be null!");
        }
        this.query          = query;
        this.originalList   = originalList;
        this.coercedClass   = coercedClass;
    }

    /**
     * Creates new coerced query result list wrapper
     * @param originalList original query result list
     * @param coercedClass class to coerce the entities
     * @return coerced wrapper list
     */
    public static <T> CoercedQueryResultList<T> coerce(Query query, QueryResultList<Entity> originalList, Class<T> coercedClass) {
        return new CoercedQueryResultList<T>(query, originalList, coercedClass);
    }

    @Override public Cursor getCursor() {
        return originalList.getCursor();
    }

    @Override public List<Index> getIndexList() {
        return originalList.getIndexList();
    }
    
    @Override public Query getQuery() {
        return query;
    }

    @Override public T get(int index) {
        return coercedClass.cast(DatastoreExtensions.asType(originalList.get(index), coercedClass));
    }

    @Override public int size() {
        return originalList.size();
    }
    
    @Override public T set(int index, T element) {
        throw new UnsupportedOperationException(ERROR_MESSAGE); 
    }
    
    @Override public void add(int index, T element) {
        throw new UnsupportedOperationException(ERROR_MESSAGE); 
    }
    
    @Override public T remove(int index) {
        throw new UnsupportedOperationException(ERROR_MESSAGE); 
    }
    
    public List<T> collect() {
        return DefaultGroovyMethods.collect(this);
    }
    
    
    public Collection<T> unique() {
        return DefaultGroovyMethods.unique(this, false);
    }
    
    public Collection<T> unique(Closure closure) {
        return DefaultGroovyMethods.unique(this, false, closure);
    }
    
    public Collection<T> unique(Comparator<T> comparator) {
        return DefaultGroovyMethods.unique(this, false, comparator);
    }
    
    public List<T> sort() {
        return DefaultGroovyMethods.sort(this, false);
    }
    
    public List<T> sort(Closure closure) {
        return DefaultGroovyMethods.sort(this, false, closure);
    }
    
    public List<T> sort(Comparator<T> comparator) {
        return DefaultGroovyMethods.sort(this, false, comparator);
    }
    
    public List<T> reverse() {
        return DefaultGroovyMethods.reverse(this, false);
    } 

}
