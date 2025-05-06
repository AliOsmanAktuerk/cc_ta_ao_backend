package de.aktuerk.brothers.cuc_ta_allaince_manager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aktuerk.brothers.cuc_ta_allaince_manager.dto.CCInputDTO;
import de.aktuerk.brothers.cuc_ta_allaince_manager.dto.CCReadDataDTO;
import de.aktuerk.brothers.cuc_ta_allaince_manager.utils.MCVService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class BrainService {

    private static final Map<String, CCInputDTO> BRAIN = new HashMap<>();
    private final MCVService mcvService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File jsonFile = new File("cc_ta_alliance.json");


    @PostConstruct
    public void init() {
        if (!jsonFile.exists()) {
            try {
                objectMapper.writeValue(jsonFile, new HashMap<String, Object>());
                log.info("Leere JSON-Datei erstellt.");
            } catch (IOException e) {
                log.error("Fehler beim Erstellen der JSON-Datei: ", e);
            }
        } else {
            try {
                Map<String, CCInputDTO> loadedBrain = objectMapper.readValue(
                        jsonFile,
                        objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, CCInputDTO.class)
                );
                BRAIN.putAll(loadedBrain);
                log.info("Daten aus JSON-Datei erfolgreich geladen. Einträge: {}", BRAIN.size());
            } catch (IOException e) {
                log.error("Fehler beim Laden der JSON-Datei: ", e);
            }
        }
    }

    public CCInputDTO getPlayer(String playerId) {
        return BRAIN.get(playerId);
    }

    public void updateBrain(String playerId, CCInputDTO ccInputDTO) {
        if (BRAIN.containsKey(playerId)) {
            BRAIN.replace(playerId, ccInputDTO);
        } else {
            BRAIN.put(playerId, ccInputDTO);
        }
    }

    public Map<String, CCInputDTO> getBrain() {
        return BRAIN;
    }

    @Scheduled(fixedRateString = "${app.save.intervall}")
    public void saveBrain() {
        try {
            log.info(jsonFile.getAbsolutePath());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, BRAIN);
            System.out.println("Map wurde erfolgreich in die Datei geschrieben.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public CCReadDataDTO getReadData() {
        List<CCReadDataDTO.CCAlliance> alliance = new ArrayList<>();
        List<CCReadDataDTO.CCPlayer> player = new ArrayList<>();
        List<CCReadDataDTO.CCCities> cities = new ArrayList<>();

        // read alliance
        BRAIN.forEach((playerId, playerData) -> {
            CCReadDataDTO.CCAlliance inputAlliance = new CCReadDataDTO.CCAlliance();
            CCInputDTO.Alliance rowAlliance = playerData.getAlliance();

            inputAlliance.setId(rowAlliance.getId());
            inputAlliance.setName(rowAlliance.getName());
            inputAlliance.setScore(rowAlliance.getScore());
            inputAlliance.setRank(rowAlliance.getRank());
            inputAlliance.setDate(playerData.getDate());

            var getAllianceInListByName = alliance.stream().filter(a -> a.getName().equals(rowAlliance.getName())).findFirst();

            if (getAllianceInListByName.isPresent()) {
                var item = getAllianceInListByName.get();
                if (inputAlliance.getDate() > item.getDate()) {
                    // remove all alliance with same name
                    alliance.remove(item);
                    alliance.add(inputAlliance);
                }
            } else {
                alliance.add(inputAlliance);
            }

        });

        // read all player
        BRAIN.forEach((playerId, playerData) -> {
            CCReadDataDTO.CCPlayer inputPlayer = new CCReadDataDTO.CCPlayer();

            inputPlayer.setDate(playerData.getDate());
            inputPlayer.setName(playerData.getPlayer());
            inputPlayer.setRank(playerData.getRank());
            inputPlayer.setResearchPoints(playerData.getResearchPoints());
            inputPlayer.setCredits(playerData.getCredits());
            inputPlayer.setCreditProd(playerData.getCreditProductionAllBases());
            inputPlayer.setControlHub(playerData.isControlHubCode());
            inputPlayer.setAllianceName(playerData.getAlliance().getName());
            inputPlayer.setBaseCount(playerData.getCities().size());

            // calculate next base
            int countOfBases = playerData.getCities().size();
            if (countOfBases >= mcvService.getMVC_LIST().size()) {
                inputPlayer.setNextBase("can not calculate! Only support 16 Base");
            } else {
                MCVService.MCV nextBaseInfo = mcvService.getMVC_LIST().get(countOfBases + 1);
                var cashProductionInAnHour = playerData.getCreditProductionAllBases(); // 1 Stunde = 1x Produktion

                double hoursLeft = (double) (nextBaseInfo.getCredits() - playerData.getCredits()) / cashProductionInAnHour;

                long totalMinutesLeft = (long) Math.ceil(hoursLeft * 60); // alles in Minuten umrechnen

                long days = totalMinutesLeft / (24 * 60);
                long hours = (totalMinutesLeft % (24 * 60)) / 60;
                long minutes = totalMinutesLeft % 60;

                // Stunden und Minuten zweistellig formatieren
                String formattedHours = String.format("%02d", hours);
                String formattedMinutes = String.format("%02d", minutes);

                // Text zusammensetzen
                StringBuilder timeLeftBuilder = new StringBuilder();
                if (days > 0) {
                    timeLeftBuilder.append(days).append(days == 1 ? " day " : " days ");
                }
                timeLeftBuilder.append(formattedHours).append(" hours ");
                timeLeftBuilder.append(formattedMinutes).append(" minutes left");

                String leftTimeForNextBase = timeLeftBuilder.toString().trim();

                inputPlayer.setNextBase(leftTimeForNextBase);
            }


            player.add(inputPlayer);
        });

        // read citeis
        BRAIN.forEach((playerId, playerData) -> {
            playerData.getCities().forEach(city -> {
                CCReadDataDTO.CCCities inputCities = new CCReadDataDTO.CCCities();
                inputCities.setDate(playerData.getDate());
                inputCities.setPlayer(playerData.getPlayer());
                inputCities.setCity(city.getName());
                inputCities.setTib_prod(city.getProduction().getTiberium());
                inputCities.setCry_prod(city.getProduction().getCrystal());
                inputCities.setPower_prod(city.getProduction().getPower());
                inputCities.setTib_res(city.getResource().getTiberium());
                inputCities.setCry_res(city.getResource().getCrystal());
                inputCities.setPower_res(city.getResource().getPower());
                inputCities.setOff(city.getOff());
                inputCities.setDef(city.getDef());
                inputCities.setAllianceName(playerData.getAlliance().getName());

                cities.add(inputCities);
            });
        });

        alliance.sort((a1, a2) -> a1.getRank() < a2.getRank() ? -1 : 1);
        player.sort((a1, a2) -> a1.getRank() < a2.getRank() ? -1 : 1);
        cities.sort((a1, a2) -> a1.getOff() > a2.getOff() ? -1 : 1);


        return new CCReadDataDTO(alliance, player, cities);
    }

}
