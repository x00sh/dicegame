package com.example.cw1dicegame
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Random


class GameLoop : AppCompatActivity() {
    // human dice and their image views
    private lateinit var hDice: Dice
    private lateinit var face1: ImageView
    private lateinit var face2: ImageView
    private lateinit var face3: ImageView
    private lateinit var face4: ImageView
    private lateinit var face5: ImageView

    // the buttons to play the game
    private lateinit var btThrow: Button
    private lateinit var btScore: Button

    // cpu dice and imageviews
    private lateinit var cDice: Dice
    private lateinit var face6: ImageView
    private lateinit var face7: ImageView
    private lateinit var face8: ImageView
    private lateinit var face9: ImageView
    private lateinit var face10: ImageView

    private lateinit var total:TextView
    private lateinit var cputotal:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_loop)

        val changeGoal: AlertDialog = AlertDialog.Builder(this)
        .setTitle("Change value?")
        .setMessage("Do you want to change the goal from $goal?")
        .setPositiveButton("Yes") { _, _ ->
            val inputDialogBuilder = AlertDialog.Builder(this)
            inputDialogBuilder.setMessage("Enter a positive number:")
            val input = EditText(this)
            inputDialogBuilder.setView(input)
            inputDialogBuilder.setPositiveButton("Ok") {_, _  ->
                val value = input.text.toString().toIntOrNull()
                if (value != null && value > 0) {
                    goal = value
                    Toast.makeText(this, "First to $goal wins", Toast.LENGTH_LONG).show() }
                else {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                    inputDialogBuilder.setView(input) }
            }
            inputDialogBuilder.setNegativeButton("Cancel") { dialog, _  ->
                dialog.cancel() }
            inputDialogBuilder.show() }

        .setNegativeButton("No") { _, _ ->
            // nothing changes
        }
    .create()

     changeGoal.show()

        val difficulty = AlertDialog.Builder(this)
        difficulty.setTitle("Difficulty")
        difficulty.setMessage("Which difficulty do you want?")
        difficulty.setPositiveButton("Hard") { dialog, _ ->
            challenge = true
            dialog.dismiss()
        }
        difficulty.setNegativeButton("Normal") { dialog, _ ->
            dialog.dismiss()
        }
        difficulty.show()
        // initialise both interaction buttons
        btThrow = findViewById(R.id.button)
        btScore = findViewById(R.id.button2)
        // initialise both scores
        total = findViewById(R.id.textView2)
        cputotal = findViewById(R.id.textView)
        //initialize human dice imageviews
        face1 = findViewById(R.id.df1)
        face2 = findViewById(R.id.df2)
        face3 = findViewById(R.id.df3)
        face4 = findViewById(R.id.df4)
        face5 = findViewById(R.id.df5)
        // initialise cpu dice imageviews
        face6 = findViewById(R.id.df6)
        face7 = findViewById(R.id.df7)
        face8 = findViewById(R.id.df8)
        face9 = findViewById(R.id.df9)
        face10 = findViewById(R.id.df10)

        // initialize human dice for 1st roll
        hDice = Dice(this)

        // initialize cpu dice
        cDice = Dice(this)

        btThrow.setOnClickListener {
            if(tiebreaker){
                rollDice(humandice, hDice, face1, face2, face3, face4, face5)
                btThrow.isEnabled = false
                btScore.isEnabled = true
            }
            else if(thrownCount == 0){
                firstRollExpired = false
                rollDice(humandice, hDice, face1, face2, face3, face4, face5)
                rollDice(cpudice, cDice, face6, face7, face8, face9, face10)
                btScore.isEnabled = true
                kept1=false
                kept2=false
                kept3=false
                kept4=false
                kept5=false
                ++thrownCount }
            else if(thrownCount == 1 && !tiebreaker){
                firstRollExpired = true
                if(!kept1){
                    retrieveImage(humandice, hDice, face1) }
                else{humandice.add(0)}
                if(!kept2){
                    retrieveImage(humandice, hDice, face2) }
                else{humandice.add(0)}
                if(!kept3){
                    retrieveImage(humandice, hDice, face3) }
                else{humandice.add(0)}
                if(!kept4){
                    retrieveImage(humandice, hDice, face4) }
                else{humandice.add(0)}
                if(!kept5){
                    retrieveImage(humandice, hDice, face5) }
                else{humandice.add(0)}
                ++thrownCount
            }
            else if(thrownCount == 2 && !tiebreaker){
                if(!kept1){
                    retrieveImage(humandice, hDice, face1)
                    keptDice.add(humandice[10])
                    kept1=true
                }
                else{humandice.add(0)}
                if(!kept2){
                    retrieveImage(humandice, hDice, face2)
                    keptDice.add(humandice[11])
                    kept2=true
                }
                else{humandice.add(0)}
                if(!kept3){
                    retrieveImage(humandice, hDice, face3)
                    keptDice.add(humandice[12])
                    kept3=true
                }
                else{humandice.add(0)}
                if(!kept4){
                    retrieveImage(humandice, hDice, face4)
                    keptDice.add(humandice[13])
                    kept4=true
                }
                else{humandice.add(0)}
                if(!kept5){
                    retrieveImage(humandice, hDice, face5)
                    keptDice.add(humandice[14])
                    kept5=true
                }
                else{humandice.add(0)}
                // player won't get to see what dice they rolled since after the 2nd reroll they score automatically so this keeps them informed

                doScoreStuff()
            } else{ }
        }
        btScore.setOnClickListener {
            if(kept1 && kept2 && kept3 && kept4 && kept5){
                Toast.makeText(this, "Dice added: ${keptDice.get(0)}, ${keptDice.get(1)}, ${keptDice.get(2)}, ${keptDice.get(3)}, ${keptDice.get(4)} ", Toast.LENGTH_LONG).show()
                doScoreStuff()}
            else{Toast.makeText(this, "You haven't chosen all of your dice!", Toast.LENGTH_SHORT).show()}
            }
        face1.setOnClickListener{
            if( (humanscore < 101) && (cpuscore < 101) && !kept1 && !firstRollExpired){
                keptDice.add(humandice[0])
                kept1=true
                }
            else if( (humanscore < 101) && (cpuscore < 101) && !kept1 && firstRollExpired){
                keptDice.add(humandice[5] )
                kept1=true
            }
        }
        face2.setOnClickListener{
            if( (humanscore < 101) && (cpuscore < 101) && !kept2 && !firstRollExpired) {
                keptDice.add(humandice[1])
                kept2 = true }
            else if( (humanscore < 101) && (cpuscore < 101) && !kept2 && firstRollExpired) {
                keptDice.add(humandice[6])
                kept2 = true
            }
        }
        face3.setOnClickListener{
            if((humanscore < 101) && (cpuscore < 101) && !kept3 && !firstRollExpired) {
                keptDice.add(humandice[2])
                kept3 = true }
            else if( (humanscore < 101) && (cpuscore < 101) && !kept3 && firstRollExpired) {
                keptDice.add(humandice[7])
                kept3 = true
            }
        }
        face4.setOnClickListener{
            if((humanscore < 101) && (cpuscore < 101) && !kept4 && !firstRollExpired){
                keptDice.add(humandice[3])
                kept4=true }
            else if( (humanscore < 101) && (cpuscore < 101) && !kept4 && firstRollExpired) {
                keptDice.add(humandice[8])
                kept4 = true
            }
        }
        face5.setOnClickListener{
            if( (humanscore < 101) && (cpuscore < 101) && !kept5 && !firstRollExpired){
                keptDice.add(humandice[4])
                kept5=true }
            else if( (humanscore < 101) && (cpuscore < 101) && !kept5 && firstRollExpired) {
                keptDice.add(humandice[9])
                kept5 = true
            }
        }
        btScore.isEnabled = false }

    var keptDiceCPU:MutableList<Int> = mutableListOf()

    fun randomlyrerolling(){
        for (i in 0..4){ // for every index in cpudice list
            var cpuRANDOM = Random()
            var decision:Int = cpuRANDOM.nextInt(3) // decide which reroll to keep
            Log.d("DECIDED ON", "decision generated: $decision")
            Log.d("INDEX START", "for index $i in 0..4")
            if(decision == 0){ // decided not to reroll, keep current roll
                Log.d("TAG", "decision $decision, adding roll ${decision + 1}")
                keptDiceCPU.add(cpudice[i]) // add number
                Log.d("TAG", "added value ${cpudice[i]} to KeptDice index $i")
            } else{ // decision is not 0, cpu wishes to reroll
                Log.d("TAG", "got value ${cpudice[i]}, rerolling")
                cpureroll(i) // reroll commences
                Log.d("TAG", "new value is ${cpudice[i]}")
                if (decision == 1){ // decides to keep the first reroll
                    Log.d("TAG", "decision $decision, adding roll ${decision + 1}")
                    keptDiceCPU.add(cpudice[i])
                    Log.d("TAG", "added ${cpudice[i]} to KeptDice index $i")
                } else{
                    Log.d("TAG", "got ${cpudice[i]}, rerolling")
                    cpureroll(i) // get another value, replace it
                    Log.d("TAG", "new result is ${cpudice[i]}")
                    Log.d("TAG", "decision $decision, adding roll ${decision + 1}")
                    keptDiceCPU.add(cpudice[i])
                    Log.d("TAG", "added ${cpudice[i]} to KeptDice index $i")

                }
            }
        }
        for(k in 0..(keptDiceCPU.size - 1)){
            Log.d("TAG","${keptDiceCPU[k]}") }
    }
    fun cpureroll(i:Int){ // take an argument which is the index at the dicelist
        cpudice.removeAt(i) // get rid of whatever is stored at that index
        cpudice.add(i, cDice.roll()) } // generate a new random number to store in that index

    fun efficient(){
        for (i in 0..4){ // for every index in cpudice list
            Log.d("TAG", "for index $i in 0..4")
            if(cpudice[i] > 3){ // value at this index is greater than 3
                keptDiceCPU.add(cpudice[i]) // add high value number
                Log.d("TAG", "added value ${cpudice[i]} to KeptDice index $i")
            } else{ // value at this index is 3 or less, reroll
                Log.d("TAG", "got value ${cpudice[i]}, rerolling")
                cpureroll(i) // get another value, replace it
                Log.d("TAG", "new value is ${cpudice[i]}")
                if (cpudice[i] > 3){ // value at this index is higher than 3
                    keptDiceCPU.add(cpudice[i])
                    Log.d("TAG", "added ${cpudice[i]} to KeptDice index $i")
                } else{
                    Log.d("TAG", "got ${cpudice[i]}, rerolling")
                    cpureroll(i) // get another value, replace it
                    Log.d("TAG", "new result is ${cpudice[i]}")
                    keptDiceCPU.add(cpudice[i])
                    Log.d("TAG", "added ${cpudice[i]} to KeptDice index $i")
                    for(k in 0..(keptDiceCPU.size - 1)){
                        Log.d("TAG","${keptDiceCPU[k]}")
                    }

                }
            }
        }
    }
    /* My logic for the efficient strategy is as follows:
    *  Whenever I play, any of my rolls equal to 4 or above, I keep. Anything else, I re-roll.
    *  This is to ensure I get a minimum of or around 20, an above average score given the
    *  number of dice and number of faces on each die and this strategy has me winning almost
    *  every time Therefore, if the computer goes through each of it's results each time it
    *  rolls, it can check if the result is greater than 4, and if it is, it can add that result
    *  to it's list of kept dice. Then, for all of the indexes where the value is less than 3,
    *  the CPU keeps re-rolling until it's rerolled twice, then keeps the final roll, regardless
    *  of it's value. I chose 4 as the threshold to keep as it essentially results in a 50/50
    *  chance of putting the CPU on track to achieving a score near 20 at the minimum.
    *
    *  While running the program, each time the score is updated, check the Logcat to see the
    *  CPU's thought process, seeing which value was rolled each time and whether or not it decided
    *  to keep rolled value.
    *
    *  The main disadvantage of this is the lack of efficiency, given that it is meticulously
    *  rolling each dice one by one, at worst, exhausting all of it's rolls in search of higher
    *  numbers that cannot be guaranteed.
    *
    *  To compare the behaviour of the random and efficient methods, I have included a dialog box
    *  that allows you to choose your difficulty, normal being the random behaviour and hard being
    *  the efficient one.
    * */

    fun doScoreStuff(){
        Toast.makeText(this, "Dice added: ${keptDice.get(0)}, ${keptDice.get(1)}, ${keptDice.get(2)}, ${keptDice.get(3)}, ${keptDice.get(4)} ", Toast.LENGTH_LONG).show()
        if(challenge){ efficient() }
        else{randomlyrerolling()}
        // after choosing the new rolls, display them before the score function is called and clears the list
        face6.setImageDrawable(cDice.getDrawable(keptDiceCPU[0]))
        face7.setImageDrawable(cDice.getDrawable(keptDiceCPU[1]))
        face8.setImageDrawable(cDice.getDrawable(keptDiceCPU[2]))
        face9.setImageDrawable(cDice.getDrawable(keptDiceCPU[3]))
        face10.setImageDrawable(cDice.getDrawable(keptDiceCPU[4]))
        thrownCount = 0
        score(total)
        cpuScore(cputotal)
        winner()
        //reset the human dice images to the default, to draw attention to the fact that the user needs to roll again to proceed

        btScore.isEnabled = false // each time the user scores, they can only add it to their tally once
        btThrow.isEnabled = true // once scored, the user must roll again
    }
    var thrownCount:Int = 0     // keep track of how many times you've pressed thrown in your turn

    var goal:Int = 101
    var humandice:MutableList<Int> = mutableListOf()   // list of all the rolls you've made this turn

    var firstRollExpired = false

    var cpudice:MutableList<Int> = mutableListOf()
    var cpuscore:Int = 0

    var keptDice:MutableList<Int> = mutableListOf() // list of the dice that will actually be added to your score

    var challenge: Boolean = false

    // the remaining dice can only roll again if you haven't already kept your current roll
    var kept1:Boolean = true
    var kept2:Boolean = true
    var kept3:Boolean = true
    var kept4:Boolean = true
    var kept5:Boolean = true
    var humanscore:Int = 0

    // count the number of wins for player and cpu
    var wins:Int = 0
    var loss:Int = 0
    var tiebreaker:Boolean = false //
    // check if either player has won or tied
    fun winner(){
        // popup windows were taken from week 7 tutorial sample code (game over popup) and modified to fit this program
        if(humanscore >= goal && humanscore > cpuscore){
            btScore.isClickable = false
            btThrow.isClickable = false
            var winnerView: View = layoutInflater.inflate(R.layout.popup_winner, null)
            var winnerWindow = PopupWindow(this)
            winnerWindow.contentView = winnerView
            winnerWindow.showAtLocation(winnerView, Gravity.CENTER, 0, 0)
            winnerView.setOnClickListener {
                winnerWindow.dismiss()
            }
            ++wins
        }
        else if(cpuscore >= goal && cpuscore > humanscore){
            btScore.isClickable = false
            btThrow.isClickable = false
            var loserView: View = layoutInflater.inflate(R.layout.popup_loser, null)
            var loserWindow = PopupWindow(this)
            loserWindow.contentView = loserView
            loserWindow.showAtLocation(loserView, Gravity.CENTER, 0, 0)
            loserView.setOnClickListener {
                loserWindow.dismiss()
            }
            ++loss
        }
        else if((humanscore == cpuscore) && (cpuscore >= goal)) {
            var tieView: View = layoutInflater.inflate(R.layout.popup_tie, null)
            var tieWindow = PopupWindow(this)
            tieWindow.contentView = tieView
            tieWindow.showAtLocation(tieView, Gravity.CENTER, 0, 0)
            tieView.setOnClickListener {
                tieWindow.dismiss()
                tiebreaker = true
            }
        } else{}
    }
    //add all of the player's kept dice
    fun score(tv:TextView) {
        for(a in keptDice){ humanscore += a }
        tv.setText("Your Score: $humanscore")
        keptDice.clear()
        humandice.clear()}
    // add up the cpu's score
    fun cpuScore(tv:TextView){
        for(a in keptDiceCPU){ cpuscore += a }
        tv.setText("CPU Score: $cpuscore")
        cpudice.clear()
        keptDiceCPU.clear()}
    // roll a random no. between 1-6, pass it through getDrawable for an image to set onto the imageview
    fun retrieveImage(list:MutableList<Int>, dice:Dice, image:ImageView){
        var result:Int = dice.roll()
        list.add(result)
        image.setImageDrawable(dice.getDrawable(result)) }
    // roll dice function is an independent function able to be
    // used by both the human and cpu players, with their own dice
    fun rollDice(dicelist:MutableList<Int>, d1:Dice, f1:ImageView,f2:ImageView,f3:ImageView,f4:ImageView,f5:ImageView) {
        retrieveImage(dicelist, d1, f1)
        retrieveImage(dicelist, d1, f2)
        retrieveImage(dicelist, d1, f3)
        retrieveImage(dicelist, d1, f4)
        retrieveImage(dicelist, d1, f5) }
}