
import com.ibm.icu.text.Transliterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.ls.LSOutput;

import javax.print.Doc;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class weatherParser {

    private static Pattern patternRegion = Pattern.compile("([^a-zA-Z])\\D{2,}");
    private static Pattern patternTime = Pattern.compile("\\d{2}\\:\\d{2}");
    private static Pattern patternTemperature = Pattern.compile("\\D?\\d+");

    public static void main(String[] args) {
        WeatherGUI weatherApp = new WeatherGUI();
        weatherApp.setVisible(true);
    }

    private static Document getPage(JTextField input) throws Exception {
        String url = "https://yandex.ru/pogoda/" + input.getText().replaceAll(" ", "-");
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    @lombok.SneakyThrows
    public static String doParser(JTextField input) {
        Document page = getPage(input);
        Element region = page.select("ol[class=breadcrumbs__list]").first();
        Element timeFromTable = page.select("time[class=time fact__time]").first();
        Element temperatureNow = page.select("span[class=temp__value]").get(1);
        Element temperatureYesterday = page.select("span[class=temp__value]").get(0);
        Element temperatureLike = page.select("span[class=temp__value]").get(2);
        String temperatureFeeling = getTemperatureFromString(temperatureLike.text());
        String clothesSuggestion = recommendClothes(temperatureFeeling);

        return "Погода в " + getRegionFromString(region.text()) + "\n" +
                "Время сейчас: " + getTimeFromString(timeFromTable.text()) + "\n" +
                "Вчера температура была: " + getTemperatureFromString(temperatureYesterday.text()) + "\n" +
                "Температура сейчас: " + getTemperatureFromString(temperatureNow.text()) + "\n" +
                "Ощущается как: " + temperatureFeeling + "\n" +
                "В связи с тем, что погода ощущается как " + temperatureFeeling + "\n" +
                clothesSuggestion + "\n" + "\n" +
                "Исходные данные: ООО \"Яндекс.Пробки\"; Росгидромет, ФГБУ \"ЦАО\"; \u00A9 2018 ECMWF; ООО \"Аэростейт\"\n" +
                "\u00A9 2000–2020  ООО \"ЯНДЕКС\"";
    }


    private static String getTimeFromString(String stringTime) throws Exception {
        Matcher matcherTime = patternTime.matcher(stringTime);

        if (matcherTime.find()) {
            return matcherTime.group();
        }
        throw new Exception("Can't extract time from this string");
    }

    private static String getTemperatureFromString(String stringTemp) throws Exception {
        Matcher matcherTemp = patternTemperature.matcher(stringTemp);

        if (matcherTemp.find()) {
            return matcherTemp.group();
        }
        throw new Exception("Can't extract temperature from this string");
    }

    private static String getRegionFromString(String stringRegion) throws Exception {
        Matcher matcherRegion = patternRegion.matcher(stringRegion);

        if (matcherRegion.find()) {
            return matcherRegion.group();
        }
        throw new Exception("Can't extract region from this string");
    }

    private static String recommendClothes(String temperatureLike) throws Exception {

        int tempFromStringToInt = Integer.valueOf(temperatureLike.replaceAll("−", "-"));

        if (tempFromStringToInt <= (-40)) {
            return "Пожалуйста, оставайтесь дома. Температура ниже 40 градусов. Если все-таки решите выйти на улицу, наденьте очень теплую зимнюю одежду.";
        } else if (tempFromStringToInt < 0 && tempFromStringToInt > (-40)) {
            return "Отдайте предпочтение очень теплой зимней одежде.";
        } else if (tempFromStringToInt >= 0 && tempFromStringToInt < 20) {
            return "Мы рекомендуем Вам надеть весеннюю / осеннюю одежду. ";
        } else if (tempFromStringToInt >= 20 && tempFromStringToInt <= 40) {
            return "Советуем выбрать летний гардероб. На улице достаточно тепло. ";
        } else if (tempFromStringToInt > 40) {
            return "Пожалуйста, лучше оставайтесь дома,  на улице экстремально жарко.Если все-таки решите выйти на улицу, не забудьте головной убор и воспользуйтесь солнцезащитным кремом.";
        }
        throw new Exception("Can't extract temperature correctly");
    }
}
