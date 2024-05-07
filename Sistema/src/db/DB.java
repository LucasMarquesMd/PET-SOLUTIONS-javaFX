package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


//Classe de conexao com DB
public class DB {

	private static Connection conn = null;
	
	//Retorna uma conexao
	public static Connection getConnection() {
		//Se conn nao estiver instanciada
		if (conn == null) {
			try {
				//Carrega os dados de "db.properties"
				Properties props = loadProperties();
				String url = props.getProperty("dburl");//pega o "valor" da url do DB
				//DriverManager -> classe degerenciamento de drivers de conexao com DB - JDBC
				conn = DriverManager.getConnection(url, props);//pega a conexao na url e utiliza os dados do props para acesso
				//getConnection() -> recebe um objeto properties com propriedades adicionais adicionais para a conexao
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	//Encerra a conexao com DB
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	//Properties -> classe que implementa a interface map
	//Classe voltada para configuracoes de sistema ou apps
	//Par de "chaves" e "valor"
	
	
	//Retotna as informacoes para conexao com DB MySql
	private static Properties loadProperties() {
		//Leitura de arquivos stream()
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();//Instancia on properties
			props.load(fs);//carrega os pares de "chave", "valor" da leitura do arquivo (fs)
			return props;
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	//Statement -> usado para executar instrucoes sql no DB
	//Encerra o statement
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	//ResultSet -> interface que itera e acessa os dados resultandes de uma consulta sql (Statement)
	//Os dados sao armazenados em forma de tabela
	//Encerra o statement
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
