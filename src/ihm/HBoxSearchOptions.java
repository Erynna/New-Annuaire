package ihm;

import java.io.File;

import model.InternProfile;
import model.InternProfileComparator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HBoxSearchOptions extends HBox {

	private Button searchBtn = new Button("Rechercher");
	private Button addBtn = new Button("Ajouter");
	private Button creationbtn = new Button("Création annuaire");

	private Button updateBtn = new Button("Mettre à jour");
	private Button deleteBtn = new Button("Supprimer");

	public HBoxSearchOptions() {
		super();	
		setSpacing(10);
		setPadding(new Insets(10.));

		searchBtn.setPrefSize(100, 30);
		addBtn.setPrefSize(100, 30);
		creationbtn.setPrefSize(100, 30);

		getChildren().addAll(searchBtn, addBtn, creationbtn, updateBtn, deleteBtn);

		addBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
            public void handle(ActionEvent event) {
                VBoxAddProfile vBoxAddProfile = new VBoxAddProfile();
                MainPannel root = (MainPannel) getScene().getRoot();

                vBoxAddProfile.getTitle().setText("AJOUTER UN STAGIAIRE");

                root.setLeft(vBoxAddProfile);

            }
        });

		//Instancie une VBox avec l'ensemble des champs nécessaire pour une recherche
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				VBoxSearchOptions searchBox = new VBoxSearchOptions();

				MainPannel root = (MainPannel) getScene().getRoot();

				searchBox.getTitle().setText("RECHERCHER UN STAGIAIRE");

				root.setLeft(searchBox);							

			}
		});

		//Ajout de la fonctionnalité création d'un annuaire dans la Hbox -> ouverture d'une fenêtre

		creationbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				CreationPan root = new CreationPan();
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle("Création de l'annuaire");
				stage.setScene(scene);
				stage.show();	
			}
		});

		updateBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				MainPannel root = (MainPannel) getScene().getRoot();

				if (root.getLeft() == null) {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message d'alerte");
					alert.setHeaderText("Aucun stagiaire sélectionné");
					alert.setContentText("Vous n'avez sélectionnez aucun Stagiaire à modifier");
					alert.showAndWait();


				}else {

					//Méthode d'actualisation de la BDD

				}

			}
		});

	}

	public Button getSearchBtn() {
		return searchBtn;
	}

	public void setSearchBtn(Button searchBtn) {
		this.searchBtn = searchBtn;
	}

	public Button getAddBtn() {
		return addBtn;
	}

	public void setAddBtn(Button addBtn) {
		this.addBtn = addBtn;
	}

	public Button getCreationbtn() {
		return creationbtn;
	}

	public void setCreationbtn(Button creationbtn) {
		this.creationbtn = creationbtn;
	}

	public Button getUpdateBtn() {
		return updateBtn;
	}

	public void setUpdateBtn(Button updateBtn) {
		this.updateBtn = updateBtn;
	}

	public Button getDeleteBtn() {
		return deleteBtn;
	}

	public void setDeleteBtn(Button deleteBtn) {
		this.deleteBtn = deleteBtn;
	}
}
