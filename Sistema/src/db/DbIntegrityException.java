package db;

public class DbIntegrityException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	//Retorna uma excessao do tipo RunTime
	public DbIntegrityException(String msg) {
		super(msg);
	}
}
