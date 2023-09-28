package mockdata.peoplesoft;

import mockdata.ams.Repository;

public interface Generator<E> {
    // TODO: require generator(...) method
    public Repository<E> getRepository();
}
