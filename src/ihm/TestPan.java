package ihm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestPan extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		IdentificationPannel root = new IdentificationPannel();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
		

	}

	public static void main(String[] args) {
		launch(args);

	}

}
