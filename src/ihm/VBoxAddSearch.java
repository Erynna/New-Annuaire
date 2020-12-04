package ihm;

import java.util.List;

import model.InternProfile;
import model.InternProfileComparator;
import model.InternProfileDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxAddSearch extends VBox {

	private Label title = new Label();
	private VBox bottomPane = new VBox();
	private Label labelSurname = new Label("Nom");
	private TextField textFieldSurname = new TextField("");
	private Label labelFirstName = new Label("Prénom");
	private TextField textFieldFirstName = new TextField("");
	private Label labelCounty = new Label("Département");
	private TextField textFieldCounty = new TextField("");
	private Label labelPromotion = new Label("Promotion");
	private TextField textFieldPromotion = new TextField("");
	private Label labelYearStudy = new Label("Année");
	private ComboBox<Integer> cbYearStudy = new ComboBox<Integer>();
	private Button searchBtn = new Button("Rechercher");
	private Button resetBtn = new Button("Réinitialiser les champs");
	private Button addBtn = new Button("Ajouter");
	private Button deleteBtn = new Button("Supprimer");
	private Button updateBtn= new Button("Actualiser");
	private VBox vbAdmin;
	private Stage popUpWindow;

	public VBoxAddSearch() {
	
		for (int i = 2000; i < 2051; i++) {
			cbYearStudy.getItems().add(i);
		}

		getChildren().add(title);
		getChildren().addAll(labelSurname, textFieldSurname);
		getChildren().addAll(labelFirstName, textFieldFirstName);
		getChildren().addAll(labelCounty, textFieldCounty);
		getChildren().addAll(labelPromotion, textFieldPromotion);
		getChildren().addAll(labelYearStudy, cbYearStudy);
		getChildren().add(bottomPane);

		setSpacing(10);
		setPadding(new Insets(5));

		VBox.setMargin(searchBtn, new Insets(50, 0, 20, 0));
		VBox.setMargin(addBtn, new Insets(50, 0, 20, 0));
		VBox.setMargin(updateBtn, new Insets(50, 0, 20, 0));
		bottomPane.setAlignment(Pos.CENTER);

		title.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
		textFieldSurname.setPrefWidth(200);
		searchBtn.setPrefSize(150, 30);
		updateBtn.setPrefSize(150, 30);
		addBtn.setPrefSize(150, 30);
		resetBtn.setPrefSize(150, 30);
		cbYearStudy.setVisibleRowCount(5);

		addBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!textFieldSurname.getText().trim().equals("") && !textFieldFirstName.getText().trim().equals("") && !textFieldCounty.getText().trim().equals("") && !textFieldPromotion.getText().trim().equals("") && cbYearStudy.getValue() != null) {
					String surname = textFieldSurname.getText().toUpperCase().trim();
					String firstname = textFieldFirstName.getText().substring(0,1).toUpperCase() + textFieldFirstName.getText().substring(1).toLowerCase().trim();
					String county = textFieldCounty.getText().toUpperCase().trim();
					String promotion = textFieldPromotion.getText().toUpperCase().trim();
					int studyYear = cbYearStudy.getValue();				
					
					InternProfile internProfile = new InternProfile(surname, firstname, county, promotion, studyYear);
					
					boolean canSave = true;
					
					MainPannel root = (MainPannel) getScene().getRoot();
					InternProfileDao dao = new InternProfileDao();
					TableViewInternProfiles tableviewIP = new TableViewInternProfiles(dao.getAll());
	                ObservableList<InternProfile> observableProfiles = tableviewIP.getObservableProfiles();
					
					for (InternProfile observableProfile : observableProfiles) {								
						InternProfileComparator internProfileComparator = new InternProfileComparator();
						if(internProfileComparator.compare(internProfile, observableProfile) == 0) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("MESSAGE D'ALERTE");
							alert.setHeaderText("ATTENTION DOUBLON !");
							alert.setContentText("Vous ne pouvez pas ajouter ce stagiaire car il existe déjà.");
							alert.showAndWait();
							canSave = false;
							break;
						}
					}if(canSave) {
						TableViewInternProfiles tableView = (TableViewInternProfiles) root.getCenter();
						tableView.getObservableProfiles().add(internProfile);
						FXCollections.sort(tableView.getObservableProfiles(), new InternProfileComparator());											
		                dao.addInternProfile(internProfile);
					} 
				}else if (textFieldSurname.getText().trim().equals("")|| textFieldFirstName.getText().trim().equals("") || textFieldCounty.getText().trim().equals("") || textFieldPromotion.getText().trim().equals("") || cbYearStudy.getValue() == null) {
					if(textFieldSurname.getText().trim().equals("")) {
						textFieldSurname.setPromptText("Veuillez entrer un nom");
						textFieldSurname.setStyle("-fx-prompt-text-fill: red");
					}
					if(textFieldFirstName.getText().trim().equals("")) {
						textFieldFirstName.setPromptText("Veuillez entrer un prénom");
						textFieldFirstName.setStyle("-fx-prompt-text-fill: red");
					}
					if(textFieldCounty.getText().trim().equals("")) {
						textFieldCounty.setPromptText("Veuillez entrer un département");
						textFieldCounty.setStyle("-fx-prompt-text-fill: red");
					}
					if(textFieldPromotion.getText().trim().equals("")) {
						textFieldPromotion.setPromptText("Veuillez entrer une promotion");
						textFieldPromotion.setStyle("-fx-prompt-text-fill: red");
					}
					if(cbYearStudy.getValue() == null) {
						cbYearStudy.setPromptText("Veuillez entrer une année");						
					}			
					Alert alertMissingInfo = new Alert(AlertType.WARNING);
					alertMissingInfo.setTitle("MESSAGE D'ALERTE");
					alertMissingInfo.setHeaderText("INFORMATION(S) MANQUANTE(S)");
					alertMissingInfo.setContentText("Tous les champs doivent être renseignés");
					alertMissingInfo.showAndWait();	
				}
			}
		});
		
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MainPannel root = (MainPannel) getScene().getRoot();
				InternProfileDao dao = new InternProfileDao();

				int checkBoxYearStudy = 0;
				if (cbYearStudy.getValue() != null) {
					checkBoxYearStudy = cbYearStudy.getValue();
				}
				List<InternProfile> filteredProfiles = dao.filterSearchInternProfile(textFieldSurname.getText(), textFieldFirstName.getText(), textFieldCounty.getText(), textFieldPromotion.getText(), checkBoxYearStudy);
				TableViewInternProfiles tableViewFiltered = new TableViewInternProfiles(filteredProfiles);
				root.setCenter(tableViewFiltered);
			}
		});
		
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {	
				MainPannel root = (MainPannel) getScene().getRoot();
				InternProfileDao dao = new InternProfileDao();
				TableViewInternProfiles tableviewScreen = (TableViewInternProfiles) root.getCenter();

				if(tableviewScreen.getTableView().getSelectionModel().getSelectedItem() == null) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message d'alerte");
					alert.setHeaderText("Aucun stagiaire sélectionné");
					alert.setContentText("Veuillez sélectionner le stagiaire à modifier");
					alert.showAndWait();
				}else {
					VBoxAddSearch optionVbox = (VBoxAddSearch) root.getLeft();
					InternProfile oldIp = tableviewScreen.getTableView().getSelectionModel().getSelectedItem();
					InternProfile newIp = new InternProfile(optionVbox.getTextFieldSurname().getText(), optionVbox.getTextFieldFirstName().getText(), optionVbox.getTextFieldCounty().getText(), optionVbox.getTextFieldPromotion().getText(), optionVbox.getCbYearStudy().getValue());
					dao.modifyInternProfile(oldIp, newIp);
					tableviewScreen.getObservableProfiles().remove(oldIp);
					tableviewScreen.getObservableProfiles().add(newIp);
					FXCollections.sort(tableviewScreen.getObservableProfiles(), new InternProfileComparator());

				}
			}
		});

		resetBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MainPannel root = (MainPannel) getScene().getRoot();
				getTextFieldSurname().setText("");
				getTextFieldFirstName().setText("");
				getTextFieldCounty().setText("");
				getTextFieldPromotion().setText("");
				getCbYearStudy().setValue(null);
				InternProfileDao dao = new InternProfileDao();
				root.setCenter(new TableViewInternProfiles(dao.getAll()));
			}
		});
		setSpacing(5.);
	}
	
	public Label getTitle() {
		return title;
	}
	public void setTitle(Label title) {
		this.title = title;
	}
	public VBox getBottomPane() {
		return bottomPane;
	}
	public void setBottomPane(VBox bottomPane) {
		this.bottomPane = bottomPane;
	}
	public Label getLabelSurname() {
		return labelSurname;
	}
	public void setLabelSurname(Label labelSurname) {
		this.labelSurname = labelSurname;
	}
	public TextField getTextFieldSurname() {
		return textFieldSurname;
	}
	public void setTextFieldSurname(TextField textFieldSurname) {
		this.textFieldSurname = textFieldSurname;
	}
	public Label getLabelFirstName() {
		return labelFirstName;
	}
	public void setLabelFirstName(Label labelFirstName) {
		this.labelFirstName = labelFirstName;
	}
	public TextField getTextFieldFirstName() {
		return textFieldFirstName;
	}
	public void setTextFieldFirstName(TextField textFieldFirstName) {
		this.textFieldFirstName = textFieldFirstName;
	}
	public Label getLabelCounty() {
		return labelCounty;
	}
	public void setLabelCounty(Label labelCounty) {
		this.labelCounty = labelCounty;
	}
	public TextField getTextFieldCounty() {
		return textFieldCounty;
	}
	public void setTextFieldCounty(TextField textFieldCounty) {
		this.textFieldCounty = textFieldCounty;
	}
	public Label getLabelPromotion() {
		return labelPromotion;
	}
	public void setLabelPromotion(Label labelPromotion) {
		this.labelPromotion = labelPromotion;
	}
	public TextField getTextFieldPromotion() {
		return textFieldPromotion;
	}
	public void setTextFieldPromotion(TextField textFieldPromotion) {
		this.textFieldPromotion = textFieldPromotion;
	}
	public Label getLabelYearStudy() {
		return labelYearStudy;
	}
	public void setLabelYearStudy(Label labelYearStudy) {
		this.labelYearStudy = labelYearStudy;
	}
	public ComboBox<Integer> getCbYearStudy() {
		return cbYearStudy;
	}
	public void setCbYearStudy(ComboBox<Integer> cbYearStudy) {
		this.cbYearStudy = cbYearStudy;
	}
	public Button getSearchBtn() {
		return searchBtn;
	}
	public void setSearchBtn(Button searchBtn) {
		this.searchBtn = searchBtn;
	}
	public Button getResetBtn() {
		return resetBtn;
	}
	public void setResetBtn(Button resetBtn) {
		this.resetBtn = resetBtn;
	}
	public Button getAddBtn() {
		return addBtn;
	}
	public void setAddBtn(Button addBtn) {
		this.addBtn = addBtn;
	}
	public Button getDeleteBtn() {
		return deleteBtn;
	}
	public void setDeleteBtn(Button deleteBtn) {
		this.deleteBtn = deleteBtn;
	}
	public Button getUpdateBtn() {
		return updateBtn;
	}
	public void setUpdateBtn(Button updateBtn) {
		this.updateBtn = updateBtn;
	}
	public VBox getVbAdmin() {
		return vbAdmin;
	}
	public void setVbAdmin(VBox vbAdmin) {
		this.vbAdmin = vbAdmin;
	}
	public Stage getPopUpWindow() {
		return popUpWindow;
	}
	public void setPopUpWindow(Stage popUpWindow) {
		this.popUpWindow = popUpWindow;
	}
}
