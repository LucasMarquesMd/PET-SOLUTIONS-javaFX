package db;

public class DbException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	//Retorna uma excessao do tipo RunTime
	public DbException(String msg) {
		super(msg);
	}
}
