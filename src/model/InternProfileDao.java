package model;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextField;

/*
 * Classe utilitaire qui permet de consulter, ajouter, supprimer, modifier des stagiaires dans le fichier binaire
 * passé en argument dans le constructeur.
 */
public class InternProfileDao {

	private String internBDD = "./internBDD.bin";
	private final int byteForLeftChild = 0;   			// désigne l'emplacement du 1er byte où est inscrite la position de l'enfant gauche
	private final int numberOfBytesForLeftChild = 4;	// désigne le nombre de bytes réservés pour inscrire la position de l'enfant gauche
	private final int byteForRightChild = 4;  			// désigne l'emplacement du 1er byte où est inscrite la position de l'enfant droit
	private final int numberOfBytesForRightChild = 4;	// désigne le nombre de bytes réservés pour inscrire la position de l'enfant droit
	private final int byteForSurname = 8;     			// désigne l'emplacement du 1er byte où est inscrit le nom du stagiaire
	private final int numberOfBytesForSurname = 21;		// désigne le nombre de bytes réservés pour inscrire le nom de famille
	private final int byteForFirstName = 29;			// désigne l'emplacement du 1er byte où est inscrit le prénom du stagiaire
	private final int numberOfBytesForFirstName = 20;	// désigne le nombre de bytes réservés pour inscrire le prénom
	private final int byteForCounty = 49;				// désigne l'emplacement du 1er byte où est inscrit le département du stagiaire
	private final int numberOfBytesForCounty = 2;		// désigne le nombre de bytes réservés pour inscrire le département du stagiaire
	private final int byteForPromotion = 51;			// désigne l'emplacement du 1er byte où est inscrit le nom de la promotion
	private final int numberOfBytesForPromotion = 10;	// désigne le nombre de bytes réservés pour inscrire la promotion
	private final int byteForStudyYear = 61;			// désigne l'emplacement du 1er byte où est inscrite l'année d'étude
	private final int numberOfBytesForStudyYear = 4;	// désigne le nombre de bytes réservés pour inscrire l'année d'étude

	/*
	 * Pour créer une instance de cette classe, il faut passer en argument dans le constructeur le nom de l'annuaire
	 * créé précédemment dans la classe CréationAnnuaire
	 */
	public InternProfileDao() {
	}

