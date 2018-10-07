import enums.Language;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageData {
    //Singleton
    private LanguageData() {
        if(Settings.getInstance() == null){
            changeLanguage(Language.English);
        } else {
            changeLanguage(Settings.getInstance().getLocale());
        }
    }

    public static LanguageData getInstance() {
        return LangDataHolder.INSTANCE;
    }

    public static class LangDataHolder {
        public static final LanguageData INSTANCE = new LanguageData();
    }

    //  Fields
    private ResourceBundle messages;

    //    Methods
    public String getMsg(String key) {
        return messages.getString(key);
    }

    public void changeLanguage(Language language) {
        final String BASE_NAME = "LanguageData";
        switch (language) {
            case English:
                messages = ResourceBundle.getBundle(BASE_NAME, new Locale("en"));
                break;
            case German:
                messages = ResourceBundle.getBundle(BASE_NAME, new Locale("de"));
                break;
            case French:
                messages = ResourceBundle.getBundle(BASE_NAME, new Locale("fr"));
                break;
            case Japanese:
                messages = ResourceBundle.getBundle(BASE_NAME, new Locale("ja"));
                break;
        }
    }
}