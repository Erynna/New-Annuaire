package ihm;

import java.util.List;

import model.InternProfile;
import model.InternProfileDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class VBoxSearchOptions extends VBox {

	private Label title = new Label();
	private VBox bottomPane = new VBox();
	private Label labelSurname = new Label("Nom");
	private TextField textFieldSurname = new TextField(null);
	private Label labelFirstName = new Label("Prénom");
	private TextField textFieldFirstName = new TextField(null);
	private Label labelCounty = new Label("Département");
	private TextField textFieldCounty = new TextField(null);
	private Label labelPromotion = new Label("Promotion");
	private TextField textFieldPromotion = new TextField(null);
	private Label labelYearStudy = new Label("Année");
	private ComboBox<Integer> cbYearStudy = new ComboBox<Integer>();
	private Button searchBtn = new Button("Rechercher");
	private Button resetBtn = new Button("Réinitialiser les champs");
	private Button addBtn = new Button("Ajouter");
	private Button deleteBtn = new Button("Supprimer");
	private Button updateBtn= new Button("Actualiser");
	private VBox vbAdmin;

	public VBoxSearchOptions() {
		super();

		for (int i = 2000; i < 2051; i++) {
			cbYearStudy.getItems().add(i);
		}

		bottomPane.getChildren().addAll(searchBtn, resetBtn);
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
		bottomPane.setAlignment(Pos.CENTER);

		title.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
		textFieldSurname.setPrefWidth(200);
		searchBtn.setPrefSize(150, 30);
		resetBtn.setPrefSize(150, 30);
		cbYearStudy.setVisibleRowCount(5);

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

	public Label getTitle() {
		return title;
	}

	public void setTitle(Label title) {
		this.title = title;
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

}
