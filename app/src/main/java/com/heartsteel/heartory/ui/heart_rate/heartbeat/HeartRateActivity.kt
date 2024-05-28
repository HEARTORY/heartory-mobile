    package com.heartsteel.heartory.ui.heart_rate.heartbeat

    import android.Manifest
    import android.animation.ObjectAnimator
    import android.animation.PropertyValuesHolder
    import android.animation.ValueAnimator
    import android.annotation.SuppressLint
    import android.app.Activity
    import android.app.Dialog
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.graphics.drawable.ColorDrawable
    import android.os.Build
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.os.Message
    import android.util.Log
    import android.view.Gravity
    import android.view.Menu
    import android.view.MenuItem
    import android.view.Surface
    import android.view.TextureView
    import android.view.View
    import android.view.ViewGroup
    import android.view.Window
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.LinearLayout
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.result.ActivityResultLauncher
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.annotation.RequiresApi
    import androidx.appcompat.app.AppCompatActivity
    import androidx.appcompat.widget.AppCompatButton
    import androidx.compose.ui.graphics.Color
    import androidx.core.app.ActivityCompat
    import androidx.navigation.NavController
    import androidx.navigation.findNavController
    import androidx.navigation.fragment.NavHostFragment
    import com.google.android.material.snackbar.Snackbar
    import com.heartsteel.heartory.R
    import com.heartsteel.heartory.databinding.ActivityHeartRateBinding
    import com.heartsteel.heartory.ui.MainActivity
    import com.heartsteel.heartory.ui.heart_rate.result.ResultActivity


    public class HeartRateActivity : AppCompatActivity() {
        private lateinit var mainActivityLauncher: ActivityResultLauncher<Intent>

        private val REQUEST_CODE_CAMERA = 0
        val MESSAGE_UPDATE_REALTIME = 1
        val MESSAGE_UPDATE_FINAL = 2
        val MESSAGE_CAMERA_NOT_AVAILABLE = 3

//        private val MENU_INDEX_NEW_MEASUREMENT = 0
//        private val MENU_INDEX_EXPORT_RESULT = 1
//        private val MENU_INDEX_EXPORT_DETAILS = 2

        enum class VIEW_STATE {
            MEASUREMENT,
            SHOW_RESULTS
        }

        enum class EMOTION {
            NULL,
            JOY,
            HAPPY,
            SAD,
            CRY,
            NORMAL
        }

        private var emotion: EMOTION = EMOTION.NULL

        private lateinit var analyzer:OutputAnalyzer
        private lateinit var objectAnimator: ObjectAnimator
        private lateinit var floatingActionButton: View
        private lateinit var pulseValue: String
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
            val floatingActionButton = findViewById<View>(R.id.heart_beat_image)

            when (state) {
                VIEW_STATE.MEASUREMENT -> {
                        floatingActionButton.visibility = View.VISIBLE
                    (findViewById<View>(R.id.editText) as EditText).setText("Wait a second...")

                    objectAnimator.start()
                }

                VIEW_STATE.SHOW_RESULTS -> {
                        floatingActionButton.visibility = View.VISIBLE
                        objectAnimator.end()
                        showBottomDialog()
                }

                null -> TODO()
            }
        }

        fun extractPulseValue(input: String): String? {
            val regex = Regex("""Pulse:\s(\d+\.\d+)""")
            val matchResult = regex.find(input)
            return matchResult?.groups?.get(1)?.value
        }

        @SuppressLint("HandlerLeak")
        private val mainHandler: Handler = object : Handler(Looper.getMainLooper()) {
            @SuppressLint("SetTextI18n", "Recycle")
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_UPDATE_REALTIME) {
                    (findViewById<View>(R.id.textView) as TextView).text = msg.obj.toString()
                    pulseValue = extractPulseValue(msg.obj.toString()).toString()
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
                    setViewState(VIEW_STATE.SHOW_RESULTS)
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
            floatingActionButton = findViewById(R.id.heart_beat_image)
            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                floatingActionButton,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
            ).apply {
                duration = 500
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
            }
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
//            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//            navController = navHostFragment.navController
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
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            mainActivityLauncher.launch(intent)
            setResult(Activity.RESULT_CANCELED)
            finish() // This will remove the HeartRateActivity from the activity stack
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


        private fun showBottomDialog() {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.heart_rate_bottom_sheet_layout)
            val cancelButton = dialog.findViewById<AppCompatButton>(R.id.cancel_btn)
            val analysisButton = dialog.findViewById<AppCompatButton>(R.id.analysis_btn)

            val joyIcon = dialog.findViewById<ImageView>(R.id.joy_img)
            val happyIcon = dialog.findViewById<ImageView>(R.id.happy_img)
            val sadIcon = dialog.findViewById<ImageView>(R.id.sad_img)
            val cryIcon = dialog.findViewById<ImageView>(R.id.cry_img)
            val normalIcon = dialog.findViewById<ImageView>(R.id.normal_img)
            analysisButton.visibility = View.INVISIBLE

            joyIcon.setOnClickListener {
                // Set a border color when clicked
                joyIcon.setBackgroundResource(R.drawable.selected_icon_border)
                // Reset other icons' backgrounds
                happyIcon.setBackgroundResource(0) // Remove background
                sadIcon.setBackgroundResource(0)   // Remove background
                cryIcon.setBackgroundResource(0)   // Remove background
                normalIcon.setBackgroundResource(0) // Remove background

                // Update the selected emotion
                emotion = EMOTION.JOY
                analysisButton.visibility = View.VISIBLE

            }

            happyIcon.setOnClickListener {
                // Set a border color when clicked
                happyIcon.setBackgroundResource(R.drawable.selected_icon_border)

                // Reset other icons' backgrounds
                joyIcon.setBackgroundResource(0) // Remove background
                sadIcon.setBackgroundResource(0)   // Remove background
                cryIcon.setBackgroundResource(0)   // Remove background
                normalIcon.setBackgroundResource(0) // Remove background

                // Update the selected emotion
                emotion = EMOTION.HAPPY
                analysisButton.visibility = View.VISIBLE

            }

            sadIcon.setOnClickListener {
                // Set a border color when clicked
                sadIcon.setBackgroundResource(R.drawable.selected_icon_border)

                // Reset other icons' backgrounds
                joyIcon.setBackgroundResource(0) // Remove background
                happyIcon.setBackgroundResource(0) // Remove background
                cryIcon.setBackgroundResource(0) // Remove background
                normalIcon.setBackgroundResource(0) // Remove background

                // Update the selected emotion
                emotion = EMOTION.SAD
                analysisButton.visibility = View.VISIBLE

            }

            cryIcon.setOnClickListener {
                // Set a border color when clicked
                cryIcon.setBackgroundResource(R.drawable.selected_icon_border)

                // Reset other icons' backgrounds
                joyIcon.setBackgroundResource(0) // Remove background
                happyIcon.setBackgroundResource(0) // Remove background
                sadIcon.setBackgroundResource(0) // Remove background
                normalIcon.setBackgroundResource(0) // Remove background

                // Update the selected emotion
                emotion = EMOTION.CRY
                analysisButton.visibility = View.VISIBLE

            }

            normalIcon.setOnClickListener {
                // Set a border color when clicked
                normalIcon.setBackgroundResource(R.drawable.selected_icon_border)

                // Reset other icons' backgrounds
                joyIcon.setBackgroundResource(0) // Remove background
                happyIcon.setBackgroundResource(0) // Remove background
                sadIcon.setBackgroundResource(0) // Remove background
                cryIcon.setBackgroundResource(0) // Remove background

                // Update the selected emotion
                emotion = EMOTION.NORMAL
                analysisButton.visibility = View.VISIBLE

            }


            cancelButton.setOnClickListener { dialog.dismiss() }
            analysisButton.setOnClickListener{
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("SELECTED_EMOTION", emotion.toString()) // Passing the selected emotion
                intent.putExtra("PULSE_VALUE", pulseValue) // Passing the pulse value
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }



    }