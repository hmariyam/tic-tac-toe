import crosemont.dti.tictactoe.modele.JoueurBot
import crosemont.dti.tictactoe.modele.Plateau
import crosemont.dti.tictactoe.modele.StrategieIntelligente
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class JoueurBotTest {

    private lateinit var joueur: JoueurBot

    @BeforeTest
    fun setup() {
        joueur = JoueurBot("Bot", 'O', StrategieIntelligente())
    }

    @Test
    fun `test demander un coup dans une grille vide`() {
        val grille = Plateau().grille
        val index = joueur.demanderCoup(grille)
        assertTrue(index in grille.indices)
    }

    @Test
    fun `test demander un coup quand il reste une case libre`() {
        val indexCaseLibre = 3
        val grille = List(9) { if (it == indexCaseLibre) null else 'X' }
        val index = joueur.demanderCoup(grille)
        assertEquals(index, indexCaseLibre)
    }

    // Tests unitaires pour le Joueur Bot intelligent faite par Mariyam
    // Vérifie que le bot attaque le joueur en l'empêchant de gagner (contre une victoire)
    @Test
    fun `test attaquer le joueur adversaire pour l'empêcher de gagner`(){
        val grille = listOf('X', null, 'X', null, null, null, null, null, null)
        val resultat = joueur.demanderCoup(grille)
        assertEquals(1, resultat)
    }

    // Vérifie que le Joueur Bot complète une combinaison gagnante
    @Test
    fun `test completer une combinaison gagnante`(){
        val grille = listOf(null, 'O', null, null, null, null, null, 'O', null)
        val resultat = joueur.demanderCoup(grille)
        assertEquals(4, resultat)
    }
}