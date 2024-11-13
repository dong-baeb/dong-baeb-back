package com.dongbaeb.demo.member.domain;

import com.dongbaeb.demo.global.exception.BadRequestException;
import java.util.Arrays;

public enum University {

    KONKUK("건국대학교", "건국대", "건국"),
    JANGSIN("장로회신학대학교", "장신대", "장신"),
    KWANGWOON("광운대학교", "광운대", "광운"),
    SIRIB("서울시립대학교", "시립대", "시립"),
    DONGDUK("동덕여자대학교", "동덕여대", "동덕"),
    SEJONG("세종대학교", "세종대", "세종"),
    GWAGI("서울과학기술대학교", "과기대", "과기"),
    SEORYEO("서울여자대학교", "서울여대", "설여"),
    ASIN("아세아연합신학대학교", "아신대", "아신"),
    SAMYUK("삼육대학교", "삼육대", "삼육"),
    SINGU("신구대학교", "신구대", "신구"),
    GACHON("가천대학교", "가천대", "가천"),
    EULJI("을지대학교", "을지대", "을지"),
    SEOLJANG("서울장신대학교", "서울장신대", "설장"),
    OEDAE("한국외국어대학교", "한국외대", "외대"),
    HANYANG("한양대학교", "한양대", "한양"),
    HANYEO("한양여자대학교", "한양여대", "한여");

    private final String name;
    private final String shortName;
    private final String abbreviation;

    University(String name, String shortName, String abbreviation) {
        this.name = name;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
    }

    public static University fromName(String name) {
        return Arrays.stream(values())
                .filter(university -> name.equals(university.name))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("동서울 IVF에 존재하지 않는 대학교 이름입니다."));
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}

