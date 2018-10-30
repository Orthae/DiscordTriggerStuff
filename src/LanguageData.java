import enums.Language;

import java.io.UnsupportedEncodingException;
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
//  This will not be needed in Java9+ since it supports UTF-8 resource bundle
        String text;
        try {
            text = new String (messages.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.getInstance().log("UnsupportedEncodingException while getting text from resource bundle");
            text = messages.getString(key);
        }
        return text;
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