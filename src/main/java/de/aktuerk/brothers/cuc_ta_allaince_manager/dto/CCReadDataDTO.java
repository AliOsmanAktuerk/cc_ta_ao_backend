package de.aktuerk.brothers.cuc_ta_allaince_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CCReadDataDTO {

    private List<CCAlliance> alliance;
    private List<CCPlayer> players;
    private List<CCCities> cities;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CCAlliance {
        private long date;
        private long id;
        private long rank;
        private String name;
        private long score;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CCPlayer {
        private String allianceName;
        private long date;
        private String name;
        private long rank;
        private double researchPoints;
        private double credits;
        private double creditProd;
        private boolean controlHub;
        private String nextBase;
        private int baseCount;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CCCities {
        private String allianceName;
        private long date;
        private String player;
        private String city;
        private double tib_prod;
        private double cry_prod;
        private double power_prod;
        private double tib_res;
        private double cry_res;
        private double power_res;
        private double off;
        private double def;
    }

}
