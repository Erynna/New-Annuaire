package ihm;

import java.io.File;

import model.InternProfile;
import model.InternProfileDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxSearchOptions extends HBox {

	private Label lblMenu = new Label("Menu		");
	private Button searchBtn = new Button("Rechercher");
	private Button addBtn = new Button("Ajouter");
	private Button creationbtn = new Button("Création annuaire");
	private Button btnR = new Button("Rafraichir");
	private Button updateBtn = new Button("Mettre à jour");
	private Button deleteBtn = new Button("Supprimer");

	public HBoxSearchOptions() {
		super();	
		setSpacing(10);
		setPadding(new Insets(10.));
		
		setMargin(updateBtn, new Insets(0, 10, 0, 200));

		searchBtn.setPrefSize(100, 30);
		addBtn.setPrefSize(100, 30);
		creationbtn.setPrefSize(100, 30);
		updateBtn.setPrefSize(100, 30);
		deleteBtn.setPrefSize(100, 30);
		
		getChildren().addAll(lblMenu, creationbtn, btnR, searchBtn, addBtn, updateBtn, deleteBtn);

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
				TableView<InternProfile> tableView = root.getTableViewInternProfiles().getTableView();
				
				if (root.getLeft() == null) {

					VBoxSearchOptions searchBox = new VBoxSearchOptions();
					root.setLeft(searchBox);
					searchBox.getTitle().setText("MODIFIER UN STAGIAIRE");

				}
				
				if(tableView.getSelectionModel().getSelectedItem() == null) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message d'alerte");
					alert.setHeaderText("Aucun stagiaire sélectionné");
					alert.setContentText("Veuillez sélectionner le stagiaire à modifier");
					alert.showAndWait();
				}
				
				else {

					InternProfileDao dao = new InternProfileDao();
					VBoxSearchOptions searchBox = (VBoxSearchOptions) root.getLeft();
					InternProfile oldIp = tableView.getSelectionModel().getSelectedItem();
					InternProfile newIp = new InternProfile(searchBox.getTextFieldSurname().getText(), searchBox.getTextFieldFirstName().getText(), searchBox.getTextFieldCounty().getText(), searchBox.getTextFieldPromotion().getText(), searchBox.getCbYearStudy().getValue());
					dao.modifyInternProfile(oldIp, newIp);
					root.setCenter(new TableViewInternProfiles(dao.getAll()));

				}

			}
		});

		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				MainPannel root = (MainPannel) getScene().getRoot();
				
				TableView<InternProfile> tableView = root.getTableViewInternProfiles().getTableView();
				
				InternProfile profile = tableView.getSelectionModel().getSelectedItem();
				
				InternProfileDao dao = new InternProfileDao();
				
				dao.deleteInternProfile(profile);
				
				root.setCenter(new TableViewInternProfiles(dao.getAll()));
				
			}
		});
		
		File annuaire = new File("./internBDD.bin");
		btnR.setOnAction(new EventHandler<ActionEvent>() {		//Fonction --> btnR du main pannel à enlever
			
			@Override
			public void handle(ActionEvent event) {
				if(annuaire.exists()) {
					InternProfileDao dao = new InternProfileDao();
					TableViewInternProfiles tableViewInternProfiles = new TableViewInternProfiles(dao.getAll());
					MainPannel root = (MainPannel) getScene().getRoot();
					root.setCenter(tableViewInternProfiles);
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
	public Label getLblMenu() {
		return lblMenu;
	}
	public void setLblMenu(Label lblMenu) {
		this.lblMenu = lblMenu;
	}
	public Button getBtnR() {
		return btnR;
	}
	public void setBtnR(Button btnR) {
		this.btnR = btnR;
	}
	
}
