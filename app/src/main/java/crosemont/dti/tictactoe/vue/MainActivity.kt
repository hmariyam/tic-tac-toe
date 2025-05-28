package crosemont.dti.tictactoe.vue

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import crosemont.dti.tictactoe.R
import crosemont.dti.tictactoe.databinding.ActivityMainBinding
import crosemont.dti.tictactoe.vuemodele.JeuViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var boutons: List<android.widget.Button>
    private val jeuViewModel: JeuViewModel by viewModels()

    companion object {
        lateinit var instance : MainActivity
        private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boutons = listOf(binding.case0, binding.case1, binding.case2 ,binding.case3,
            binding.case4, binding.case5, binding.case6, binding.case7, binding.case8)

        for ((index, btn) in boutons.withIndex()) {
            btn.setOnClickListener {
                Log.i(TAG, "Case cliquée: $index")
                    jeuViewModel.jouerCoup(index)
            }
        }

        binding.btnReinitialiser.setOnClickListener {
            Log.i(TAG, "Bouton réinitialiser cliqué")
            jeuViewModel.reinitialiserJeu()
        }

        jeuViewModel.grille.observe(this, Observer { grille ->
            Log.i(TAG, "Changement d'état - grille: $grille")

            for ((index, btn) in boutons.withIndex()) {
                btn.text = grille[index]?.toString() ?: ""
            }
        })

        jeuViewModel.joueurActuel.observe(this, Observer { joueur ->
            Log.i(TAG, "Changement d'état - joueur actuel: ${joueur}")

            binding.tvEtatJeu.text = getString(R.string.tour_Text, joueur.nom, joueur.symbole)
            basculerÉtatBoutons(actif = joueur.estHumain)

            if(!joueur.estHumain) {
                lifecycleScope.launch {
                    delay(500) // Délai pour donner l'impression que le bot réfléchit!
                    jeuViewModel.jouerCoup(joueur.demanderCoup(jeuViewModel.grille.value!!))
                }
            }
        })

        jeuViewModel.gagnant.observe(this, Observer { gagnant ->
            Log.i(TAG, "Changement d'état - gagnant: ${gagnant}")

            gagnant?.let {
                binding.tvEtatJeu.text = getString(R.string.gagnant_Text, gagnant.nom)
            }
        })

        jeuViewModel.partieTerminée.observe(this, Observer { partieTerminée ->
            Log.i(TAG, "Changement d'état - partie terminée: ${partieTerminée}")

            if (partieTerminée) {
                if (jeuViewModel.gagnant.value == null) {
                    binding.tvEtatJeu.text = getString(R.string.match_nul_Text)
                }
            }
            basculerÉtatBoutons(actif = !partieTerminée)
        })

        jeuViewModel.caseOccupe.observe(this, Observer { caseOccupe ->
            Toast.makeText(this, getString(R.string.message_exception), Toast.LENGTH_SHORT).show()
        })

        jeuViewModel.combinaisonGagnante.observe(this, Observer { combinaisonGagnante ->
            if(combinaisonGagnante != null){
                combinaisonGagnante.let { positions ->
                    for(positionGagnante in positions){
                        // Puisque les cases n'ont pas de id, on ne peut pas faire R.id.position par exemple
                        // Donc, on accède la liste des boutons avec boutons -> boutons[positionGagnante]
                        // BackgroundTintList parce que on a utilisé backgroundTint pour mettre les cases blancs
                        boutons[positionGagnante].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEA86")))
                    }
                }
            } else {
                for(case in boutons){
                    case.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))
                }
            }
        })
    }

    fun basculerÉtatBoutons(actif: Boolean) {
        for (btn in boutons) {
            btn.isEnabled = actif
        }
    }
}