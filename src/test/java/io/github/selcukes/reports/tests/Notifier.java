package io.github.selcukes.reports.tests;


import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.selcukes.reports.notification.MicrosoftTeams;

public class Notifier {
    public static void post(String message){
        try {
            MicrosoftTeams.forUrl(() -> {
                return "https://outlook.office.com/webhook/8b7ae3b2-ef21-4caa-a770-f3e5f5dc67ac@55e374bf-374e-49de-a716-836ce6f714d1/IncomingWebhook/bd7cf6c116354991afa50691e45bde16/e4eb5b1c-adff-4433-9fb1-9b1611fabe32"; //$NON-NLS-1$
            }).sendMessage(message);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
