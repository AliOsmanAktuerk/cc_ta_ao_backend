package de.aktuerk.brothers.cuc_ta_allaince_manager.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CCInputDTO {

    private int worldId;
    private String worldName;
    private long date;
    private long researchPoints;
    private double credits;
    private double creditProductionAllBases;
    private Alliance alliance;
    private List<City> cities;
    private String player;
    private int rank;
    private boolean controlHubCode;

    // Getter und Setter

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Alliance {
        private int id;
        private int rank;
        private String name;
        private int score;

        // Getter und Setter
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class City {
        private String id;
        private String name;
        private Production production;
        private Resource resource;
        private double off;
        private double def;
        // Getter und Setter
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Production {
        private double tiberium;
        private double crystal;
        private double power;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resource {
        private double tiberium;
        private double crystal;
        private double power;
    }

    // Getter und Setter
}
