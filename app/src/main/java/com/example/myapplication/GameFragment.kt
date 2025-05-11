package com.example.myapplication

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var score = 0
    private var correctColor = ""
    private lateinit var timer: CountDownTimer
    private lateinit var mediaCorrect: MediaPlayer
    private lateinit var mediaWrong: MediaPlayer
    private lateinit var mediaBackground: MediaPlayer

    private val colors = listOf("Rojo", "Verde", "Azul", "Amarillo")
    private val colorValues = mapOf(
        "Rojo" to Color.RED,
        "Verde" to Color.GREEN,
        "Azul" to Color.BLUE,
        "Amarillo" to Color.YELLOW
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar sonidos
        mediaCorrect = MediaPlayer.create(requireContext(), R.raw.correct)
        mediaWrong = MediaPlayer.create(requireContext(), R.raw.wrong)
        mediaBackground = MediaPlayer.create(requireContext(), R.raw.music_bg)

        // Configurar música de fondo (looping)
        mediaBackground.isLooping = true
        mediaBackground.start()

        // Iniciar juego
        startGame()
        setupButtons()
    }

    private fun startGame() {
        score = 0
        updateScore()
        showNewColor()

        // Temporizador de 30 segundos
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerText.text = "Tiempo: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(score)
                findNavController().navigate(action)
            }
        }.start()
    }

    private fun updateScore() {
        binding.scoreText.text = "Puntos: $score"
    }

    private fun showNewColor() {
        correctColor = colors.random()
        val colorValue = colorValues[correctColor] ?: Color.BLACK
        binding.colorView.setBackgroundColor(colorValue)
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.btnRojo to "Rojo",
            binding.btnVerde to "Verde",
            binding.btnAzul to "Azul",
            binding.btnAmarillo to "Amarillo"
        )

        buttons.forEach { (button, colorName) ->
            button.setOnClickListener {
                if (colorName == correctColor) {
                    score++
                    mediaCorrect.start()
                } else {
                    mediaWrong.start()
                }
                updateScore()
                showNewColor()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        mediaCorrect.release()
        mediaWrong.release()
        mediaBackground.release() // Detener música al destruir el fragment
        _binding = null
    }
}

