package model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AdminUserDao {

	private String loginBDDPath = "./login.bin";
	private File loginsBDD = new File(loginBDDPath);
	private long pointerPosition = 0;						//Variable de classe nécessaire afin de stocker l'information de positionnement du RandomAccessFile renvoyé par checkLoginExistence et utilisée dans collectPassword

	/*
	 * Méthode qui permet l'ajout d'un nouvel "Administrateur" possédant un identifiant et un mot de passe personnalisé
	 */
	public void addAdminAccount(String login, String password) {

		RandomAccessFile raf = null;

		try {

			loginsBDD.createNewFile();

			raf = new RandomAccessFile(loginsBDD, "rw");

			raf.seek(getFileLength(raf));

			if(!checkLoginExistence(login)) {
				raf.writeUTF(login);
				raf.writeUTF(password);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Méthode qui renvoie le mot de passe administrateur dans le cas où l'identifiant existe bien dans la BDD, et qui retourne "" dans le cas contraire
	 */
	public String collectPassword(String login) {

		String password = "";
		RandomAccessFile raf = null;

		try {

			raf = new RandomAccessFile(loginsBDD, "rw");

			if (checkLoginExistence(login)) {

				raf.seek(pointerPosition);
				password = raf.readUTF();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return password;
	}

	/*
	 * Méthode qui renvoie "true" si le login existe dans la BDD et "false" dans le cas contraire
	 */
	public boolean checkLoginExistence(String login) {

		boolean loginExistence = false;
		RandomAccessFile raf = null;
		String loginToCompare ="";
		int fileLength = 0;

		try {

			raf = new RandomAccessFile(loginsBDD, "rw");
			fileLength = getFileLength(raf);

			if (fileLength > 0) {
				do {

					loginToCompare = raf.readUTF();

					if(loginToCompare.equals(login)) {

						pointerPosition = raf.getFilePointer();					//Enregistrement de la position du pointeur dans une variable de classe afin de pouvoir utiliser l'information à l'extérieur (récupération mdp)
						loginExistence = true;
						break;

					}else {
						raf.readUTF();
					}

				}while(raf.getFilePointer() < fileLength);						//Bouclage sur la totalité du fichier
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return loginExistence;
	}

	/*
	 * Méthode qui permet de récupérer la taille des données àl'intérieur de notre fichier
	 */
	private int getFileLength(RandomAccessFile raf) {

		int position = 0;

		try {

			raf.seek(0);

			while(raf.readLine() != null) {

				position = (int) raf.getFilePointer();

			}

			raf.seek(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return position;
	}

	/*
	 * Méthode qui permet de renvoyer un booleen true si les informations de connexion : login et password sont corrects, false sinon.
	 */
	public boolean connexion(AdminUser admin) {

		boolean connexion = false;

		if(admin.getPassword().equals(collectPassword(admin.getLogin()))) {
			connexion = true;
		}

		return connexion;

	}
}