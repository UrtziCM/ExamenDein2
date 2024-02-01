package net.urtzi.examen.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.urtzi.examen.controllers.db.DBManager;
import net.urtzi.examen.models.*;

/**
 * Controlador principal de la aplicacion principal.
 */

public class ProductoController implements javafx.fxml.Initializable {
	@FXML
    private Button btnActualizar;

    @FXML
    private Button btnCrear;

    @FXML
    private Button btnImage;

    @FXML
    private Button btnLimpiar;

    @FXML
    private CheckBox checkboxDispo;

    @FXML
    private TableView<Producto> tablaComida;

    @FXML
    private TextField textfCodigo;

    @FXML
    private TextField txtfNombre;

    @FXML
    private TextField txtfPrecio;
    private DBManager gestor;
    private ObservableList<Producto> data;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gestor = new DBManager();
		prepareTableForDBItems();
		data = gestor.cargarComida();
		tablaComida.setItems(data);
	}
	

    @FXML
    void actualizarComida(ActionEvent event) throws IOException {
    	String codigo = textfCodigo.getText();
    	String nombre = txtfNombre.getText();
    	double precio = Double.parseDouble(txtfPrecio.getText());
    	boolean disponible = checkboxDispo.isSelected();
    	gestor.actualizarProducto(new Producto(codigo,nombre,precio,disponible));
    	tablaComida.setItems(data=gestor.cargarComida());
    }

    @FXML
    void crearComida(ActionEvent event) throws IOException {
    	String codigo = textfCodigo.getText();
    	String nombre = txtfNombre.getText();
    	double precio = Double.parseDouble(txtfPrecio.getText());
    	boolean disponible = checkboxDispo.isSelected();
    	gestor.addProducto(new Producto(codigo,nombre,precio,disponible));
    	data=gestor.cargarComida();
    	tablaComida.setItems(data);
    }

    @FXML
    void limpiarTextfields(ActionEvent event) {
    	textfCodigo.setDisable(false);
    	textfCodigo.setText("");
    	txtfNombre.setText("");
    	txtfPrecio.setText("");
    	checkboxDispo.setSelected(false);
    	btnActualizar.setDisable(true);
    	btnCrear.setDisable(false);
    }

    @FXML
    void onAboutClicked(ActionEvent event) {
    	mostrarVentanaEmergente("INFO", "Gestion de productos V0.1\n Autor: Urtzi", AlertType.INFORMATION);
    }

    @FXML
    void onTableClicked(MouseEvent event) throws SQLException {
    	if (event.getButton() == MouseButton.PRIMARY && tablaComida.getSelectionModel().getSelectedItem() != null) {
    		Producto c = tablaComida.getSelectionModel().getSelectedItem();
    		c = gestor.getProductoByID(c.getCodigo());
    		textfCodigo.setText(c.getCodigo());
    		textfCodigo.setDisable(true);
    		btnCrear.setDisable(true);
    		btnActualizar.setDisable(false);
    		txtfNombre.setText(c.getNombre());
    		txtfPrecio.setText(c.getPrecio()+"");
    		checkboxDispo.setSelected(c.isDisponible());
    	} else if (event.getButton() == MouseButton.SECONDARY && tablaComida.getSelectionModel().getSelectedItem() != null) {
    		ContextMenu menu = new ContextMenu();
    		MenuItem borrar = new MenuItem("Borrar");
    		MenuItem imagen = new MenuItem("Mostrar reporte");
    		imagen.setOnAction(e -> {
				try {
					verImagen();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
    		borrar.setOnAction(e -> {
				try {
					gestor.borrarProducto(tablaComida.getSelectionModel().getSelectedItem());
					tablaComida.setItems(gestor.cargarComida());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
    		menu.getItems().add(borrar);
    		menu.getItems().add(imagen);
    		menu.setX(event.getScreenX());
    		menu.setY(event.getScreenY());
    		menu.show(tablaComida.getScene().getWindow());
    		
    	}
    	
    }
    
    private void verImagen() throws SQLException {
    	Producto c = tablaComida.getSelectionModel().getSelectedItem();
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", c.getCodigo());
        parameters.put("nombre", c.getNombre());
        parameters.put("dispo", c.isDisponible());
    	
    	//llamada con parametros y bbdd
    	//JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/reports/informe.jasper"));
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("producto.jasper"));
	        JasperPrint jprint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
	        JasperViewer viewer = new JasperViewer(jprint, false);
	        viewer.setVisible(true);
		} catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("Ha ocurrido un error");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    private void prepareTableForDBItems() {
		for (TableColumn<Producto, ?> tc : tablaComida.getColumns()) {
			if (tc.getText().toLowerCase().equals("codigo") || tc.getText().toLowerCase().equals("nombre") || tc.getText().toLowerCase().equals("precio"))
				tc.setCellValueFactory(new PropertyValueFactory<>(tc.getText().toLowerCase()));
			else
				tc.setCellValueFactory(new PropertyValueFactory<>("disponible"));
		}
	}
	

	private static void mostrarVentanaEmergente(String titulo, String content, AlertType tipo) {
		Alert anadidoAnimal = new Alert(tipo);
		anadidoAnimal.setTitle(titulo);
		anadidoAnimal.setHeaderText(null);
		anadidoAnimal.setContentText(content);
		anadidoAnimal.showAndWait();
	}
	
    @FXML
    void mostrarAyudaOffline(ActionEvent event) throws IOException {
    	Stage stg = new Stage();
    	BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/VisorAyudaOffline.fxml"));		
		Scene scene = new Scene(root);
		stg.setScene(scene);
		stg.showAndWait();
    }

    @FXML
    void mostrarAyudaOnline(ActionEvent event) throws IOException {
    	Stage stg = new Stage();
    	BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/VisorAyuda.fxml"));		
		Scene scene = new Scene(root);
		stg.setScene(scene);
		stg.showAndWait();
    }


}
