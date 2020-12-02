package ihm;

import java.util.List;
import model.InternProfile;
import model.InternProfileDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class TableViewInternProfiles extends AnchorPane {

	private ObservableList<InternProfile> observableProfiles;
	private TableView<InternProfile> tableView;
	private List<InternProfile> profiles;
	private InternProfile internProfile;

	@SuppressWarnings("unchecked")
	public TableViewInternProfiles(List<InternProfile> internProfiles) {
		super();
	
		ObservableList<InternProfile> observableProfiles = FXCollections.observableArrayList(internProfiles);
		
		TableView<InternProfile> tableView = new TableView<InternProfile>(observableProfiles);

		TableColumn<InternProfile, String> colSurName = new TableColumn<InternProfile, String>("Nom");
		colSurName.setCellValueFactory(new PropertyValueFactory<InternProfile, String>("surname"));
		TableColumn<InternProfile, String> colFirstName = new TableColumn<InternProfile, String>("Prénom");
		colFirstName.setCellValueFactory(new PropertyValueFactory<InternProfile, String>("firstName"));
		TableColumn<InternProfile, String> colCounty = new TableColumn<InternProfile, String>("Département");
		colCounty.setCellValueFactory(new PropertyValueFactory<InternProfile, String>("county"));
		TableColumn<InternProfile, String> colPromotion = new TableColumn<InternProfile, String>("Promotion");
		colPromotion.setCellValueFactory(new PropertyValueFactory<InternProfile, String>("promotion"));
		TableColumn<InternProfile, Integer> colStudyYear = new TableColumn<InternProfile, Integer>("Année");
		colStudyYear.setCellValueFactory(new PropertyValueFactory<InternProfile, Integer>("studyYear"));

		tableView.getColumns().addAll(colSurName, colFirstName, colCounty, colPromotion, colStudyYear);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


		getChildren().add(tableView);
		setPrefSize(800, 500);

		AnchorPane.setBottomAnchor(tableView, 5.);
		AnchorPane.setLeftAnchor(tableView, 5.);
		AnchorPane.setRightAnchor(tableView, 5.);
		AnchorPane.setTopAnchor(tableView, 5.);
	}

	public ObservableList<InternProfile> getObservableProfiles() {
		return observableProfiles;
	}
	public void setObservableProfiles(ObservableList<InternProfile> observableProfiles) {
		this.observableProfiles = observableProfiles;
	}
	public TableView<InternProfile> getTableView() {
		return tableView;
	}
	public void setTableView(TableView<InternProfile> tableView) {
		this.tableView = tableView;
	}
	public List<InternProfile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<InternProfile> profiles) {
		this.profiles = profiles;
	}
	public InternProfile getInternProfile() {
		return internProfile;
	}
	public void setInternProfile(InternProfile internProfile) {
		this.internProfile = internProfile;
	}

}