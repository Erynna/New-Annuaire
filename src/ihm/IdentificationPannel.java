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

/*
 * Ce panneau permet de se connecter en tant qu'utilisateur ou administrateur. Il est possible �galement de cr�er un compte administrateur.
 */

public class IdentificationPannel extends VBox {
	private Label lblLogin;
	private TextField tfLogin;
	private HBox hbLog;
	private Label lblPassword;
	private TextField tfPassword;
	private HBox hbPass;
	private Button btnLambdaUser;
	private Button btnConnexion;
	private Button btnCreationAdmin;
	private HBox hbButton;
	private Label error;
	private Button deleteBtn;
	private Button updateBtn;
	private HBox hbAdmin;
	
	public IdentificationPannel() {
		super();
		
		hbLog = new HBox();
		lblLogin = new Label("Login : ");
		lblLogin.setPrefWidth(150);
		tfLogin = new TextField();
		tfLogin.setPrefWidth(300);
		hbLog.getChildren().addAll(lblLogin, tfLogin);
		hbLog.setAlignment(Pos.TOP_CENTER);
		hbLog.setPadding(new Insets(5.));
		
		hbPass = new HBox();
		lblPassword = new Label("Mot de passe : ");
		lblPassword.setPrefWidth(150);
		tfPassword = new TextField();
		tfPassword.setPrefWidth(300);
		hbPass.getChildren().addAll(lblPassword, tfPassword);
		hbPass.setAlignment(Pos.TOP_CENTER);
		hbPass.setPadding(new Insets(5.));	
		
		hbButton = new HBox();
		btnConnexion = new Button("Se connecter");
		btnConnexion.setPrefSize(150, 100);
		btnLambdaUser = new Button("Utilisateur");
		btnLambdaUser.setPrefSize(150, 100);
		btnCreationAdmin = new Button("Créer un compte" + "\n   administrateur");
		btnCreationAdmin.setPrefSize(150, 100);
		hbButton.getChildren().addAll(btnConnexion, btnCreationAdmin, btnLambdaUser);
		hbButton.setAlignment(Pos.BOTTOM_CENTER);
		hbButton.setSpacing(15);
		
		getChildren().addAll(hbLog, hbPass, hbButton);
		setPrefSize(600, 200);
		getStylesheets().add(getClass().getResource("./style.css").toExternalForm());
		
		//Fonctinnalités administrateurs
		deleteBtn = new Button("Supprimer");
		deleteBtn.setPrefSize(100, 30);
		updateBtn = new Button("MàJ");
		updateBtn.setPrefSize(100, 30);
		hbAdmin = new HBox();
		hbAdmin.getChildren().addAll(deleteBtn, updateBtn);
		
		error = new Label("Login et/ou mot de passe incorrect");
		error.setId("alertMessage");
		
		/*
		 * Cette action permet de se connecter en tant qu'administrateur et de pouvoir acc�der � plus de fonctionnalit�s
		 */
		btnConnexion.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String lg = tfLogin.getText().trim();
				String ps = tfPassword.getText().trim();
				AdminUser admin = new AdminUser(lg, ps); 	
				AdminUserDao dao = new AdminUserDao();
				if(lg.length() != 0 && ps.length() != 0) {
					boolean verification = false;
					verification = dao.connexion(admin);
					//Si login et mot de passe correct
					if (verification) {
						MainPannel root = new MainPannel();
			//Ajout Kevin
						root.getHbSearchOptions().getChildren().add(hbAdmin);
						Scene scene = new Scene(root);
						Stage stage = (Stage) getScene().getWindow();
						stage.setTitle("Annuaire");
						stage.setScene(scene);
					//Sinon affichage d'un message d'erreur
					}else {
						getChildren().add(error);
					}
				}else {
					getChildren().add(error);
				}
			}
		});
		
		/*
		 * Cette action permet l'ouverture d'une nouvelle fenetre afin de creer un compte administrateur
		 */
		btnCreationAdmin.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				CreationAdminPan root = new CreationAdminPan();
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle("Création d'un compte administrateur");
				stage.setScene(scene);
				stage.show();
			}
		});
		
		/*
		 * Cette action permet la connection au logiciel en tant qu'utilisateur
		 */
		btnLambdaUser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MainPannel root = new MainPannel();
				Scene scene = new Scene(root);
				Stage stage = (Stage) getScene().getWindow();
				stage.setTitle("Annuaire");
				stage.setScene(scene);
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
	public Button getBtnLambdaUser() {
		return btnLambdaUser;
	}
	public void setBtnLambdaUser(Button btnLambdaUser) {
		this.btnLambdaUser = btnLambdaUser;
	}
	public Button getBtnConnexion() {
		return btnConnexion;
	}
	public void setBtnConnexion(Button btnConnexion) {
		this.btnConnexion = btnConnexion;
	}
	public HBox getHbLog() {
		return hbLog;
	}
	public void setHbLog(HBox hbLog) {
		this.hbLog = hbLog;
	}
	public HBox getHbPass() {
		return hbPass;
	}
	public void setHbPass(HBox hbPass) {
		this.hbPass = hbPass;
	}
	public Button getBtnCreationAdmin() {
		return btnCreationAdmin;
	}
	public void setBtnCreationAdmin(Button btnCreationAdmin) {
		this.btnCreationAdmin = btnCreationAdmin;
	}
	public HBox getHbButton() {
		return hbButton;
	}
	public void setHbButton(HBox hbButton) {
		this.hbButton = hbButton;
	}
	public Label getError() {
		return error;
	}
	public void setLabel(Label error) {
		this.error = error;
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



	public HBox getHbAdmin() {
		return hbAdmin;
	}



	public void setHbAdmin(HBox hbAdmin) {
		this.hbAdmin = hbAdmin;
	}
	

}
