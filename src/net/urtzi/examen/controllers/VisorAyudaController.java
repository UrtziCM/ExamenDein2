package net.urtzi.examen.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class VisorAyudaController implements Initializable {
	@FXML
	private WebView visor;
	private WebEngine webEngine;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		webEngine = visor.getEngine();
		webEngine.load("https://moodle.icjardin.com/index.php");
	}
}
