/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxLUN"
    private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaComponenteConnessa"
    private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaOggetti"
    private Button btnCercaOggetti; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizzaOggetti"
    private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

    @FXML // fx:id="txtObjectId"
    private TextField txtObjectId; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    
    
    @FXML
    void doAnalizzaOggetti(ActionEvent event) {
    	//creo il grafo
    	txtResult.clear();
    	this.model.creaGrafo();
    	txtResult.appendText("GRAFO CREATO!!\n\n");
    	txtResult.appendText("# VERTICI: " + this.model.getNVertici());
    	txtResult.appendText(" # ARCHI: " + this.model.getNArchi());
    }

    @FXML
    void doCalcolaComponenteConnessa(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	try {
    		
    		int id = Integer.parseInt(txtObjectId.getText());
    	
    		if (this.model.presente(id)) {
    			txtResult.appendText("Numero di vertici nella componente connessa: " + this.model.getCompConnessa(id));
    			boxLUN.getItems().clear();
    			for (int i = 2; i<=this.model.getCompConnessa(id); i++) {
        			boxLUN.getItems().add(i);
        		}
    		} else {
    			txtResult.appendText("Errore: il codice non è presente nel grafico");
    		}
    		
    		
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Errore: il codice dell'oggetto deve essere un numero intero");
    		return;
    	}

    }

    @FXML
    void doCercaOggetti(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	try {
    		
    		int id = Integer.parseInt(txtObjectId.getText());
    		
    		if (boxLUN.getValue() !=null) {
    			txtResult.setText("Percorso migliore:\n");
    			for( ArtObject o : this.model.ricerca(boxLUN.getValue(), id)) {
    				txtResult.appendText(o.toString()+"\n");
    			}
    			txtResult.appendText("Peso totale: "+this.model.getPesoMax());
    			
    		} else {
    			txtResult.setText("Errore: inserire un valore per la lunghezza, se non ci sono valori nel menù cliccare componente connessa per generarli");
    		}
    		
    		
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Errore: il codice dell'oggetto deve essere un numero intero");
    		return;
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
