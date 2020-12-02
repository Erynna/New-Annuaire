package model;

import java.util.Comparator;

/*
 * Classe qui permet de comparer des objets InternProfile. 
 */

/*
 * Méthode qui permet de comparer 2 objets de la classe InternProfile
 * @ return : 1 si l'objet 1 est inférieur à  l'objet 2 (les données des 2 InternProfile passés en argument sont comparées deux à deux)
 *            -1 si l'objet 1 est supérieur (dans l'ordre alphabétique)
 *            0 si les deux objets sont égaux 
 */
public class InternProfileComparator implements Comparator<InternProfile> {

	@Override
	public int compare(InternProfile ip1, InternProfile ip2) {
		// compare les noms de famille
		if (ip1.getSurname().compareTo(ip2.getSurname())>0) {
			return 1;
		}
		else if (ip1.getSurname().compareTo(ip2.getSurname())<0) {
			return -1;
		}
		// si égalité des noms de famille, compare les prénoms
		else {
			if (ip1.getFirstName().compareTo(ip2.getFirstName())>0) {
				return 1;
			}
			else if (ip1.getFirstName().compareTo(ip2.getFirstName())<0) {
				return -1;
			}
			// si égalité des prénoms, compare les départements
			else {
				if (ip1.getCounty().compareTo(ip2.getCounty())>0) {
					return 1;
				}
				else if (ip1.getCounty().compareTo(ip2.getCounty())<0) {
					return -1;
				}
				//si égalité des départements, compare les promotions
				else {
					if (ip1.getPromotion().compareTo(ip2.getPromotion())>0) {
						return 1;
					}
					else if (ip1.getPromotion().compareTo(ip2.getPromotion())<0) {
						return -1;
					}
					else {
						//si égalité des promotions, compare les années d'études
						if (ip1.getStudyYear()>ip2.getStudyYear()) {
							return 1;
						}
						else if (ip1.getStudyYear()<ip2.getStudyYear()) {
							return -1;
						}
						else {
							//si égalité de tous les champs, renvoie 0
							return 0;
						}
					}
					
				}
			}
		}
	}
}