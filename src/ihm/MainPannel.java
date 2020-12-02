package ihm;

import java.io.File;
import model.InternProfileDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/*
 * Cette classe correspond au panneau principal regroupant le tableview et les boutons d'actions
 */

public class MainPannel extends BorderPane {
		
	private TableViewInternProfiles tableViewInternProfiles;
	private HBoxSearchOptions hbSearchOptions = new HBoxSearchOptions();
	private Label lblTV = new Label("L'annuaire n'existe pas");
	private Button btnR = new Button("Rafraichir");
	
	public MainPannel() {
		super();
		setPrefSize(1000, 700);
		setTop(hbSearchOptions);
		setBottom(btnR);
		
		//Ajout du TableView
		File annuaire = new File("./internBDD.bin");
		if(annuaire.exists()) {
			InternProfileDao dao = new InternProfileDao();
			tableViewInternProfiles = new TableViewInternProfiles(dao.getAll());
			setCenter(tableViewInternProfiles);
		}else {
			setCenter(lblTV);
		}
		
		btnR.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(annuaire.exists()) {
					InternProfileDao dao = new InternProfileDao();
					tableViewInternProfiles = new TableViewInternProfiles(dao.getAll());
					setCenter(tableViewInternProfiles);
				}
			}
		});
	}

	public TableViewInternProfiles getTableViewInternProfiles() {
		return tableViewInternProfiles;
	}
	public void setTableViewInternProfiles(TableViewInternProfiles tableViewInternProfiles) {
		this.tableViewInternProfiles = tableViewInternProfiles;
	}
	public HBoxSearchOptions getHbSearchOptions() {
		return hbSearchOptions;
	}
	public void setHbSearchOptions(HBoxSearchOptions hbSearchOptions) {
		this.hbSearchOptions = hbSearchOptions;
	}
	public Label getLblTV() {
		return lblTV;
	}
	public void setLblTV(Label lblTV) {
		this.lblTV = lblTV;
	}
	public Button getBtnR() {
		return btnR;
	}
	public void setBtnR(Button btnR) {
		this.btnR = btnR;
	}
	
}
