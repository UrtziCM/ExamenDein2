package net.urtzi.examen.models;

public class Producto {
	private String codigo;
	private String nombre;
	private double precio;
	private boolean disponible;
	
	public String getCodigo() {
		return codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public double getPrecio() {
		return precio;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public Producto(String codigo, String nombre, double precio, boolean disponible) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.disponible = disponible;
	}
}
