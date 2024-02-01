package net.urtzi.examen.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class VisorAyudaControllerOffline implements Initializable {

    @FXML
    private ListView<String> listaHtml;

    @FXML
    private WebView visor;
    private WebEngine webEngine;

    
    
    @FXML
    void changeToSelectedHTML(MouseEvent event) {
    	if (listaHtml.getSelectionModel().getSelectedItem() != null) {
    		webEngine.load(this.getClass().getResource("/html/"+listaHtml.getSelectionModel().getSelectedItem() +".html").toExternalForm());
    		
    	}
    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		webEngine = visor.getEngine();
		listaHtml.getItems().add("help");
		listaHtml.getItems().add("ayuda");

	}

}
