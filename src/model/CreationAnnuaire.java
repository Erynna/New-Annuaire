package model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * Classe utilitaire qui permet d'extraire ligne à ligne des données d'un fichier texte pour en faire des stagiaires 
 * qui s'inscrivent dans un arbre de stagiaires.
 */
public class CreationAnnuaire {

	private final String originalFilePath;
	private int maxSurnameLength = 0;
	private int maxFirstNameLength = 0;
	private int maxCountyLength = 0;
	private int maxPromotionLength = 0;
	private int maxYearStudyLength = 4;

	public CreationAnnuaire(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}

	/*
	 * Méthode qui crée un arbre binaire à partir du fichier dont le chemin est renseigné dans la variable originalFilePath.
	 * Le premier stagiaire extrait du fichier constitue le stagiaire racine (le "patriarche"). Chaque stagiaire qui est ensuite 
	 * extrait du fichier est ajouté à la descendance de la racine au fur et à mesure de la lecture du fichier source.
	 * @ return : un objet de type InternProfile qui contient dans ses propriétés leftChild et rightChild toute sa descendance.
	 */
	public InternProfile createTree() {

		BufferedReader br = null;
		InternProfile internProfile = null;

		try {
			br = new BufferedReader(new FileReader(originalFilePath));
			String line;

			internProfile = new InternProfile();

			while ((line = br.readLine()) != null) { 

				String surname = "";
				String firstName = "";
				String county = "";
				String promotion = "";
				int studyYear = 0;
				//Parcourt chacune des 5 lignes pour en déduire le nom, le prénom, le département, la promotion et l'année 
				//d'étude du stagiaire
				for (int i = 0; i < 5; i++) {      
					switch (i) {
					case 0:
						surname = line.trim();
						if (surname.length() > maxSurnameLength) {
							maxSurnameLength = surname.length();
						}
						break;
					case 1:
						firstName = line.trim();
						if (firstName.length() > maxFirstNameLength) {
							maxFirstNameLength = firstName.length();
						}
						break;
					case 2:
						county = line.trim();
						if (county.length() > maxCountyLength) {
							maxCountyLength = county.length();
						}
						break;
					case 3:
						promotion = line.trim();
						if (promotion.length() > maxPromotionLength) {
							maxPromotionLength = promotion.length();
						}
						break;
					case 4:
						studyYear = Integer.parseInt(line.trim());
						break;
					}
					line = br.readLine();
				}
				//Inscrit le stagiaire dans une lignée de stagiaires
				internProfile.addChild(surname, firstName, county, promotion, studyYear);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return internProfile;	
	}

	/*
	 * Méthode qui permet de chercher, dans la descendance d'un InternProfile passé en argument, un InternProfile.
	 * @param :  - internProfileFather : un objet de type InternProfile (pour une recherche complète, passer ici 
	 * 				en argument l'InternProfile racine)
	 * 			 - internProfile : un objet de type internProfile qu'on veut trouver dans la descendance du internProfileFather
	 * @return : null si aucun objet trouvé sinon un objet de type InternProfile qui représente l'objet cherché 
	 */
	public InternProfile findInternProfile(InternProfile internProfileFather, InternProfile internProfile) {
		//instanciation d'un objet InternProfileComparator pour comparer les 2 objets passés en argument
		InternProfileComparator comparator = new InternProfileComparator();
		int resultCompararison = comparator.compare(internProfile,internProfileFather);
		switch (resultCompararison) {
		//si le résultat de la comparaison =-1 (i.e l'objet internProfile est plus petit que internProfileFather), 
		//on oriente la recherche sur l'enfant gauche
		case -1:
			if (internProfileFather.getLeftChild().isEmpty) {
				return null;
			}
			else {
				return findInternProfile(internProfileFather.getLeftChild(), internProfile);
			}
			//si le résultat de la comparaison =1 (i.e l'objet internProfile est plus grand que internProfileFather), 
			//on oriente la recherche sur l'enfant droit
		case 1:
			if (internProfileFather.getRightChild().isEmpty) {
				return null;
			}
			else {
				return findInternProfile(internProfileFather.getRightChild(), internProfile);
			}
			//si le résultat de la comparaison n'est ni 1 ni -1, il est nécessairement = 0. Cela signifie que l'objet internProfileFather
			//est l'objet recherché. La méthode renvoie alors internProfileFather.
		default:
			return internProfileFather;
		}
	}

	/*
	 * Méthode qui permet récursivement de créer le fichier optimisé composé des informations stagiaires ainsi que les position des enfants gauches et droits
	 */
	public void createInternsBDDFile() {

		RandomAccessFile raf = null;
		File internBDDFile = new File("./internBDD.bin");

		try {
			
			internBDDFile.createNewFile();

			raf = new RandomAccessFile(internBDDFile, "rw");

			InternProfile arbre = createTree();

			writeChildrenInterns(arbre, raf);				//Appel de la méthode qui permet d'écrire les infos stagiaires et celles de ses enfants à partir de la racine

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
	 * Méthode qui permet à partir d'une chaine de caractere de générer un tableau ordonné de bytes et de compléter par des bytes vides pour s'adapter aux tailles max des données
	 */
	public byte[] completeWithEmptyBytes(String data, int dataMaxLength) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] dataWithEmptyBytes;
		byte[] dataInBytes = data.getBytes();
		byte[] nullBytes = new byte[dataMaxLength - dataInBytes.length];
		
		try {
			outputStream.write(dataInBytes);
			outputStream.write(nullBytes);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		dataWithEmptyBytes = outputStream.toByteArray();
		
		return dataWithEmptyBytes;
	}

	/*
	 * Méthode qui permet de concatener l'ensemble des donnees presentes dans les differents tableaux de bytes sous la forme d'un seul et unique tableau de byte
	 */
	public byte[] concatenateInternDatas(InternProfile internProfile) {

		ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
		byte[] fullDatas = null;
		
		try {
			outputStream.write(completeWithEmptyBytes(internProfile.getSurname(), maxSurnameLength));
			outputStream.write(completeWithEmptyBytes(internProfile.getFirstName(), maxFirstNameLength)); 
			outputStream.write(completeWithEmptyBytes(internProfile.getCounty(), maxCountyLength)); 
			outputStream.write(completeWithEmptyBytes(internProfile.getPromotion(), maxPromotionLength)); 
			outputStream.write(completeWithEmptyBytes(String.valueOf(internProfile.getStudyYear()), maxYearStudyLength)); 

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fullDatas = outputStream.toByteArray();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return fullDatas;
	}

	/*
	 * Méthode qui permet récursivement d'écrire les données stagiaires et les positions des enfants sous la forme d'une arborescence binaire en octets.
	 */
	private void writeChildrenInterns(InternProfile internProfile, RandomAccessFile raf) {

		InternProfile leftChild = internProfile.getLeftChild();
		InternProfile rightChild = internProfile.getRightChild();
		int fullDatasLength = maxSurnameLength + maxFirstNameLength + maxCountyLength + maxPromotionLength + maxYearStudyLength;
		int offsetLeftPosition = fullDatasLength + 9;				//Offset lié à l'écriture des 4 octets de la position enfant gauche, des 4 octets de la position enfant droit, de l'ensemble des données stagiaire et du separateur final
		int offsetRightPosition = fullDatasLength + 5;				//Offset lié à l'écriture des 4 octets de la position enfant droit, de l'ensemble des données stagiaire et du separateur final
		byte[] tab = concatenateInternDatas(internProfile);

		try {

			if(!leftChild.isEmpty) {										//Si présence d'un element gauche on indique sa position

				raf.writeInt((int)raf.getFilePointer() + offsetLeftPosition); //On indique que l'enfant se situe à la position du pointeur + l'ensemble des données du stagiaire + 9 octets (octets de données enfants)

			}else if (leftChild.isEmpty) {

				raf.writeInt(0);

			}

			if(!rightChild.isEmpty) {										//Si présence d'un element droit on indique sa position

				raf.writeInt((int)raf.getFilePointer() + offsetRightPosition + (leftChild.getNumberOfChildren() * offsetLeftPosition)); //On indique que l'enfant droit se situe à la position du pointeur l'offSetRightPosition (longueur des données stagiaire plus 4 octets de données + 1 octet separateur) + l'ensemble des données des enfants gauches

			}else if (rightChild.isEmpty) {

				raf.writeInt(0);

			}

			raf.write(tab);													//Traitement initial : Ecriture des données de l'élément courant		

			raf.writeBytes("\r");

			if(!leftChild.isEmpty){											//Si element possède un enfant gauche : Recursivité à Gauche

				writeChildrenInterns(leftChild, raf);

			}

			if(!rightChild.isEmpty) {										//Si élement possède un enfant droit : Recursivité à Droite

				writeChildrenInterns(rightChild, raf);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getMaxSurnameLength() {
		return maxSurnameLength;
	}

	public void setMaxSurnameLength(int maxSurnameLength) {
		this.maxSurnameLength = maxSurnameLength;
	}

	public int getMaxFirstNameLength() {
		return maxFirstNameLength;
	}

	public void setMaxFirstNameLength(int maxFirstNameLength) {
		this.maxFirstNameLength = maxFirstNameLength;
	}

	public int getMaxCountyLength() {
		return maxCountyLength;
	}

	public void setMaxCountyLength(int maxCountyLength) {
		this.maxCountyLength = maxCountyLength;
	}

	public int getMaxPromotionLength() {
		return maxPromotionLength;
	}

	public void setMaxPromotionLength(int maxPromotionLength) {
		this.maxPromotionLength = maxPromotionLength;
	}

	public int getMaxYearStudyLength() {
		return maxYearStudyLength;
	}

	public void setMaxYearStudyLength(int maxYearStudyLength) {
		this.maxYearStudyLength = maxYearStudyLength;
	}
}
