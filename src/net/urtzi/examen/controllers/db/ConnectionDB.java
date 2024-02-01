package net.urtzi.examen.controllers.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB {
	private Connection conexion;
	private Properties prop;

	public ConnectionDB() throws SQLException {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(new File(this.getClass().getResource("/properties/secret.properties").getPath()))){
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = prop.getProperty("db.url");
		String baseDatos = prop.getProperty("db.name");
		String usuario = prop.getProperty("db.user");
		String password = prop.getProperty("db.pwd");
		String cadenaConexion = "jdbc:mysql://" + host + ":3306/" + baseDatos;
		conexion = DriverManager.getConnection(cadenaConexion, usuario, password);
		conexion.setAutoCommit(true);
	}

	public Connection getConexion() {
		return conexion;
	}

	public void closeConexion() {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}