package com.cashlessride.booking.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cashlessride.booking.R
import com.cashlessride.booking.manager.APIManager
import com.cashlessride.booking.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import java.net.HttpURLConnection


private const val LOG_TAG = "BookingFMS"

/**
 * Created on 6/5/2019.
 */
class BookingFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.tag(LOG_TAG).d("From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
//        remoteMessage?.data?.isNotEmpty()?.let {
//            Timber.tag(LOG_TAG).d("Message data payload: %s", remoteMessage.data)
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
//        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Timber.tag(LOG_TAG).d("Message Notification Body: ${it.body}")

            sendNotification(it)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Timber.tag(LOG_TAG).d("Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        val apiManager = APIManager.getInstance(this)

        apiManager.updatePushToken(token){ response ->
            if (response.success == true) {
                Timber.tag(LOG_TAG).d("Push token updated")
            } else {
                if (response.status == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Timber.tag(LOG_TAG).d("HTTP_UNAUTHORIZED")
                } else {
                    Timber.tag(LOG_TAG).e(response.error)
                }
            }
        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        val backIntent = Intent(this, MainActivity::class.java)
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val intent = Intent(notification.clickAction)

        val pendingIntent = PendingIntent.getActivities (
            this,
            822 /* Request code */,
            arrayOf(backIntent, intent),
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}