package crosemont.dti.tictactoe.modele

class Plateau {
    class CaseInexistanteException : Exception()
    class CaseOccupéeException : Exception()

    private var _grille: Array<Char?> = arrayOfNulls(9)
    val grille
        get() = _grille.toList()

    // Companion object de combinaison gagnante pour l'utiliser dans la stratégie intelligente
    companion object {
        fun combinaisonGagnantes() : Array<IntArray> {
            val combinaisonsGagnantes = arrayOf(
                intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Lignes
                intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Colonnes
                intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)  // Diagonales
            )
            return combinaisonsGagnantes
        }
    }

    private fun case(i: Int): Char? {
        return _grille[i]
    }

    private fun caseEstVide(case: Char?): Boolean {
        return case == null
    }

    fun jouerCoup(index: Int, symbole: Char) {
        if (index !in _grille.indices)
            throw CaseInexistanteException()
        if (!caseEstVide(case(index)))
            throw CaseOccupéeException()

        _grille[index] = symbole
    }

    fun vérifierVictoire(symbole: Char): IntArray ? {

        for(combinaison in Plateau.combinaisonGagnantes()){
            var (pos1,pos2,pos3) = combinaison
            if(case(pos1) == symbole && case(pos2) == symbole && case(pos3) == symbole){
                return combinaison
            }
        }
        return null
    }

    fun estPlein(): Boolean {
        return _grille.none { caseEstVide(it) }
    }

    fun reinitialiser() {
        _grille = arrayOfNulls(9)
    }

    override fun toString(): String {
        // Chaîne de caractère brut
        return """
            ${case(0) ?: 0} | ${case(1) ?: 1} | ${case(2) ?: 2}
            ---------
            ${case(3) ?: 3} | ${case(4) ?: 4} | ${case(5) ?: 5}
            ---------
            ${case(6) ?: 6} | ${case(7) ?: 7} | ${case(8) ?: 8}
        """.trimIndent()
    }
}