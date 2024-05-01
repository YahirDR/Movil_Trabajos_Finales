package com.example.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class MyFirebaseNotificationService : FirebaseMessagingService() {

    // Define algunas constantes útiles para la gestión de notificaciones.
    companion object {
        private const val TAG = "FCM Notification"  // Etiqueta para registros
        const val DEFAULT_NOTIFICATION_ID = 0      // ID predeterminado para las notificaciones
        const val CHANNEL_ID = "1"                 // ID del canal de notificación
        const val CHANNEL_NAME = "Default"         // Nombre del canal de notificación
    }
    // Este método se llama cuando se genera un nuevo token de FCM.
    override fun onNewToken(token: String) {
        // Registra el nuevo token
        Log.i(TAG, "new FCM token created: $token")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Crea un canal de notificación
        createNotificationChannel(notificationManager)
    }
    // Este método se llama cuando se recibe un mensaje de notificación desde FCM.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        // Obtiene el servicio de gestión de notificaciones
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Construye la notificación
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
        // Muestra la notificación
        notificationManager.notify(DEFAULT_NOTIFICATION_ID, notificationBuilder.build())
    }
    // Este método crea un canal de notificación para versiones de Android Oreo (API nivel 26) y superiores.
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}

