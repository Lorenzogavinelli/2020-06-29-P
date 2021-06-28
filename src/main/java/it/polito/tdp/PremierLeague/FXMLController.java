/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	
    	List<Arco>best = new LinkedList<Arco>();
    	best.addAll(this.model.getMostPlayed());
    	
    	for(Arco a: best) {
    		this.txtResult.appendText(a.toString() + "\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	Integer mese = this.cmbMese.getValue();
    	Double minuti = Double.parseDouble(this.txtMinuti.getText());
    	this.model.creaGrafo(mese, minuti);
    	
    	txtResult.appendText("Numero vertici: " + model.numeroVertici() + "\n");
    	txtResult.appendText("Numero archi: " + model.numeroArchi() + "\n");
    	
    	
    	this.cmbM1.getItems().addAll(this.model.getTendina());
    	this.cmbM2.getItems().addAll(this.model.getTendina());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    	Match partenza = this.cmbM1.getValue();
    	Match arrivo = this.cmbM2.getValue();
    	List<Match> rico = new ArrayList<Match>();
    	rico.addAll(this.model.camminoMigliore(partenza, arrivo));
    	double peso = this.model.peso();
    	
    	this.txtResult.clear();
    	for(Match m : rico) {
    		this.txtResult.appendText(m.toString() + "\n");
    	}
    	this.txtResult.appendText("Peso: " + peso);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List <Integer> mesi = new LinkedList<>();
    	Integer [] arrayMesi = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    	for(Integer i : arrayMesi) {
    		mesi.add(i);
    	}
    	cmbMese.getItems().addAll(mesi);
    }
    
    
}
