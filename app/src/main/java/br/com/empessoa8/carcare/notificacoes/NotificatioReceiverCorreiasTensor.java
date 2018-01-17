package br.com.empessoa8.carcare.notificacoes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.fragments.arrefecimento_freio.Act_Arrefecimento_Freio;
import br.com.empessoa8.carcare.fragments.correia_tensor.Act_Correias_Tensor;


/**
 * Created by elias on 16/08/2017.
 */

public class NotificatioReceiverCorreiasTensor extends BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context, "Manutenção Preventiva", "Correias e Tensor", "Car Care");
    }
    private void createNotification(Context context, String msg, String msgText, String msgAlert) {
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Act_Correias_Tensor.class), 0);
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_carcare)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);
        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
