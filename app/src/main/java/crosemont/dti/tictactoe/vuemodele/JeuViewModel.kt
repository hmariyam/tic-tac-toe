package crosemont.dti.tictactoe.vuemodele

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import crosemont.dti.tictactoe.R
import crosemont.dti.tictactoe.modele.BotStrategie
import crosemont.dti.tictactoe.modele.Joueur
import crosemont.dti.tictactoe.modele.JoueurBot
import crosemont.dti.tictactoe.modele.JoueurHumain
import crosemont.dti.tictactoe.modele.Plateau
import crosemont.dti.tictactoe.modele.StrategieIntelligente
import crosemont.dti.tictactoe.vue.MainActivity

class JeuViewModel : ViewModel() {

    private val plateau = Plateau()
    private val joueur1 = JoueurHumain("1", 'X')
    private val joueur2 = JoueurBot("Bot", 'O', strategie = StrategieIntelligente())

    private val _grille = MutableLiveData<List<Char?>>().apply { value = plateau.grille }
    val grille: LiveData<List<Char?>> get() = _grille

    private val _joueurActuel = MutableLiveData<Joueur>().apply { value = joueur1 }
    val joueurActuel: LiveData<Joueur> get() = _joueurActuel

    private val _gagnant = MutableLiveData<Joueur?>()
    val gagnant: LiveData<Joueur?> get() = _gagnant

    private val _partieTerminée = MutableLiveData<Boolean>().apply { value = false }
    val partieTerminée: LiveData<Boolean> get() = _partieTerminée

    private val _caseOccupe = MutableLiveData<String>()
    val caseOccupe = _caseOccupe

    private val _combinaisonGagnante = MutableLiveData<IntArray?>()
    val combinaisonGagnante : MutableLiveData<IntArray?> get() = _combinaisonGagnante

    fun jouerCoup(index: Int) {
        try {
            plateau.jouerCoup(index, joueurActuel.value!!.symbole)
            _grille.value = plateau.grille
            val combinaisonGagnante = plateau.vérifierVictoire(_joueurActuel.value!!.symbole)
            if (combinaisonGagnante != null) {
                _gagnant.value = _joueurActuel.value
                _combinaisonGagnante.value = combinaisonGagnante
                _partieTerminée.value = true
                return
            }

            if (plateau.estPlein()) {
                _partieTerminée.value = true
                return
            }
            changerJoueur()
        } catch (e : Plateau.CaseOccupéeException) {
            _caseOccupe.value = MainActivity.instance.getString(R.string.message_exception)
        }
    }

    private fun changerJoueur() {
        _joueurActuel.value = if (_joueurActuel.value == joueur1) joueur2 else joueur1
    }

    fun reinitialiserJeu() {
        plateau.reinitialiser()
        _gagnant.value = null
        _partieTerminée.value = false
        _grille.value = plateau.grille
        _joueurActuel.value = joueur1
        _combinaisonGagnante.value = null
    }
}