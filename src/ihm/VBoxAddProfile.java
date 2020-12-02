package ihm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.InternProfile;
import model.InternProfileComparator;
import model.InternProfileDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class VBoxAddProfile extends VBox {

	private Label title = new Label();
	private VBox bottomPane = new VBox();
	private Label labelSurname = new Label("Nom");;
	private TextField textFieldSurname = new TextField(null);
	private Label labelFirstName = new Label("Prénom");
	private TextField textFieldFirstName  = new TextField(null);
	private Label labelCounty = new Label("Département");
	private TextField textFieldCounty = new TextField(null);
	private Label labelPromotion = new Label("Promotion");
	private TextField textFieldPromotion = new TextField(null);
	private Label labelYearStudy = new Label("Année");
	private ComboBox<Integer> cbYearStudy = new ComboBox<Integer>();
	private Button addBtn = new Button("Ajouter");
	private Button resetBtn = new Button("Réinitialiser les champs");
	private Stage popUpWindow;


	public VBoxAddProfile() {
		super();

		InternProfileDao dao = new InternProfileDao();
		ObservableList<InternProfile> observableProfiles = FXCollections.observableArrayList(dao.getAll());

		for (int i = 2000; i < 2051; i++) {
			cbYearStudy.getItems().add(i);
		}
		bottomPane.getChildren().addAll(addBtn, resetBtn);
		getChildren().add(title);
		getChildren().addAll(labelSurname, textFieldSurname);
		getChildren().addAll(labelFirstName, textFieldFirstName);
		getChildren().addAll(labelCounty, textFieldCounty);
		getChildren().addAll(labelPromotion, textFieldPromotion);
		getChildren().addAll(labelYearStudy, cbYearStudy);
		getChildren().add(bottomPane);

		setSpacing(10);
		setPadding(new Insets(5));

		VBox.setMargin(addBtn, new Insets(50, 0, 20, 0));
		bottomPane.setAlignment(Pos.CENTER);

		title.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
		textFieldSurname.setPrefWidth(200);
		addBtn.setPrefSize(150, 30);
		resetBtn.setPrefSize(150, 30);
		cbYearStudy.setVisibleRowCount(5);

		addBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {			
				//emptyFieldsTests();
			
				if (textFieldSurname != null && textFieldFirstName != null && textFieldCounty != null && textFieldPromotion != null && cbYearStudy != null) {
					String surname = textFieldSurname.getText().toUpperCase();
					String firstname = textFieldFirstName.getText().substring(0,1).toUpperCase() + textFieldFirstName.getText().substring(1).toLowerCase();
					String county = textFieldCounty.getText().toUpperCase();
					String promotion = textFieldPromotion.getText().toUpperCase();
					int studyYear = cbYearStudy.getValue();


					InternProfile internProfile = new InternProfile(surname, firstname, county, promotion, studyYear);

					boolean canSave = true;
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
						observableProfiles.add(internProfile);
						dao.addInternProfile(internProfile);				
					} 
				}else if (textFieldSurname.getLength() == 0 || textFieldFirstName.getLength() == 0 || textFieldCounty.getLength() == 0 || textFieldPromotion.getLength() == 0 || cbYearStudy == null) {
					if(textFieldSurname.getText() == null) {
						textFieldSurname.setPromptText("Veuillez entrer un nom");
						textFieldSurname.setStyle("-fx-prompt-text-fill: red");
					}
					if(textFieldFirstName.getText() == null) {
						textFieldFirstName.setPromptText("Veuillez entrer un prénom");
						textFieldFirstName.setStyle("-fx-prompt-text-fill: red");
					}
					if(textFieldCounty.getText() == null) {
						textFieldCounty.setPromptText("Veuillez entrer un département");
						textFieldCounty.setStyle("-fx-prompt-text-fill: red");
					}
					if(textFieldPromotion.getText() == null) {
						textFieldPromotion.setPromptText("Veuillez entrer une promotion");
						textFieldPromotion.setStyle("-fx-prompt-text-fill: red");
					}
					if(cbYearStudy.getValue() == null) {
						cbYearStudy.setPromptText("Veuillez entrer une année");
						cbYearStudy.setStyle("-fx-prompt-text-fill: red");
					}			
					Alert alertMissingInfo = new Alert(AlertType.WARNING);
					alertMissingInfo.setTitle("MESSAGE D'ALERTE");
					alertMissingInfo.setHeaderText("INFORMATION(S) MANQUANTE(S)");
					alertMissingInfo.setContentText("Tous les champs doivent être renseignés");
					alertMissingInfo.showAndWait();	
				}
			}
		});
		resetBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				MainPannel root = (MainPannel) getScene().getRoot();
				getTextFieldSurname().setText(null);
				getTextFieldFirstName().setText(null);
				getTextFieldCounty().setText(null);
				getTextFieldPromotion().setText(null);
				getCbYearStudy().setValue(null);
				InternProfileDao dao = new InternProfileDao();
				root.setCenter(new TableViewInternProfiles(dao.getAll()));

			}
		});

		setSpacing(5.);
	}

	public void emptyFieldsTests() {
//		if(textFieldSurname.getText() == null) {
//			textFieldSurname.setPromptText("Veuillez entrer un nom");
//			textFieldSurname.setStyle("-fx-prompt-text-fill: red");
//		}
//		if(textFieldFirstName.getText() == null) {
//			textFieldFirstName.setPromptText("Veuillez entrer un prénom");
//			textFieldFirstName.setStyle("-fx-prompt-text-fill: red");
//		}
//		if(textFieldCounty.getText() == null) {
//			textFieldCounty.setPromptText("Veuillez entrer un département");
//			textFieldCounty.setStyle("-fx-prompt-text-fill: red");
//		}
//		if(textFieldPromotion.getText() == null) {
//			textFieldPromotion.setPromptText("Veuillez entrer une promotion");
//			textFieldPromotion.setStyle("-fx-prompt-text-fill: red");
//		}
//		if(cbYearStudy.getValue() == null) {
//			cbYearStudy.setPromptText("Veuillez entrer une année");
//			cbYearStudy.setStyle("-fx-prompt-text-fill: red");
//		}			
//		Alert alertMissingInfo = new Alert(AlertType.WARNING);
//		alertMissingInfo.setTitle("MESSAGE D'ALERTE");
//		alertMissingInfo.setHeaderText("INFORMATION(S) MANQUANTE(S)");
//		alertMissingInfo.setContentText("Tous les champs doivent être renseignés");
//		alertMissingInfo.showAndWait();	
//		
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


	public Button getAddBtn() {
		return addBtn;
	}


	public void setAddBtn(Button addBtn) {
		this.addBtn = addBtn;
	}


	public Button getResetBtn() {
		return resetBtn;
	}


	public void setResetBtn(Button resetBtn) {
		this.resetBtn = resetBtn;
	}


	public Stage getPopUpWindow() {
		return popUpWindow;
	}


	public void setPopUpWindow(Stage popUpWindow) {
		this.popUpWindow = popUpWindow;
	}
}
