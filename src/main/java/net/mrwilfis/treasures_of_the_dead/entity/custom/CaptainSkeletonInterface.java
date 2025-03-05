package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public interface CaptainSkeletonInterface {


    default String getRandomName(RandomSource random) {

        Minecraft minecraft = Minecraft.getInstance();
        String ClientLanguage = minecraft.getLanguageManager().getSelected().toLowerCase();

        boolean rus = (ClientLanguage.equals("ru_ru") || ClientLanguage.equals("ba_ru") || ClientLanguage.equals("be_by") || ClientLanguage.equals("ry_ua")
                || ClientLanguage.equals("tt_ru") || ClientLanguage.equals("rpr") || ClientLanguage.equals("uk_ua") || ClientLanguage.equals("kk_kz"));

        List<String> CaptainManNames = new ArrayList<String>();
        List<String> CaptainWomanNames = new ArrayList<String>();
        List<String> CaptainTitles1 = new ArrayList<String>();
        List<String> CaptainTitles2 = new ArrayList<String>();

        if (rus) {
            CaptainManNames.add("Дюк");
            CaptainManNames.add("Джони");
            CaptainManNames.add("Ник");
            CaptainManNames.add("Джек");
            CaptainManNames.add("Уолтер");
            CaptainManNames.add("Гектор");
            CaptainManNames.add("Уилл");
            CaptainManNames.add("Билл");
            CaptainManNames.add("Никита");
            CaptainManNames.add("Эдвард");
            CaptainManNames.add("Дэвид");
            CaptainManNames.add("Артур");
            CaptainManNames.add("Виктор");
            CaptainManNames.add("Брайан");
            CaptainManNames.add("Владимир");
            CaptainManNames.add("Арррон");
            CaptainManNames.add("Томас");
            CaptainManNames.add("Бэн");
            CaptainManNames.add("Борис");
            CaptainManNames.add("Габриэль");
            CaptainManNames.add("Джеймс");
        } else {
            CaptainManNames.add("Duke");
            CaptainManNames.add("Johny");
            CaptainManNames.add("Nick");
            CaptainManNames.add("Jack");
            CaptainManNames.add("Walter");
            CaptainManNames.add("Hector");
            CaptainManNames.add("Will");
            CaptainManNames.add("Bill");
            CaptainManNames.add("Nikita");
            CaptainManNames.add("Edward");
            CaptainManNames.add("David");
            CaptainManNames.add("Artur");
            CaptainManNames.add("Viktor");
            CaptainManNames.add("Brian");
            CaptainManNames.add("Vladimir");
            CaptainManNames.add("Aarrron");
            CaptainManNames.add("Thomas");
            CaptainManNames.add("Ben");
            CaptainManNames.add("Boris");
            CaptainManNames.add("Gabriel");
            CaptainManNames.add("James");
        }
        if (rus) {
            CaptainWomanNames.add("Элиза");
            CaptainWomanNames.add("Катарина");
            CaptainWomanNames.add("Барбара");
            CaptainWomanNames.add("Элизабетт");
            CaptainWomanNames.add("Марта");
            CaptainWomanNames.add("Мария");
            CaptainWomanNames.add("Карма");
            CaptainWomanNames.add("Камилла");
            CaptainWomanNames.add("Кейтлин");
            CaptainWomanNames.add("Джессика");
            CaptainWomanNames.add("Эвелин");
            CaptainWomanNames.add("Джуди");
            CaptainWomanNames.add("Ребекка");
        } else {
            CaptainWomanNames.add("Eliza");
            CaptainWomanNames.add("Katarina");
            CaptainWomanNames.add("Barbara");
            CaptainWomanNames.add("Elizabeth");
            CaptainWomanNames.add("Martha");
            CaptainWomanNames.add("Maria");
            CaptainWomanNames.add("Karma");
            CaptainWomanNames.add("Camille");
            CaptainWomanNames.add("Caitlin");
            CaptainWomanNames.add("Jessica");
            CaptainWomanNames.add("Evelyn");
            CaptainWomanNames.add("Judy");
            CaptainWomanNames.add("Rebecca");
        }
        //Titles 1 - always in the start of Name (Examples: FEARLESS Jack, DIRTY Walter). It is all adjectives
        if (rus) {
            CaptainTitles1.add("Великий");
            CaptainTitles1.add("Ужасный");
            CaptainTitles1.add("Бесстрашный");
            CaptainTitles1.add("Меткий");
            CaptainTitles1.add("Болтливый");
            CaptainTitles1.add("Весёлый");
            CaptainTitles1.add("Вредный");
            CaptainTitles1.add("Бедный");
            CaptainTitles1.add("Грязный");
            CaptainTitles1.add("Храбрый");
            CaptainTitles1.add("Ненавистный");
            CaptainTitles1.add("Обречённый");
            CaptainTitles1.add("Богатый");
            CaptainTitles1.add("Безумный");
            CaptainTitles1.add("Грозный");
            CaptainTitles1.add("Молодой");
            CaptainTitles1.add("Старина");
            CaptainTitles1.add("Молчаливый");
        } else {
            CaptainTitles1.add("Grand");
            CaptainTitles1.add("Horrible");
            CaptainTitles1.add("Fearless");
            CaptainTitles1.add("Accurate");
            CaptainTitles1.add("Chatty");
            CaptainTitles1.add("Jolly");
            CaptainTitles1.add("Vex");
            CaptainTitles1.add("Poor");
            CaptainTitles1.add("Dirty");
            CaptainTitles1.add("Brave");
            CaptainTitles1.add("Hateful");
            CaptainTitles1.add("Doomed");
            CaptainTitles1.add("Rich");
            CaptainTitles1.add("Crazy");
            CaptainTitles1.add("Fearsome");
            CaptainTitles1.add("Young");
            CaptainTitles1.add("Old");
            CaptainTitles1.add("Silent");
        }
        //Titles 2 - can be in the start or in the end of name (Examples: SEA WOLF Elizabeth, Duke THE KRAKEN SLAYER)
        if (rus) {
            CaptainTitles2.add("Морской Пёс");
            CaptainTitles2.add("Морской Волк");
            CaptainTitles2.add("Убийца Кракенов");
            CaptainTitles2.add("Пьянчуга");
            CaptainTitles2.add("Призрак");
            CaptainTitles2.add("Морская Крыса");
            CaptainTitles2.add("Грязная Крыса");
            CaptainTitles2.add("Трюмная Крыса");
            CaptainTitles2.add("Мясной Дробовик");
            CaptainTitles2.add("Длинный Хвост");
            CaptainTitles2.add("Мертвец");
            CaptainTitles2.add("Проломленный Череп");
            CaptainTitles2.add("Бродяга");
            CaptainTitles2.add("Сильная Душа");
        } else {
            CaptainTitles2.add("Sea Dog");
            CaptainTitles2.add("Sea Wolf");
            CaptainTitles2.add("Kraken slayer");
            CaptainTitles2.add("Drunkard");
            CaptainTitles2.add("Wrath");
            CaptainTitles2.add("Sea Rat");
            CaptainTitles2.add("Dirty Rat");
            CaptainTitles2.add("Bilge Rat");
            CaptainTitles2.add("Meat Shotgun");
            CaptainTitles2.add("Long Tail");
            CaptainTitles2.add("Dead Man");
            CaptainTitles2.add("Fractured Skull");
            CaptainTitles2.add("Wanderer");
            CaptainTitles2.add("Strong Soul");
        }

        String name;
        String title;
        String the = "";
        if (ClientLanguage.equals("en_au") || ClientLanguage.equals("en_ca") || ClientLanguage.equals("en_gb") || ClientLanguage.equals("en_nz") || ClientLanguage.equals("en_pt") || ClientLanguage.equals("en_ud") || ClientLanguage.equals("en_us")) {
            the = "The ";
        }
        if (rus) {
            the = "";
        }

        String fullName;


        if (random.nextFloat() < 0.7f) {
            name = CaptainManNames.get(random.nextInt(0, CaptainManNames.size()));
            if (random.nextFloat() < 0.5f) {
                // title1 + name
                title = CaptainTitles1.get(random.nextInt(0, CaptainTitles1.size()));
                fullName = title + " " + name;
            } else if (random.nextFloat() < 0.75f) {
                // title2 + name
                title = CaptainTitles2.get(random.nextInt(0, CaptainTitles2.size()));
                fullName = title + " " + name;
            } else {
                // name + the + title2
                title = CaptainTitles2.get(random.nextInt(0, CaptainTitles2.size()));
                fullName = name + " " + the + title;
            }
        } else {
            name = CaptainWomanNames.get(random.nextInt(0, CaptainWomanNames.size()));
            if (random.nextFloat() < 0.5f) {
                // title1 + name
                title = CaptainTitles1.get(random.nextInt(0, CaptainTitles1.size()));
                if (rus && title.charAt(title.length()-1) == 'й') {
                    // title = title.replace(title.substring(title.length() - 1), "");
                    // title = title.replace(title.substring(title.length() - 1), "");

                    title = title.substring(0, title.length() - 1);
                    title = title.substring(0, title.length() - 1);

                    title += "ая";
                }
                fullName = title + " " + name;
            } else if (random.nextFloat() < 0.75f) {
                // title2 + name
                title = CaptainTitles2.get(random.nextInt(0, CaptainTitles2.size()));
                fullName = title + " " + name;
            } else {
                // name + the + title2
                title = CaptainTitles2.get(random.nextInt(0, CaptainTitles2.size()));
                fullName = name + " " + the + title;
            }
        }

        return fullName;
    }
}
