package ihm;

import model.InternProfile;
import model.InternProfileDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxSearchOptions extends HBox {

	private Label lblMenu = new Label("Menu");
	private Button searchBtn = new Button("Rechercher");
	private Button addBtn = new Button("Ajouter");
	private Button creationbtn = new Button("Création annuaire");
	private Button updateBtn = new Button("Mettre à jour");
	private Button deleteBtn = new Button("Supprimer");

	public HBoxSearchOptions() {
		super();	
		setSpacing(10);
		setPadding(new Insets(10.));
		setMargin(updateBtn, new Insets(0, 10, 0, 50));
		getStylesheets().add(getClass().getResource("./style.css").toExternalForm());

		searchBtn.setPrefSize(150, 100);
		searchBtn.setId("btnmenu");
		addBtn.setPrefSize(150, 100);
		addBtn.setId("btnmenu");
		creationbtn.setPrefSize(150, 100);
		creationbtn.setId("btnmenu");
		updateBtn.setPrefSize(150, 100);
		updateBtn.setId("btnmenu");
		deleteBtn.setPrefSize(150, 100);
		deleteBtn.setId("btnmenu");
		lblMenu.setId("menu");
		
		

		getChildren().addAll(lblMenu, creationbtn, searchBtn, addBtn, updateBtn, deleteBtn);

		addBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				VBoxAddSearch vBoxAddProfile = new VBoxAddSearch();
				MainPannel root = (MainPannel) getScene().getRoot();
				vBoxAddProfile.getTitle().setText("AJOUTER UN STAGIAIRE");
				vBoxAddProfile.getBottomPane().getChildren().addAll(vBoxAddProfile.getAddBtn(), vBoxAddProfile.getResetBtn());
				root.setLeft(vBoxAddProfile);
			}
		});

		//Instancie une VBox avec l'ensemble des champs nécessaire pour une recherche
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				VBoxAddSearch searchBox = new VBoxAddSearch();
				MainPannel root = (MainPannel) getScene().getRoot();

				searchBox.getTitle().setText("RECHERCHER UN STAGIAIRE");
				searchBox.getBottomPane().getChildren().addAll(searchBox.getSearchBtn(), searchBox.getResetBtn());
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

				if (root.getLeft() != null) {
					VBoxAddSearch vboxOption = (VBoxAddSearch) root.getLeft();
					if(vboxOption.getTitle().getText() == "AJOUTER UN STAGIAIRE") {
						vboxOption.getTitle().setText("MODIFIER UN STAGIAIRE");
						vboxOption.getBottomPane().getChildren().remove(0);
						vboxOption.getBottomPane().getChildren().remove(0);
						vboxOption.getBottomPane().getChildren().addAll(vboxOption.getUpdateBtn(), vboxOption.getResetBtn());
					}else if (vboxOption.getTitle().getText() == "RECHERCHER UN STAGIAIRE") {
						vboxOption.getTitle().setText("MODIFIER UN STAGIAIRE");
						vboxOption.getBottomPane().getChildren().remove(0);
						vboxOption.getBottomPane().getChildren().remove(0);
						vboxOption.getBottomPane().getChildren().addAll(vboxOption.getUpdateBtn(), vboxOption.getResetBtn());
					}
				}else {
					VBoxAddSearch vboxOption = new VBoxAddSearch();
					vboxOption.getTitle().setText("MODIFIER UN STAGIAIRE");
					vboxOption.getBottomPane().getChildren().addAll(vboxOption.getUpdateBtn(), vboxOption.getResetBtn());
					root.setLeft(vboxOption);
				}
			}
		});

		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MainPannel root = (MainPannel) getScene().getRoot();
				TableViewInternProfiles tableView = (TableViewInternProfiles) root.getCenter();
                InternProfile profile = tableView.getTableView().getSelectionModel().getSelectedItem();
                tableView.getObservableProfiles().remove(profile);
                InternProfileDao dao = new InternProfileDao();
                dao.deleteInternProfile(profile);
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

}
