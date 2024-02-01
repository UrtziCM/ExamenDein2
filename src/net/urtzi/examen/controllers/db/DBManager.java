package net.urtzi.examen.controllers.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import net.urtzi.examen.models.*;

/**
 * Database manager class uses a ConnectionDB to access a database and retrieve
 * data.
 * 
 * @see net.urtzi.olimpiadas.controller.database.ConnectionDB
 */
public class DBManager {
	private ConnectionDB conexion;

	/**
	 * Retrieves the data from the Deporte table in the database and returns an
	 * ObservableList with it.
	 * 
	 * @return ObservableList with the Deporte objects from the database.
	 * @see net.urtzi.examen.models.Deporte
	 */
	public ObservableList<Producto> cargarComida() {
		ObservableList<Producto> comidas = FXCollections.observableArrayList();
		try {
			conexion = new ConnectionDB();
			String consulta = "SELECT * FROM productos";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String codigo = rs.getString("codigo");
				String nombre = rs.getString("nombre");
				double precio= rs.getDouble("precio");
				boolean disponible =(rs.getInt("disponible")==1)?true:false;
				Producto c = new Producto(codigo,nombre,precio,disponible);
				comidas.add(c);
			}

			rs.close();
			conexion.closeConexion();

		} catch (SQLException e) {
			System.out.println("Error en la carga de productos desde la base de datos");
			e.printStackTrace();
		}
		return comidas;
	}
	/**
	 * Adds a product to the database using the given object
	 * @param comida the object to be added to the DB
	 */
	public void addProducto(Producto comida) {
		try {
			conexion = new ConnectionDB();
			String sqlAddEquipo;
			sqlAddEquipo = "INSERT INTO productos VALUES(?,?,?,?,?)";
			PreparedStatement pstm = conexion.getConexion().prepareStatement(sqlAddEquipo);
			pstm.setString(1, comida.getCodigo());
			pstm.setString(2, comida.getNombre());
			pstm.setDouble(3, comida.getPrecio());
			int disponible=(comida.isDisponible())?1:0;
			pstm.setInt(4, disponible);
			pstm.setBinaryStream(5, null);
			pstm.execute();
			conexion.closeConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Returns the object with the given code on the database for easy access
	 * @param codigo the code of the product in the database
	 * @return The product if exists else null
	 * @throws SQLException if there are any issues with the database
	 */
	public Producto getProductoByID(String codigo) throws SQLException {
		conexion = new ConnectionDB();
		Producto equ = null;
		String sql = String.format("SELECT * FROM productos WHERE codigo = ?");
		PreparedStatement pstmt = conexion.getConexion().prepareStatement(sql);
		pstmt.setString(1, codigo);
		pstmt.executeQuery();
		ResultSet rs = pstmt.getResultSet();
		if (rs.next()) {
			equ = new Producto(rs.getString("codigo"), rs.getString("nombre"), rs.getDouble("precio"), rs.getBoolean("disponible"));
		}
		conexion.closeConexion();
		return equ;
	}
	/**
	 * Updates the given object in the database
	 * @param comida the item to be updated
	 */
	public void actualizarProducto(Producto comida) {
		try {
			conexion = new ConnectionDB();
			String sqlAddEquipo;
			sqlAddEquipo = "REPLACE INTO productos VALUES(?,?,?,?,?)";
			PreparedStatement pstm = conexion.getConexion().prepareStatement(sqlAddEquipo);
			pstm.setString(1, comida.getCodigo());
			pstm.setString(2, comida.getNombre());
			pstm.setDouble(3, comida.getPrecio());
			int disponible=(comida.isDisponible())?1:0;
			pstm.setInt(4, disponible);
			pstm.executeUpdate();
			conexion.closeConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Erases a product from the database.
	 * @param comida The product to be erased
	 * @throws SQLException if there are any problems with the database.
	 */
	public void borrarProducto(Producto comida) throws SQLException {
		conexion = new ConnectionDB();
		String sql = "DELETE FROM productos WHERE codigo=?";
		PreparedStatement stmt = conexion.getConexion().prepareStatement(sql);
		stmt.setString(1, comida.getCodigo());
		stmt.executeUpdate();
		conexion.closeConexion();
	}
	

	
	


}