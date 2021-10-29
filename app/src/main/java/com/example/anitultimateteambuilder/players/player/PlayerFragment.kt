package com.example.anitultimateteambuilder.players.player

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.anitultimateteambuilder.PlayerRarity
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.byteArrayToImage
import com.example.anitultimateteambuilder.database.AppDatabase
import com.example.anitultimateteambuilder.databinding.PlayerFragmentBinding
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.imageUriToByteArray
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PlayerFragment : Fragment() {

    private val REQUEST_CODE = 100
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        ivImage.setImageURI(uri)
        viewModel.image_uri = uri
    }


    private lateinit var viewModel: PlayerViewModel
    private lateinit var layout: View
    private lateinit var ivImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navController = findNavController()
        val binding = PlayerFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).DatabaseDao
        val viewModelFactory = PlayerViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerViewModel::class.java)

        binding.viewModel = viewModel

        val arguments = PlayerFragmentArgs.fromBundle(requireArguments())
        if (arguments.playerName.isEmpty()) {
            // Nothing happens
        } else {
            viewModel.getPlayerData(arguments.playerName)
        }

        binding.fab.setOnClickListener {
            GlobalScope.launch {
                viewModel.savePlayerData(Player(binding.tvName.text.toString(),binding.tvStats.text.toString().toInt(),
                    imageUriToByteArray(requireContext(),viewModel.image_uri)?: viewModel.image_ba,PlayerRarity.values()[binding.etRarity.text.toString().toInt()])
                )
            }

            navController.popBackStack()
        }

        viewModel.player.observe(viewLifecycleOwner){
            binding.tvName.setText(it.name)
            binding.tvStats.setText(it.stats.toString())
            binding.ivImage.setImageBitmap(byteArrayToImage(it.image))
            binding.etRarity.setText(it.rarity.ordinal.toString())
            viewModel.image_ba = it.image
        }

        binding.ivImage.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
            {
                openGalleryForImage()
            }
            else
            {
                onClickRequestPermission(it)
            }
        }

        layout = binding.playerLayout
        ivImage = binding.ivImage

        return binding.root
    }

    private fun openGalleryForImage() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, REQUEST_CODE)
        getContent.launch("image/*")
    }

    fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_INDEFINITE,
                    null
                ) {}
            }

            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }





}