	/*
	 * Méthode qui lit les bytes correspondant à un stagiaire dans le RandomAccessFile passé en argument et 
	 * renvoie un objet InternProfile
	 * @ param : raf=> un RandomAccessFile
	 * 			cursorPosition=> un int qui représente la position où placer le pointeur dans le raf
	 * @ return : un objet InternProfile constitué à partir des informations lues dans le fichier binaire
	 */
	private InternProfile extractDatasIntern(RandomAccessFile raf, int cursorPosition) {

		InternProfile internProfile = null;
		try {
			//récupère le nom de famille
			raf.seek(cursorPosition+byteForSurname);  
			byte[] bytesSurname = new byte[numberOfBytesForSurname];
			for (int i=0;i<numberOfBytesForSurname;i++) {
				bytesSurname[i]=raf.readByte();
			}
			String surname = new String(bytesSurname);
			//récupère le prénom
			raf.seek(cursorPosition+byteForFirstName);
			byte[] bytesFirstName = new byte[numberOfBytesForFirstName];
			for (int i=0;i<numberOfBytesForFirstName;i++) {
				bytesFirstName[i]=raf.readByte();
			}
			String firstName = new String(bytesFirstName);
			//récupère le département
			raf.seek(cursorPosition+byteForCounty);
			byte[] bytesCounty = new byte[numberOfBytesForCounty];
			for (int i=0;i<numberOfBytesForCounty;i++) {
				bytesCounty[i]=raf.readByte();
			}
			String county = new String(bytesCounty);
			//récupère la promotion
			raf.seek(cursorPosition+byteForPromotion);
			byte[] bytesPromotion = new byte[numberOfBytesForPromotion];
			for (int i=0;i<numberOfBytesForPromotion;i++) {
				bytesPromotion[i]=raf.readByte();
			}
			String promotion = new String(bytesPromotion);
			//récupère l'année d'étude
			raf.seek(cursorPosition+byteForStudyYear);
			byte[] bytesStudyYear = new byte[numberOfBytesForStudyYear];
			for (int i=0;i<numberOfBytesForStudyYear;i++) {
				bytesStudyYear[i]=raf.readByte();
			}
			String studyYear = new String(bytesStudyYear);
			internProfile = new InternProfile(surname.trim(), firstName.trim(), county.trim(), promotion.trim(), Integer.parseInt(studyYear));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return internProfile;
	}

	/*
	 * Méthode qui place le curseur à la position passée en argument (cette position doit correspondre 
	 * au 1er byte d'un stagiaire), lit la position de l'enfant gauche du stagiaire et renvoie cette position. 
	 * @ param : raf => un objet RandomAccessFile
	 *           cursorPosition => un int qui désigne la position où placer le pointeur dans le fichier binaire
	 * @ return : un int qui désigne la position dans le fichier de l'enfant gauche. 
	 */	
	private int positionLeftChild(RandomAccessFile raf, int cursorPosition) {
		int positionToCatch=0;
		try {
			raf.seek(cursorPosition+byteForLeftChild);
			positionToCatch = raf.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return positionToCatch;
	}

	/*
	 * Méthode qui place le curseur à la position passée en argument (cette position doit correspondre 
	 * au 1er byte d'un stagiaire), lit la position de l'enfant droit du stagiaire et renvoie cette position. 
	 * @ param : raf => un objet RandomAccessFile
	 *           cursorPosition => un int qui désigne la position où placer le pointeur dans le fichier binaire
	 * @ return : un int qui désigne la position dans le fichier de l'enfant droit. 
	 */
	private int positionRightChild(RandomAccessFile raf, int cursorPosition) {
		int positionToCatch=0;
		try {
			raf.seek(cursorPosition+byteForRightChild);
			positionToCatch = raf.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return positionToCatch;
	}

	/*
	 * Méthode récursive qui lit tout le fichier binaire et ajoute tous les stagiaires à la liste internProfiles
	 * passée en argument.
	 * @ param : raf => un RandomAccesFile
	 * 			 cursurPosition => un int qui représente la position à partir de laquelle lire le fichier binaire
	 * 			 internProfiles => une List<InternProfile> dans laquelle ajouter les stagiaires
	 */
	private void addChildToArray (RandomAccessFile raf, int cursorPosition, List<InternProfile> internProfiles) {
		int posLeftChild = positionLeftChild(raf, cursorPosition);
		int posRightChild = positionRightChild(raf, cursorPosition);

		if (posLeftChild!=0) {
			addChildToArray(raf, posLeftChild, internProfiles);
		}
		internProfiles.add(extractDatasIntern(raf, cursorPosition));
		if (posRightChild!=0) {
			addChildToArray(raf, posRightChild, internProfiles);
		}
	}

	/*
	 * Méthode qui permet d'extraire par ordre alphabétique tous les stagiaires de l'annuaire.
	 * @ return : un objet de type List<InternProfile> qui contient tous les stagiaires de l'annuaire
	 *            classés par ordre alphabétique.
	 */
	public List<InternProfile> getAll() {

		RandomAccessFile raf = null;
		int cursorPosition=0;
		List<InternProfile> internProfiles = new ArrayList<InternProfile>();
		try {
			raf = new RandomAccessFile(internBDD, "r");
			addChildToArray(raf, cursorPosition, internProfiles);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return internProfiles;
	}

	/*
	 * Méthode qui permet de comparer les données passées en argument (surname, firstName, county, promotion, studyYear) avec
	 * les données présentes à partir du cursorPosition
	 * @ param : surname, firstName, county, promotion, studyYear => les données du stagiaire
	 *           cursorPosition : la position du curseur à partir de laquelle lire les données
	 *           raf  : un ojet RandomAccessFile
	 * @ return : un int qui peut être égal à :
	 * 						- 1 si le stagiaire passé en argument en plus grand que le stagiaire à la position cursorPosition
	 * 						- -1 si le stagiaire passé en argument en plus petit que le stagiaire à la position cursorPosition
	 * 						- 0 s'ils sont égaux
	 */
	private int compare(String surname, String firstName, String county, String promotion, int studyYear, int cursorPosition, RandomAccessFile raf) {
		int result=0;
		try {
			//récupère le nom de famille
			raf.seek(cursorPosition+byteForSurname);  
			byte[] bytesSurname = new byte[numberOfBytesForSurname];
			for (int i=0;i<numberOfBytesForSurname;i++) {
				bytesSurname[i]=raf.readByte();
			}
			String surnameRead = new String(bytesSurname);
			//récupère le prénom
			raf.seek(cursorPosition+byteForFirstName);
			byte[] bytesFirstName = new byte[numberOfBytesForFirstName];
			for (int i=0;i<numberOfBytesForFirstName;i++) {
				bytesFirstName[i]=raf.readByte();
			}
			String firstNameRead = new String(bytesFirstName);
			//récupère le département
			raf.seek(cursorPosition+byteForCounty);
			byte[] bytesCounty = new byte[numberOfBytesForCounty];
			for (int i=0;i<numberOfBytesForCounty;i++) {
				bytesCounty[i]=raf.readByte();
			}
			String countyRead = new String(bytesCounty);
			//récupère la promotion
			raf.seek(cursorPosition+byteForPromotion);
			byte[] bytesPromotion = new byte[numberOfBytesForPromotion];
			for (int i=0;i<numberOfBytesForPromotion;i++) {
				bytesPromotion[i]=raf.readByte();
			}
			String promotionRead = new String(bytesPromotion);
			//récupère l'année d'étude
			raf.seek(cursorPosition+byteForStudyYear);
			byte[] bytesStudyYear = new byte[numberOfBytesForStudyYear];
			for (int i=0;i<numberOfBytesForStudyYear;i++) {
				bytesStudyYear[i]=raf.readByte();
			}
			String studyYearRead = new String(bytesStudyYear);
			if (surname.compareTo(surnameRead.trim())>0) {
				result= 1;
			}
			else if (surname.compareTo(surnameRead.trim())<0) {
				result= -1;
			}
			else {
				if (firstName.compareTo(firstNameRead.trim())>0) {
					result= 1;
				}
				else if (firstName.compareTo(firstNameRead.trim())<0) {
					result= -1;
				}
				else {
					if (county.compareTo(countyRead.trim())>0) {
						result= 1;
					}
					else if (county.compareTo(countyRead.trim())<0) {
						result= -1;
					}
					else {
						if (promotion.compareTo(promotionRead.trim())>0) {
							result= 1;
						}
						else if (promotion.compareTo(promotionRead.trim())<0) {
							result= -1;
						}
						else {
							if (studyYear > Integer.parseInt(studyYearRead)) {
								result= 1;
							}
							else if (studyYear < Integer.parseInt(studyYearRead)) {
								result= -1;
							}
							else {
								result= 0;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Méthode qui permet de copier les données d'un InternProfile dans un fichier binaire à une position donnée.
	 * @ param : - internProfile : un objet InternProfile dont on veut copier les données
	 * 			 - raf : un objet RandomAccessFile avec lequel écrire les données
	 * 			 - cursorPosition : la position dans le fichier à partir de laquelle écrire les données
	 */
	private void copyDatasInFile(InternProfile internProfile, RandomAccessFile raf, int cursorPosition) {
		ByteArrayOutputStream outputStream;
		byte[] fullDatas;

		char[] surname = internProfile.getSurname().toCharArray();
		char[] firstName = internProfile.getFirstName().toCharArray();
		char[] county = internProfile.getCounty().toCharArray();
		char[] promotion = internProfile.getPromotion().toCharArray();
		char[] yearStudy = String.valueOf(internProfile.getStudyYear()).toCharArray();

		byte[] surnameByte = convertCharToByte(surname, numberOfBytesForSurname);
		byte[] firstNameByte = convertCharToByte(firstName, numberOfBytesForFirstName);
		byte[] countyByte = convertCharToByte(county, numberOfBytesForCounty);
		byte[] promotionByte = convertCharToByte(promotion, numberOfBytesForPromotion);
		byte[] yearStudyByte = convertCharToByte(yearStudy, numberOfBytesForStudyYear);

		outputStream = new ByteArrayOutputStream(); 

		try {
			outputStream.write(surnameByte);
			outputStream.write(firstNameByte); 
			outputStream.write(countyByte); 
			outputStream.write(promotionByte); 
			outputStream.write(yearStudyByte); 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		fullDatas = outputStream.toByteArray(); 
		try {
			raf.seek(cursorPosition+byteForSurname);   
			raf.write(fullDatas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Méthode qui permet de convertir un tableau de caractères en un tableau de bytes à inscrire dans un fichier binaire.
	 * @ param : date = un tableau de caractères
	 *           dataMaxLength : un int qui désigne le nombre max de bytes à inscrire dans le tableau de sortie. Si ce nombre 
	 *           est supérieur au nombre de caractère dans le tableau data, le programme ajoute des 0.
	 * @ return : un tableau de bytes.
	 * 
	 */
	private byte[] convertCharToByte(char[] data, int dataMaxLength) {

		byte[] dataInByte = new byte[dataMaxLength];
		for (int i = 0; i < data.length; i++) {
			dataInByte[i] = (byte) data[i];
		}
		if (data.length < dataMaxLength) {
			for (int i = data.length; i < dataMaxLength; i++) {
				dataInByte[i] = 0;
			}
		}
		return dataInByte;
	}

	/*
	 * Méthode récursive qui permet de trouver le stagiaire parent du stagiaire à ajouter, d'inscrire dans le fichier binaire
	 * le stagiaire enfant et d'inscrire la position du stagiaire enfant dans le parent.
	 * @ param : - les données du stagiaire à ajouter : surname, firstName, county, studyYea, promotion
	 * 			 - cursorPosition : la position du curseur à partir de laquelle trouver le stagiaire parent
	 * 			 - raf : un objet RandomAccessFile
	 */
	private void createChild(String surname, String firstName, String county, String promotion, int studyYear, int cursorPosition, RandomAccessFile raf) throws IOException {
		// si le stagiaire à ajouter est plus petit que le stagiaire courant
		if (compare(surname, firstName, county, promotion, studyYear, cursorPosition, raf)==-1) {
			int pos = positionLeftChild(raf, cursorPosition);
			// si le stagiaire courant n'a pas d'enfant gauche, on ajoute le stagiaire
			if (pos==0) {
				int positionToWrite = findFirstEmptyLine(raf);
				raf.seek(cursorPosition+byteForLeftChild);
				raf.writeInt(positionToWrite);
				copyDatasInFile(new InternProfile(surname, firstName, county, promotion, studyYear), raf, positionToWrite);
				raf.seek(positionToWrite+numberOfBytesForLeftChild+numberOfBytesForRightChild+numberOfBytesForSurname+
						numberOfBytesForFirstName+numberOfBytesForCounty+numberOfBytesForPromotion+numberOfBytesForStudyYear);
				raf.writeBytes("\r");
			}
			// sinon on repète la méthode creatChild sur l'enfant gauche du stagiaire courant
			else {
				createChild(surname, firstName, county, promotion, studyYear, pos, raf);
			}
		}
		// si le stagiaire à ajouter est plus grand que le stagiaire courant
		else if (compare(surname, firstName, county, promotion, studyYear, cursorPosition, raf)==1) {
			int pos = positionRightChild(raf, cursorPosition);
			// si le stagiaire courant n'a pas d'enfant droit, on ajoute le stagiaire
			if (pos==0) {
				int positionToWrite = findFirstEmptyLine(raf);
				raf.seek(cursorPosition+byteForRightChild);
				raf.writeInt(positionToWrite);
				copyDatasInFile(new InternProfile(surname, firstName, county, promotion, studyYear), raf, positionToWrite);
				raf.seek(positionToWrite+numberOfBytesForLeftChild+numberOfBytesForRightChild+numberOfBytesForSurname+
						numberOfBytesForFirstName+numberOfBytesForCounty+numberOfBytesForPromotion+numberOfBytesForStudyYear);
				raf.writeBytes("\r");
			}
			// sinon on repète la méthode creatChild sur l'enfant droit du stagiaire courant
			else {
				createChild(surname, firstName, county, promotion, studyYear, pos, raf);
			}
		}
	}

	/*
	 * Méthode qui permet de repérer la 1ère ligne vide dans le fichier binaire
	 * @ param : raf = un objet RandomAccessFile
	 * @ return : un int qui désigne le 1er byte disponible pour écrire des données
	 */
	private int findFirstEmptyLine(RandomAccessFile raf) {
		int pos = 0;
		try {
			raf.seek(pos+byteForSurname);
			byte byteToRead = raf.readByte();
			while (byteToRead!=0) {
				pos +=numberOfBytesForLeftChild+numberOfBytesForRightChild+numberOfBytesForSurname+
						numberOfBytesForFirstName+numberOfBytesForCounty+numberOfBytesForPromotion+
						numberOfBytesForStudyYear+1;  // +1 car à la fin de chaque internProfile, ajout d'un '\r'
				raf.seek(pos+byteForSurname);
				byteToRead = raf.readByte();
			}
		} catch (IOException e) {
			return pos;
		}
		return pos;
	}

	/*
	 * Méthode qui permet d'ajouter un objet de type InternProfile dans le fichier internBDD.
	 * @ param : un objet de type InternProfile
	 */
	public void addInternProfile(InternProfile internProfile) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(internBDD, "rw");
			createChild(internProfile.getSurname(), internProfile.getFirstName(), internProfile.getCounty(), internProfile.getPromotion(), internProfile.getStudyYear(), 0, raf);
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	/*
	 * Fonction qui renvoie la position du plus petit enfant droit.
	 * @ param : cursorPosition : la position à partir de laquelle chercher le plus petit enfant droit
	 *           raf : un RandomAccessFile
	 * @ return : un int qui désigne la position dans le fichier binaire InternBDD du plus petit enfant droit
	 */
	private int findSmallestRightChild(int cursorPosition, RandomAccessFile raf) {
		int posLeftChild = positionLeftChild(raf, cursorPosition);
		if (posLeftChild==0) {
			return cursorPosition;
		}
		else {
			return findSmallestRightChild(posLeftChild, raf);
		}
	}

	/*
	 * Méthode qui permet d'effacer les données d'un stagiaire dans le fichier binaire InternBDD.
	 * @ param : - cursorPosition : la position à partir de laquelle effacer les données
	 *           - raf : un objet RandomAccessFile
	 *           - deleteChild : un booleen à true si on veut supprimer les positions des enfants, sinon à false
	 */
	private void deleteDatas(int cursorPosition, RandomAccessFile raf, boolean deleteChild) {
		try {
			if (deleteChild==true) {
				raf.seek(cursorPosition+byteForLeftChild);
				for (int i=0;i<numberOfBytesForLeftChild;i++) {
					//raf.write(' ');
					//modif
					raf.write(0);
				}
				raf.seek(cursorPosition+byteForRightChild);
				for (int i=0;i<numberOfBytesForRightChild;i++) {
					//raf.write(' ');
					//modif
					raf.write(0);
				}
			}
			raf.seek(cursorPosition+byteForSurname);
			for (int i=0;i<numberOfBytesForSurname;i++) {
				//raf.write(' ');
				//modif
				raf.write(0);
			}
			raf.seek(cursorPosition+byteForFirstName);
			for (int i=0;i<numberOfBytesForFirstName;i++) {
				//raf.write(' ');
				//modif
				raf.write(0);
			}
			raf.seek(cursorPosition+byteForCounty);
			for (int i=0;i<numberOfBytesForCounty;i++) {
				//raf.write(' ');
				//modif
				raf.write(0);
			}
			raf.seek(cursorPosition+byteForPromotion);
			for (int i=0;i<numberOfBytesForPromotion;i++) {
				//raf.write(' ');
				//modif
				raf.write(0);
			}
			raf.seek(cursorPosition+byteForStudyYear);
			for (int i=0;i<numberOfBytesForStudyYear;i++) {
				//raf.write(' ');
				//modif
				raf.write(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

	/*
	 * Méthode qui permet d'effacer un stagiaire dont les données sont passées en argument du fichier binaire InternBDD
	 * @ param : surname, firstName, county, promotion, studyYear du stagiaire
	 *           raf : un objet RandomAccessFile
	 *           cursorPosition : la position à partir de laquelle chercher le stagiaire à supprimer
	 */
	private void deleteRecursive(String surname, String firstName, String county, String promotion, int studyYear, int cursorPosition, RandomAccessFile raf) throws IOException {
		int posLeftChild = positionLeftChild(raf, cursorPosition);
		int posRightChild = positionRightChild(raf, cursorPosition);

		if (compare(surname, firstName, county, promotion, studyYear, posLeftChild, raf)==0) {
			if (positionLeftChild(raf, posLeftChild)==0 && positionRightChild(raf, posLeftChild)==0) {
				raf.seek(cursorPosition+byteForLeftChild);
				raf.writeInt(0);
				// Modif
				deleteDatas(posLeftChild, raf, true);
			}
			else if (positionLeftChild(raf, posLeftChild)==0) {
				int pos2 = positionRightChild(raf, posLeftChild);
				raf.seek(cursorPosition+byteForLeftChild);
				raf.writeInt(pos2);
				// Modif
				deleteDatas(posLeftChild, raf, true);
			}
			else if (positionRightChild(raf, posLeftChild)==0) {
				int pos2 = positionLeftChild(raf, posLeftChild);
				raf.seek(cursorPosition+byteForLeftChild);
				raf.writeInt(pos2);
				// Modif
				deleteDatas(posLeftChild, raf, true);
			}
			else if (positionLeftChild(raf, posLeftChild)!=0 && positionRightChild(raf, posLeftChild)!=0) {
				int val = findSmallestRightChild(positionRightChild(raf,posLeftChild), raf);
				InternProfile ip = extractDatasIntern(raf, val);
				copyDatasInFile(ip, raf, posLeftChild);
				deleteRecursive(ip.getSurname(), ip.getFirstName(), ip.getCounty(), ip.getPromotion(), ip.getStudyYear(), posLeftChild, raf);
			}
		}

		else if (compare(surname, firstName, county, promotion, studyYear, posRightChild, raf)==0) {
			if (positionLeftChild(raf, posRightChild)==0 && positionRightChild(raf, posRightChild)==0) {
				raf.seek(cursorPosition+byteForRightChild);
				raf.writeInt(0);
				// Modif
				deleteDatas(posRightChild, raf, true);
			}
			else if (positionLeftChild(raf, posRightChild)==0) {
				int pos2 = positionRightChild(raf, posRightChild);
				raf.seek(cursorPosition+byteForRightChild);
				raf.writeInt(pos2);
				// Modif
				deleteDatas(posRightChild, raf, true);
			}
			else if (positionRightChild(raf, posRightChild)==0) {
				int pos2 = positionLeftChild(raf, posRightChild);
				raf.seek(cursorPosition+byteForRightChild);
				raf.writeInt(pos2);
				// Modif
				deleteDatas(posRightChild, raf, true);
			}
			else if (positionLeftChild(raf, posRightChild)!=0 && positionRightChild(raf, posRightChild)!=0) {
				int val = findSmallestRightChild(positionRightChild(raf,posRightChild), raf);
				InternProfile ip = extractDatasIntern(raf, val);
				copyDatasInFile(ip, raf, posRightChild);
				deleteRecursive(ip.getSurname(), ip.getFirstName(), ip.getCounty(), ip.getPromotion(), ip.getStudyYear(), posRightChild, raf);
			}
		}

		else if (compare(surname, firstName, county, promotion, studyYear, cursorPosition, raf)==-1) {
			deleteRecursive(surname, firstName, county, promotion, studyYear, posLeftChild, raf);
		}
		else {
			deleteRecursive(surname, firstName, county, promotion, studyYear, posRightChild, raf);
		}
	}

	/*
	 * Méthode qui permet d'effacer le stagiaire racine du fichier binaire InternBDD
	 * @ param : surname, firstName, county, promotion, studyYear du stagiaire racine
	 *           raf : un objet RandomAccessFile
	 */
	private void deleteRoot(String surname, String firstName, String county, String promotion, int studyYear, int cursorPosition, RandomAccessFile raf) throws IOException {
		int posLeftChild = positionLeftChild(raf, cursorPosition);
		int posRightChild = positionRightChild(raf, cursorPosition);

		if (posLeftChild==0 && posRightChild==0) {
			deleteDatas(cursorPosition, raf, true);
		}
		else if (posLeftChild==0) {
			//				int pos2 = positionRightChild(raf, posLeftChild);
			//				raf.seek(cursorPosition+byteForLeftChild);
			//				raf.writeInt(pos2);
		}
		else if (posRightChild==0) {
			//				int pos2 = positionLeftChild(raf, posLeftChild);
			//				raf.seek(cursorPosition+byteForLeftChild);
			//				raf.writeInt(pos2);
		}
		else if (posLeftChild!=0 && posRightChild!=0) {
			int val = findSmallestRightChild(posRightChild, raf);
			InternProfile ip = extractDatasIntern(raf, val);
			copyDatasInFile(ip, raf, cursorPosition);
			deleteRecursive(ip.getSurname(), ip.getFirstName(), ip.getCounty(), ip.getPromotion(), ip.getStudyYear(), cursorPosition, raf);
		}
	}

	/*
	 * Fonction qui permet de déterminer si les données passées en argument sont celles du stagiaire racine.
	 * @ param : - surname, firstName, county, promotion, studyYear à comparer avec les données du stagiaire racine
	 * 			 - raf : un objet RandomAccessFile
	 * @ return : true si les données sont celles du stagiaire, sinon false
	 */
	private boolean isRoot(String surname, String firstName, String county, String promotion, int studyYear, RandomAccessFile raf) {
		InternProfile internProfileRoot = extractDatasIntern(raf, 0);
		InternProfile internProfileToDelete = new InternProfile(surname, firstName, county, promotion, studyYear);
		InternProfileComparator comparator = new InternProfileComparator();
		if (comparator.compare(internProfileRoot, internProfileToDelete)==0) {
			return true;
		}
		else {
			return false;
		}
	}

	/*
	 * Méthode qui permet de supprimer un objet InternProfile du fichier binaire internBDD.
	 * @ param : internProfile = un objet InternProfile à supprimer.
	 */
	public void deleteInternProfile(InternProfile internProfile) {
		RandomAccessFile raf = null;
		String surname = internProfile.getSurname();
		String firstName = internProfile.getFirstName();
		String county = internProfile.getCounty();
		String promotion = internProfile.getPromotion();
		int studyYear = internProfile.getStudyYear();

		try {
			raf = new RandomAccessFile(internBDD, "rw");
			if (isRoot(surname, firstName, county, promotion, studyYear, raf)) {
				deleteRoot(surname, firstName, county, promotion, studyYear, 0, raf);
			}
			else {
				deleteRecursive(surname, firstName, county, promotion, studyYear, 0, raf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Méthode qui permet de modifier un InternProfile dans le fichier binaire internBDD
	 * @param : oldIp = un objet InternProfile qu'on veut modifier (cet objet contient les anciennes valeurs)
	 * 			newIp = un objet InternProfile avec les nouvelles données
	 */
	public void modifyInternProfile(InternProfile oldIp, InternProfile newIp) {
		deleteInternProfile(oldIp);
		addInternProfile(newIp);
	}

	public List<InternProfile> filterSearchInternProfile (String surname, String firstName, String county, String promotion, int studyYear) {
		List<InternProfile> internProfiles = this.getAll();
		List<InternProfile> returnedProfiles = new ArrayList<InternProfile>();

		for (InternProfile internProfile : internProfiles) {
			if (surname != null) {
				if (!internProfile.getSurname().toUpperCase().contains(surname.toUpperCase())) {
					continue;
				}
			}
			if (firstName != null) {
				if (!internProfile.getFirstName().toUpperCase().contains(firstName.toUpperCase())) {
					continue;
				}
			}
			if (county != null) {
				if (!internProfile.getCounty().toUpperCase().contains(county.toUpperCase())) {
					continue;
				}
			}
			if (promotion != null) {
				if (!internProfile.getPromotion().toUpperCase().contains(promotion.toUpperCase())) {
					continue;
				}
			}
			if (studyYear != 0) {
				if (internProfile.getStudyYear() != (studyYear)) {
					continue;
				}
			}
			returnedProfiles.add(internProfile);
		}
		return returnedProfiles;
	}

}







