//package com.msimplelogic.adapter;
//
//import com.msimplelogic.activities.R;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class GenreDataFactory {
//    public static List<Genre> makeGenres() {
//        return Arrays.asList(
//                makeClassicGenre(),
//                makeSalsaGenre());
//
//    }
//
//
//    public static Genre makeClassicGenre() {
//        return new Genre("ORDER", makeClassicArtists(),1);
//    }
//
//
//
//    public static List<Artist> makeClassicArtists() {
//        Artist beethoven = new Artist("Ludwig van Beethoven", false,"25 jan 2020");
//        Artist bach = new Artist("Johann Sebastian Bach", true,"25 jan 2020");
//        Artist brahms = new Artist("Johannes Brahms", false,"25 jan 2020");
//        Artist puccini = new Artist("Giacomo Puccini", false,"25 jan 2020");
//
//        return Arrays.asList(beethoven, bach, brahms, puccini);
//    }
//
//    public static Genre makeSalsaGenre() {
//        return new Genre("Return Order", makeSalsaArtists(), 1);
//    }
//
//
//
//    public static List<Artist> makeSalsaArtists() {
//        Artist hectorLavoe = new Artist("Hector Lavoe", true,"25 jan 2020");
//        Artist celiaCruz = new Artist("Celia Cruz", false,"25 jan 2020");
//        Artist willieColon = new Artist("Willie Colon", false,"25 jan 2020");
//        Artist marcAnthony = new Artist("Marc Anthony", false,"25 jan 2020");
//
//        return Arrays.asList(hectorLavoe, celiaCruz, willieColon, marcAnthony);
//    }
//
//
//
//
//}
