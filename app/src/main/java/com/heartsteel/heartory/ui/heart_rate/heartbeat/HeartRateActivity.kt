    package com.heartsteel.heartory.ui.heart_rate.heartbeat

    import android.Manifest
    import android.annotation.SuppressLint
    import android.app.Activity
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.os.Message
    import android.util.Log
    import android.view.Menu
    import android.view.MenuItem
    import android.view.Surface
    import android.view.TextureView
    import android.view.View
    import android.widget.EditText
    import android.widget.TextView
    import androidx.activity.result.ActivityResultLauncher
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity
    import androidx.appcompat.widget.Toolbar
    import androidx.core.app.ActivityCompat
    import com.google.android.material.snackbar.Snackbar
    import com.heartsteel.heartory.R
    import com.heartsteel.heartory.databinding.ActivityHeartRateBinding
    import com.heartsteel.heartory.ui.MainActivity
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale

    public class HeartRateActivity : AppCompatActivity() {
        private lateinit var mainActivityLauncher: ActivityResultLauncher<Intent>

        private val REQUEST_CODE_CAMERA = 0
        val MESSAGE_UPDATE_REALTIME = 1
        val MESSAGE_UPDATE_FINAL = 2
        val MESSAGE_CAMERA_NOT_AVAILABLE = 3

        private val MENU_INDEX_NEW_MEASUREMENT = 0
        private val MENU_INDEX_EXPORT_RESULT = 1
        private val MENU_INDEX_EXPORT_DETAILS = 2


        enum class VIEW_STATE {
            MEASUREMENT,
            SHOW_RESULTS
        }

        private lateinit var analyzer:OutputAnalyzer

//        fun setViewState(state: HeartRateActivity.VIEW_STATE?) {
//            val appMenu = (findViewById<View>(R.id.toolbar) as Toolbar).getMenu()
//            when (state) {
//                VIEW_STATE.MEASUREMENT -> {
//                    appMenu.getItem(MENU_INDEX_NEW_MEASUREMENT).setVisible(false)
//                    appMenu.getItem(MENU_INDEX_EXPORT_RESULT).setVisible(false)
//                    appMenu.getItem(MENU_INDEX_EXPORT_DETAILS).setVisible(false)
//                    findViewById<View>(R.id.floatingActionButton).visibility = View.INVISIBLE
//                }
//
//                VIEW_STATE.SHOW_RESULTS -> {
//                    findViewById<View>(R.id.floatingActionButton).visibility = View.VISIBLE
//                    appMenu.getItem(MENU_INDEX_EXPORT_RESULT).setVisible(true)
//                    appMenu.getItem(MENU_INDEX_EXPORT_DETAILS).setVisible(true)
//                    appMenu.getItem(MENU_INDEX_NEW_MEASUREMENT).setVisible(true)
//                }
//
//                null -> TODO()
//            }
//        }

        fun setViewState(state: HeartRateActivity.VIEW_STATE?) {
            when (state) {
                VIEW_STATE.MEASUREMENT -> {
                    val floatingActionButton = findViewById<View>(R.id.heart_beat_image)
                    if (floatingActionButton != null) {
                        floatingActionButton.visibility = View.VISIBLE
                    }
                }

                VIEW_STATE.SHOW_RESULTS -> {
                    val floatingActionButton = findViewById<View>(R.id.heart_beat_image)
                    if (floatingActionButton != null) {
                        floatingActionButton.visibility = View.VISIBLE
                    }
                }

                null -> TODO()
            }
        }


        @SuppressLint("HandlerLeak")
        private val mainHandler: Handler = object : Handler(Looper.getMainLooper()) {
            @SuppressLint("SetTextI18n")
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == MESSAGE_UPDATE_REALTIME) {
                    (findViewById<View>(R.id.textView) as TextView).text = msg.obj.toString()
                    (findViewById<View>(R.id.editText) as EditText).setText("We are measuring your blood pressure...")
                }
//                if (msg.what == MESSAGE_UPDATE_FINAL) {
//                    (findViewById<View>(R.id.editText) as EditText).setText(msg.obj.toString())
//                    Log.i("msg", msg.obj.toString())
//
//                    // make sure menu items are enabled when it opens.
//                    val appMenu = (findViewById<View>(R.id.toolbar) as Toolbar).getMenu()
//                    setViewState(VIEW_STATE.SHOW_RESULTS)
//                }
                if (msg.what == MESSAGE_UPDATE_FINAL) {
//                    val fullText = msg.obj.toString()
//                    val pulseValueStart = fullText.indexOf("Pulse: ") // Length of "Pulse: "
//                    val pulseValueEnd = fullText.indexOf(" (")
//                    if (pulseValueStart != -1 && pulseValueEnd != -1) {
//                        val pulseValue = fullText.substring(pulseValueStart, pulseValueEnd)
//                        (findViewById<View>(R.id.editText) as EditText).setText(pulseValue)
//                        setViewState(VIEW_STATE.SHOW_RESULTS)
//                    } else {
//                        Log.e("Pulse", "Unable to extract pulse value from the text: $fullText")
//                    }
                    (findViewById<View>(R.id.editText) as EditText).setText("Here your result")
                }
                if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                    Log.println(Log.WARN, "camera", msg.obj.toString())
                    (findViewById<View>(R.id.textView) as TextView).setText(
                        R.string.camera_not_found
                    )
                    analyzer.stop()
                }
            }
        }

        private var justShared = false

        private val cameraService = CameraService(this, mainHandler)

        override fun onResume() {
            super.onResume()
            analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler)
            val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
            if (cameraTextureView != null) {
                val previewSurfaceTexture = cameraTextureView.surfaceTexture


                // justShared is set if one clicks the share button.
                if (previewSurfaceTexture != null && !justShared) {
                    // this first appears when we close the application and switch back
                    // - TextureView isn't quite ready at the first onResume.
                    val previewSurface = Surface(previewSurfaceTexture)

                    // show warning when there is no flash
                    if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                        Snackbar.make(
                            findViewById(R.id.constraintLayout),
                            getString(R.string.noFlashWarning),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    // hide the new measurement item while another one is in progress in order to wait
                    // for the previous one to finish
//                    (findViewById<View>(R.id.toolbar) as Toolbar).getMenu()
//                        .getItem(MENU_INDEX_NEW_MEASUREMENT).setVisible(false)
                    cameraService.start(previewSurface)
                    analyzer.measurePulse(cameraTextureView, cameraService)
                }
            }
        }

        override fun onPause() {
            super.onPause()
            cameraService.stop()
            if (analyzer != null) analyzer.stop()
            analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler)
        }

        private val binding: ActivityHeartRateBinding by lazy {
            ActivityHeartRateBinding.inflate(layoutInflater)
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
            mainActivityLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_CANCELED) {
                    // Handle the result from HomeActivity if needed
                }
            }
            binding.topAppBar.setNavigationOnClickListener {
                navigateToHomeActivity()
            }
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA
            )
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, proceed with camera-related operations
                } else {
                    Snackbar.make(
                        findViewById(R.id.constraintLayout),
                        getString(R.string.cameraPermissionRequired),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
            Log.i("MENU", "menu is being prepared")
            val inflater = menuInflater
            inflater.inflate(R.menu.bottom_nav_menu, menu)
            return super.onPrepareOptionsMenu(menu)
        }


        private fun navigateToHomeActivity() {
            val intent = Intent(this, MainActivity::class.java)
            mainActivityLauncher.launch(intent)
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        fun onClickNewMeasurement(item: MenuItem?) {
            onClickNewMeasurement()
        }

        fun onClickNewMeasurement(view: View?) {
            onClickNewMeasurement()
        }

        fun onClickNewMeasurement() {
            analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler)

            // clear prior results
            val empty = CharArray(0)
            (findViewById<View>(R.id.editText) as EditText).setText(empty, 0, 0)
            (findViewById<View>(R.id.textView) as TextView).setText(empty, 0, 0)

            // hide the new measurement item while another one is in progress in order to wait
            // for the previous one to finish
            // Exporting results cannot be done, either, as it would read from the already cleared UI.
            setViewState(VIEW_STATE.MEASUREMENT)
            val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
            val previewSurfaceTexture = cameraTextureView.surfaceTexture
            if (previewSurfaceTexture != null) {
                // this first appears when we close the application and switch back
                // - TextureView isn't quite ready at the first onResume.
                val previewSurface = Surface(previewSurfaceTexture)
                cameraService.start(previewSurface)
                analyzer.measurePulse(cameraTextureView, cameraService)
            }
        }

        fun onClickExportResult(item: MenuItem?) {
            val intent =
                getTextIntent((findViewById<View>(R.id.textView) as TextView).getText() as String)
            justShared = true
            startActivity(Intent.createChooser(intent, getString(R.string.send_output_to)))
        }

        fun onClickExportDetails(item: MenuItem?) {
            val intent =
                getTextIntent((findViewById<View>(R.id.editText) as EditText).getText().toString())
            justShared = true
            startActivity(Intent.createChooser(intent, getString(R.string.send_output_to)))
        }

        private fun getTextIntent(intentText: String): Intent {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(
                Intent.EXTRA_SUBJECT, String.format(
                    getString(R.string.output_header_template),
                    SimpleDateFormat(
                        getString(R.string.dateFormat),
                        Locale.getDefault()
                    ).format(Date())
                )
            )
            intent.putExtra(Intent.EXTRA_TEXT, intentText)
            return intent
        }

    }