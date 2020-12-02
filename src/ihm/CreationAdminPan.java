package ihm;

import model.AdminUser;
import model.AdminUserDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreationAdminPan extends VBox {

	//M�thode permettant la cr�ation d'un compte admin et l'enregistrement dans un fichier csv

	private Label lblLogin;
	private TextField tfLogin;
	private Label lblPassword;
	private TextField tfPassword;
	private Button btnCreate;
	private HBox hbLog;
	private HBox hbPass;
	private HBox hbBtn;
	private Label lblError;
	//	private AdminUserDao;   //cr�ation d'une dao � faire

	public CreationAdminPan() {
		setPrefSize(600, 200);

		hbLog = new HBox();
		lblLogin = new Label("Choisissez un login : ");
		lblLogin.setPrefWidth(200);
		tfLogin = new TextField();
		tfLogin.setPrefWidth(300);
		hbLog.getChildren().addAll(lblLogin, tfLogin);
		hbLog.setAlignment(Pos.CENTER);
		hbLog.setPadding(new Insets(5.));

		hbPass = new HBox();
		lblPassword = new Label("Choisissez un mot de passe : ");
		lblPassword.setPrefWidth(200);
		tfPassword = new TextField();
		tfPassword.setPrefWidth(300);
		hbPass.getChildren().addAll(lblPassword, tfPassword);
		hbPass.setAlignment(Pos.CENTER);
		hbPass.setPadding(new Insets(5.));

		hbBtn = new HBox();
		btnCreate = new Button("OK");
		btnCreate.setPrefSize(100, 50);
		hbBtn.getChildren().addAll(btnCreate);
		hbBtn.setAlignment(Pos.BOTTOM_CENTER);
		hbBtn.setPadding(new Insets(5.));

		getChildren().addAll(hbLog, hbPass, hbBtn);
		setPadding(new Insets(15.));

		//Création d'un compte		//OK
		btnCreate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String login = tfLogin.getText();
				String password = tfPassword.getText();
	//			AdminUser admin = new AdminUser(login, password);		//Utilit�  ??
				AdminUserDao dao =  new AdminUserDao();
				
				//		Apparition d'un message d'erreur si login déjà existant	
				if(dao.checkLoginExistence(login)){
					lblError = new Label("Login déjà existant");
					getChildren().add(lblError);
				}else {
					dao.addAdminAccount(login, password);
					
				}
			}
		});


	}


	public Label getLblLogin() {
		return lblLogin;
	}
	public void setLblLogin(Label lblLogin) {
		this.lblLogin = lblLogin;
	}
	public TextField getTfLogin() {
		return tfLogin;
	}
	public void setTfLogin(TextField tfLogin) {
		this.tfLogin = tfLogin;
	}
	public Label getLblPassword() {
		return lblPassword;
	}
	public void setLblPassword(Label lblPassword) {
		this.lblPassword = lblPassword;
	}
	public TextField getTfPassword() {
		return tfPassword;
	}
	public void setTfPassword(TextField tfPassword) {
		this.tfPassword = tfPassword;
	}
	public Button getBtnCreate() {
		return btnCreate;
	}
	public void setBtnCreate(Button btnCreate) {
		this.btnCreate = btnCreate;
	}


}
