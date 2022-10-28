package com.desiart.kaholas

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.airbnb.lottie.LottieAnimationView
import java.util.*


class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    val RecordAudioRequestCode = 1
    private val speechRecognizer: SpeechRecognizer? = null
    private var ANSWER: Int = 1
    private var i = 1;
    private val number = 2;
   lateinit var textView:TextView
   lateinit var button:Button
   lateinit var ans: TextView
   lateinit var answer:EditText
   lateinit var speak_answer:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.findViewById<TextView>(R.id.textView)
        button.findViewById<Button>(R.id.button)
        ans.findViewById<TextView>(R.id.ans)
        answer.findViewById<EditText>(R.id.answer)
        speak_answer.findViewById<ImageView>(R.id.textView)

        tts = TextToSpeech(this, this)
        SpeechRecognizer.createSpeechRecognizer(this);
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission();
        }
                 QuestionHandling()




          sayAnswer()




        button.setOnClickListener() {

            if (answer.text.isEmpty() == true) {
                tts!!.speak("Please enter the answer", TextToSpeech.QUEUE_FLUSH, null, "")
                Handler().postDelayed({
                    tts!!.speak("$number multiplied by $i", TextToSpeech.QUEUE_FLUSH, null, "")

                }, 2000)
            } else {

                math(number, i, ans, answer, textView)
                answer.clearAnimation()
                i++
                if (i == 11) {
                    i = 1
                }
                answer.clearComposingText()
                textView.text = "$number * $i"
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            }
        }
    }

    fun math(num: Int, i: Int, ans: TextView, answer: EditText, question: TextView) {
        readQuestion(num, i)

        if (i == 1) {

            speak(num, i, answer)
        } else if (i == 2) {
            speak(num, i, answer)

        } else if (i == 3) {
            speak(num, i, answer)
        } else if (i == 4) {
            speak(num, i, answer)
        } else if (i == 5) {
            speak(num, i, answer)
        } else if (i == 6) {
            speak(num, i, answer)
        } else if (i == 7) {
            speak(num, i, answer)
        } else if (i == 8) {
            speak(num, i, answer)
        } else if (i == 9) {
            speak(num, i, answer)
        } else if (i == 10) {
            speak(num, i, answer)
        }
        answer.setText("");
    }

    fun speak(num: Int, i: Int, answer: EditText) {
        val animation = findViewById<LottieAnimationView>(R.id.animation)
        if ((num * i).toString() == answer.text.toString()) {

            animation.setAnimation(R.raw.correct)
            animation.playAnimation()
            tts!!.speak("Correct answer", TextToSpeech.QUEUE_FLUSH, null, "")

        } else {
            animation.setAnimation(R.raw.wrong)
            animation.playAnimation()
            tts!!.speak("wrong  answer", TextToSpeech.QUEUE_FLUSH, null, "")

        }
    }



    fun readQuestion(num: Int, i: Int) {
        tts!!.speak("$num * $i ", TextToSpeech.QUEUE_FLUSH, null, "")
    }




    fun QuestionHandling() {
        textView.text = "$number * $i"
        var first: Boolean = true
        if (first == true) {
            android.os.Handler().postDelayed({
                tts!!.speak("$number multiplied by $i", TextToSpeech.QUEUE_FLUSH, null, "")

            }, 2000)
        }


        textView.addTextChangedListener {
            android.os.Handler().postDelayed({
                tts!!.speak("$number multiplied by $i", TextToSpeech.QUEUE_FLUSH, null, "")

            }, 2000)
        }

    }
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }
    fun sayAnswer(){
    val speechRecognizerIntent =  Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer!!.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {
                answer.setText("")
                answer.setHint("Listening...")
            }

            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {}
            override fun onError(i: Int) {}
            override fun onResults(bundle: Bundle) {
                speak_answer.setImageResource(R.drawable.ic_baseline_mic_24)
                val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                answer.setText(data!![0])
            }

            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle) {}
        })

        speak_answer.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                speechRecognizer.stopListening()
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                speak_answer.setImageResource(R.drawable.ic_baseline_mic_on)
                speechRecognizer.startListening(speechRecognizerIntent)
            }
            false
        })


    }
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RecordAudioRequestCode && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }


}












