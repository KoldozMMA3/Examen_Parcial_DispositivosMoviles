package com.example.myapplication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    // Obtener argumentos enviados desde GameFragment
    private val args: ResultFragmentArgs by navArgs()

    private lateinit var prefs: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = PreferenceManager(requireContext())

        val score = args.score
        val highScore = prefs.getHighScore()

        // Actualizar si hay un nuevo récord
        if (score > highScore) {
            prefs.saveHighScore(score)
            binding.textHighScore.text = "Nuevo record: $score puntos!"
        } else {
            binding.textHighScore.text = "Record actual: $highScore puntos"
        }

        binding.textResult.text = "Tu puntaje fue: $score"

        binding.textMessage.text = when {
            score >= 20 -> "¡Impresionante..... Eres muy habil!"
            score >= 10 -> "¡Buen trabajo lo hiciste genial!"
            else -> "Sigue practicando y lograras mejorar."
        }

        binding.btnReintentar.setOnClickListener {
            findNavController().navigate(ResultFragmentDirections.actionResultFragmentToWelcomeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


