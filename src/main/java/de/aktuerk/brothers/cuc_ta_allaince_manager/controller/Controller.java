package de.aktuerk.brothers.cuc_ta_allaince_manager.controller;

import de.aktuerk.brothers.cuc_ta_allaince_manager.dto.CCInputDTO;
import de.aktuerk.brothers.cuc_ta_allaince_manager.dto.CCOutputDTO;
import de.aktuerk.brothers.cuc_ta_allaince_manager.dto.CCReadDataDTO;
import de.aktuerk.brothers.cuc_ta_allaince_manager.service.BrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {

    @Value("${app.pwd}")
    public String pwd;
    @Value("${app.allowed.world}")
    public int worldId;

    private final BrainService brainService;

    @Autowired
    public Controller(BrainService brainService) {
        this.brainService = brainService;
    }

    @CrossOrigin(origins = {"https://cncapp09.alliances.commandandconquer.com", "http://localhost"})
    @PostMapping("/input")
    public ResponseEntity<String> receivePlayerData(@RequestBody CCInputDTO playerData) {
        try {
            if (playerData.getWorldId() != worldId) {
                log.warn("World is not yet supported: {}, player : {} ", playerData.getWorldId(), playerData.getPlayer());
                return new ResponseEntity<>("{\"message\" :\"World is not yet supported\"}", HttpStatus.UNAUTHORIZED);
            }

            brainService.updateBrain(playerData.getPlayer(), playerData);
            return new ResponseEntity<>("{\"message\" :\"Daten erfolgreich empfangen\"}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin(origins = "${app.open.api.for.ip.read}")
    @GetMapping("/output/{pwd}")
    public ResponseEntity<String> getOutput(@PathVariable("pwd") String pwd) {
        if (!pwd.equals(this.pwd)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new CCOutputDTO(brainService.getBrain()).toString(), HttpStatus.OK);
    }


    @CrossOrigin(origins = "${app.open.api.for.ip.read}")
    @GetMapping("/read/data/{pwd}")
    public ResponseEntity<CCReadDataDTO> readData(@PathVariable("pwd") String pwd) {
        if (!pwd.equals(this.pwd)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(brainService.getReadData(), HttpStatus.OK);
    }
}
