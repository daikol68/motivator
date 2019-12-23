package de.daikol.motivator;

public interface Messages {

    String ENTITY_NOT_FOUND_BY_ID = "Das Objekt mit der angegebenen ID konnte nicht gefunden werden!";

    String PROGRESS_APPLY_NOT_ALLOWED = "Nur Teilnehmer des Wettbewerbs dürfen Fortschritt bearbeiten!";
    String USER_NOT_IN_COMPETITION = "User muss am Wettbewerb teilnehmen!";

    String USER_NOT_FOUND_BY_ID = "User mit ID konnte nicht gefunden werden!";
    String USER_NOT_FOUND_BY_NAME = "User mit Name konnte nicht gefunden werden!";
    String USER_NOT_FOUND_BY_EMAIL = "User mit Email Adresse konnte nicht gefunden werden!";

    String FILE_NOT_FOUND = "Die Datei konnte nicht gefunden werden!";

    String USER_NOT_FOUND_BY_EMAIL_AND_CODE = "User mit Email Adresse und dem Code konnte nicht gefunden werden!";

    String PROGRESS_ALREADY_CREATED = "Es wurde bereits ein Fortschritt gemeldet!";

    String NOT_FOUND = "Die angegebene Resource konnte nicht gefunden werden!";

    String NOT_PROVIDED = "Nicht alle notwendigen Daten konnten übermittelt werden!";

    String ILLEGAL_LOGINDATA = "Die Kombination aus Benutzernamen und Passwort ist nicht korrekt!";
    String ILLEGAL_LOGINDATA_PASSWORD = "Das Passwort ist nicht korrekt!";
    String NOT_ENOUGH_POINTS = "Sie haben leider nicht genug Punkte um die Belohung zu kaufen!";

    String NEWS_USER_REGISTRATION_START = "Herzlich willkommen beim Challenger! Klicken Sie auf den Link in der E-Mail um die Registrierung abzuschließen!";
    String NEWS_USER_REGISTRATION_COMPLETE = "Vielen Dank für die Registrierung! Starten Sie gleich den ersten Wettbewerb!";
    String NEWS_COMPETITION_CREATED = "Die Challenge %s wurde von %s erstellt!";
    String NEWS_COMPETITION_ACCEPTED = "Die Challenge %s wurde von %s angenommen!";
    String NEWS_COMPETITION_DECLINED = "Die Challenge %s wurde von %s abgelehnt!";
    String NEWS_COMPETITION_UPDATED = "Die Challenge %s wurde von %s verändert!";

    String NEWS_PROGRESS_CREATED = "Der Fortschritt beim Erfolg %s wurde von %s gemeldet!";
    String NEWS_PROGRESS_APPLIED = "Der Fortschritt beim Erfolg %s wurde von %s bestätigt!";
    String NEWS_PROGRESS_DECLINED = "Der Fortschritt beim Erfolg %s wurde von %s abgelehnt!";

    String NEWS_REWARD_BOUGHT = "In der Challenge %s wurde %s von %s gekauft!";
    String NO_RECEIVER = "Es wurde weder ein User noch ein Wettbewerb als Empfänger eingetragen!";
}
