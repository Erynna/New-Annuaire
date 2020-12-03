package ihm;

import java.io.File;
import model.CreationAnnuaire;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * Ce panneau permet la création d'un annuaire à partir d'un fichier choisi par l'utilisateur
 */

public class CreationPan extends BorderPane {

	private Label lblFile;
	private Button btnBrowse;
	private HBox hbPath;
	private Label lblInfo1;
	private Button btnOk;
	private Label lblInfo2;
	private Label error;

	public CreationPan() {

		lblInfo1 = new Label("Choisissez le fichier à partir duquel vous souhaitez créer un annuaire : \r");
		btnBrowse = new Button("Rechercher");
		btnBrowse.setPrefSize(100, 40);
		
		btnOk = new Button("Ok");
		setTop(lblInfo1);
		setCenter(btnBrowse);
		setBottom(btnOk);
		setPadding(new Insets(10.));
		setPrefSize(400, 100);

		//Action demandant à l'utilisateur de choisir un fichier afin de créer l'annuaire au format bianaire
		btnBrowse.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File f = fileChooser.showOpenDialog(new Stage().getOwner());
				if(f != null) {
					lblFile = new Label();
					lblFile.setText(f.getAbsolutePath());
				}
			}
		});
		
		//Action faisant appel à la méthode création annuaire
		btnOk.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(lblFile.getText() != null) {
					String origin = lblFile.getText();
					creation(origin);
					lblInfo2 = new Label("La création de votre annuaire a bien été réalisée");
					setCenter(lblInfo2);					
					//Fermeture auto
					Stage stage = (Stage) getScene().getWindow();
					stage.close();
				}
			}
		});	
	
	}

	public void creation(String fileOrigin) {
		CreationAnnuaire annuaire = new CreationAnnuaire(fileOrigin);
		annuaire.createInternsBDDFile();
	}
	
	public Label getLblFile() {
		return lblFile;
	}
	public void setLblFile(Label lblFile) {
		this.lblFile = lblFile;
	}
	public Button getBtnBrowse() {
		return btnBrowse;
	}
	public void setBtnBrowse(Button btnBrowse) {
		this.btnBrowse = btnBrowse;
	}
	public HBox getHbPath() {
		return hbPath;
	}
	public void setHbPath(HBox hbPath) {
		this.hbPath = hbPath;
	}
	public Label getLblInfo1() {
		return lblInfo1;
	}
	public void setLblInfo1(Label lblInfo1) {
		this.lblInfo1 = lblInfo1;
	}
	public Label getLblInfo2() {
		return lblInfo2;
	}
	public void setLblInfo2(Label lblInfo2) {
		this.lblInfo2 = lblInfo2;
	}
	public Button getBtnOk() {
		return btnOk;
	}
	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
	public Label getError() {
		return error;
	}
	public void setError(Label error) {
		this.error = error;
	}

}
