package DAO;

public abstract class DAO<T> {

    public abstract T create(T obj);

    public abstract T find(String id);

    
    public abstract T update(T obj);

    public abstract void delete(T obj);

	public void delete(String file) {
		// TODO Auto-generated method stub
		
	}

}
