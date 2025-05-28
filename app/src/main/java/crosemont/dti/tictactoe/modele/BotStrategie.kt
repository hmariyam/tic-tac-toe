package crosemont.dti.tictactoe.modele

interface BotStrategie {

    fun demanderCoupBot(grille : List<Char?>, symbole : Char, symboleAdversaire : Char) : Int

}