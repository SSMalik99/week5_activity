package com.example.week5_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    // variables
    lateinit var chronometer : Chronometer // chronometer is a stopwatch type of thing
    var running = false // stopwatch is running or not
    var offset : Long = 0 // start time for the stopwatch

    // key strings for bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer = findViewById<Chronometer>(R.id.chronometer)


        // check on the runnig chronometer is it going -ve
        chronometer.setOnChronometerTickListener {


            if (isGoingNegative()) {
                chronometer.stop()
                running = false
                offset = 0
            }else if (offset > 0 && running) {

                offset-=1000
            }
        }


        //restore from the previous bundle
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)

            if (running) {
                chronometer.base = savedInstanceState.getLong(BASE_KEY)
                chronometer.start()
            }else {
                setBaseTime()
            }
        }

        val addOneSecBtn = findViewById<Button>(R.id.addOneBtn)
        val addFiveSecBtn = findViewById<Button>(R.id.addFiveBtn)
        // add one minute button function
        addOneSecBtn.setOnClickListener {

            if (!running){
                offset += 1000
                chronometer.base = SystemClock.elapsedRealtime() + offset
            }else{
                showWarningAlert(
                    "You can't increase time while timer is running",
                    "Warning!"
                )
            }

        }

        // add five second button
        addFiveSecBtn.setOnClickListener {
            if (!running){
                offset += 5000
                chronometer.base = SystemClock.elapsedRealtime() + offset
            }else{
                showWarningAlert(
                    "You can't increase time while timer is running",
                    "Warning!"
                )
            }
        }

        // start button
        findViewById<Button>(R.id.startBtn).setOnClickListener {

            if (!running) {
                if (isGoingNegative()){

                    chronometer.stop()

                }else {
                    //set base time
                    setBaseTime()
                    //start the stop watch
                    chronometer.start()
                    // set running true
                    running = true
                }
            }
        }

        //stop button function
        findViewById<Button>(R.id.stopBtn).setOnClickListener {
            if (running) {
                //save offset
//                saveOffset()
                // stop the stopwatch
                chronometer.stop()
                //set running false
                running = false
            }
        }

        //reset button function
        findViewById<Button>(R.id.resetBtn).setOnClickListener {
            //set offset to zero
            offset = 0
            //reset stop watch to 0
            setBaseTime()
            running = false
        }
    }

    private fun showWarningAlert(message:String, title: String) {
        val dialogue = AlertDialog.Builder(this)
        dialogue.setMessage(message)
        dialogue.setCancelable(true)
        val alert = dialogue.create()
        alert.setTitle(title)
        alert.show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, chronometer.base)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        if (running) {
//            saveOffset()
            chronometer.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            chronometer.start()
            offset = 0
        }
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - chronometer.base
    }



    //allow us to set base time for any offset
    private fun setBaseTime() {
        chronometer.base = SystemClock.elapsedRealtime() + offset
    }

    private fun isGoingNegative() : Boolean {
        var time = chronometer.text.toString()
        if (time == "00:00" || time == "00:00:00") {
            return true
        }
        if (time.contains("-")){
            return true
        }
        return  false
    }
}