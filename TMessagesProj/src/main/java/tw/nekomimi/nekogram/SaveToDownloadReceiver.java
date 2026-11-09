package tw.nekomimi.nekogram;

import android.content.Context;

public class SaveToDownloadReceiver {
    private static int nextId = 1000;

    public static int createNotificationId() {
        return nextId++;
    }

    public static void showNotification(Context context, int id, int count, Runnable onCancel) {
    }

    public static void updateNotification(int id, float progress) {
    }

    public static void cancelNotification(int id) {
    }
}
