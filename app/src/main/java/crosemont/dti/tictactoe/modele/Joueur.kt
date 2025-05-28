package crosemont.dti.tictactoe.modele

import crosemont.dti.tictactoe.vue.MainActivity

abstract class Joueur(val nom: String, val symbole: Char) {

    abstract val estHumain: Boolean

    abstract fun demanderCoup(grille: List<Char?>): Int

    override fun toString(): String {
        return "Joueur(nom: $nom, symbole: $symbole)"
    }
}

class JoueurHumain(nom: String, symbole: Char) : Joueur(nom, symbole) {
    override val estHumain = true

    override fun demanderCoup(grille: List<Char?>): Int {
        throw Exception("Le joueur humain joue à partir de l'interface graphique.")
    }
}

class JoueurBot(nom: String, symbole: Char, val strategie : BotStrategie) : Joueur(nom, symbole) {

    override val estHumain = false

    override fun demanderCoup(grille: List<Char?>): Int {
        return strategie.demanderCoupBot(grille, symbole, symboleAdversaire = 'X')
    }
}

class StrategieAleatoire : BotStrategie {

    override fun demanderCoupBot(grille: List<Char?>, symbole : Char, symboleAdversaire : Char): Int {
        val indexValides = grille.mapIndexedNotNull() { i, symbole ->
            if (symbole == null) i else null
        }
        return indexValides.random()
    }
}

class StrategieIntelligente : BotStrategie {

    override fun demanderCoupBot(grille: List<Char?>, symbole : Char, symboleAdversaire : Char): Int {

        // Vérifie si il y a une possibilité d'une combinaison gagnante dans la grille afin de gagner
        for (combinaison in Plateau.combinaisonGagnantes()){
            var (pos1,pos2,pos3) = combinaison
            if(grille[pos1] == symbole && grille[pos2] == symbole && grille[pos2] == null){
                return pos3
            } else if (grille[pos1] == symbole && grille[pos2] == null && grille[pos3] == symbole) {
                return pos2
            } else if (grille[pos1] == null && grille[pos2] == symbole && grille[pos3] == symbole) {
                return pos1
            }
        }

        // Vérifie si il y a une possibilité d'une combinaison gagnante chez le JoueurHumain afin de l'attaquer
        for (combinaison in Plateau.combinaisonGagnantes()){
            var (pos1,pos2,pos3) = combinaison
            if(grille[pos1] == symboleAdversaire && grille[pos2] == symboleAdversaire && grille[pos3] == null){
                return pos3
            } else if (grille[pos1] == symboleAdversaire && grille[pos2] == null && grille[pos3] == symboleAdversaire) {
                return pos2
            } else if (grille[pos1] == null && grille[pos2] == symboleAdversaire && grille[pos3] == symboleAdversaire) {
                return pos1
            }
        }

        // Sinon, il prend une case aléatoire si il n'y a aucune possibilité
        val indexValides = grille.mapIndexedNotNull() { i, symbole ->
            if (symbole == null) i else null
        }
        return indexValides.random()
    }
}