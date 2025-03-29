package com.kstraupenieks.asca

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class HomeFragment : Fragment() {

    private var selectedFileUri: Uri? = null
    private lateinit var tvSelectedFile: TextView
    private lateinit var tvTranscriptionResult: TextView
    private lateinit var btnTranscribe: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Welcome message
        val sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "User")
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "Welcome, $username!"

        val btnSelectFile = view.findViewById<Button>(R.id.btnSelectFile)
        btnTranscribe = view.findViewById(R.id.btnTranscribe)
        tvSelectedFile = view.findViewById(R.id.tvSelectedFile)
        tvTranscriptionResult = view.findViewById(R.id.tvTranscriptionResult)

        btnTranscribe.isEnabled = false

        val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedFileUri = uri
                val fileName = getFileName(requireContext(), uri)
                tvSelectedFile.text = "Selected: $fileName"
                btnTranscribe.isEnabled = true
            }
        }

        btnSelectFile.setOnClickListener {
            filePicker.launch("audio/mpeg")
        }

        btnTranscribe.setOnClickListener {
            selectedFileUri?.let { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    val transcription = transcribeWithOpenAI(requireContext(), uri)
                    val classification = classifyWithOpenAI(transcription)

                    withContext(Dispatchers.Main) {
                        val textOnly = JSONObject(transcription).getString("text")
                        tvTranscriptionResult.text = """
                    ✅ Transcription:
                    $textOnly
                    
                    ⚠️ Possible scammer? → $classification
                """.trimIndent()

                        if (classification.equals("yes", ignoreCase = true)) {
                            showScamAlertNotification()
                        }
                    }
                }
            }
        }



        return view
    }

    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && it.moveToFirst()) {
                    result = it.getString(nameIndex)
                }
            }
        }
        return result ?: uri.lastPathSegment ?: "unknown_file"
    }

    private fun transcribeWithOpenAI(context: Context, audioUri: Uri): String {
        val apiKey = Constants.API_KEY // 🔐 Replace with your actual OpenAI API key
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(audioUri) ?: return "File not found"
        val fileBytes = inputStream.readBytes()

        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "audio.mp3",
                fileBytes.toRequestBody("audio/mpeg".toMediaTypeOrNull())
            )
            .addFormDataPart("model", "whisper-1")
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/audio/transcriptions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: "No response from OpenAI"
    }
    fun classifyWithOpenAI(transcription: String): String {
        val apiKey = Constants.API_KEY // Replace with your actual OpenAI key

        val client = OkHttpClient()

        val prompt = "Scam call? Reply Yes or No.\n\n$transcription"

        val jsonBody = JSONObject()
        jsonBody.put("model", "gpt-3.5-turbo")

        val messages = org.json.JSONArray()
        messages.put(JSONObject().put("role", "system").put("content", "Reply only Yes or No if it's a scam."))
        messages.put(JSONObject().put("role", "user").put("content", prompt))
        jsonBody.put("messages", messages)

        jsonBody.put("max_tokens", 5)
        jsonBody.put("temperature", 0.2)

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        val rawResponse = response.body?.string() ?: return "No response"
        val json = JSONObject(rawResponse)
        return json.getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")
            .trim()
    }
    private fun showScamAlertNotification() {
        val channelId = "scam_alert_channel"
        val notificationId = 1

        // Check if we have permission before showing notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted — don't show notification
                return
            }
        }

        // Create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Scam Alert"
            val descriptionText = "Alerts for possible scam calls"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build and show the notification
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠️ Scam Call Detected")
            .setContentText("Open the app to review the call.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationId, builder.build())
        }
    }




}
