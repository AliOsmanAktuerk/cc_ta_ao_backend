package de.aktuerk.brothers.cuc_ta_allaince_manager.utils;

import de.aktuerk.brothers.cuc_ta_allaince_manager.dto.CCInputDTO;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class MCVService {

    @Getter
    @Setter
    @Slf4j
    public static class MCV {
        private long researchPoints;
        private long credits;

        public MCV( long researchPoints, long credits) {
            this.researchPoints = researchPoints;
            this.credits = credits;
        }

    }

    private final Map<Integer, MCV> MVC_LIST = new HashMap<>();


    @PostConstruct
    public void init() {
        MVC_LIST.put(2, new MCV(1400000L, 1800000L));
        MVC_LIST.put(3, new MCV(9500000L, 12000000L));
        MVC_LIST.put(4, new MCV(44000000L, 60000000L));
        MVC_LIST.put(5, new MCV(170000000L, 250000000L));
        MVC_LIST.put(6, new MCV(750000000L, 1000000000L));
        MVC_LIST.put(7, new MCV(2670000000L, 3900000000L));
        MVC_LIST.put(8, new MCV(9800000000L, 14800000000L));
        MVC_LIST.put(9, new MCV(35000000000L, 52800000000L));
        MVC_LIST.put(10, new MCV(124000000000L, 184800000000L));
        MVC_LIST.put(11, new MCV(315000000000L, 530800000000L));
        MVC_LIST.put(12, new MCV(600000000000L, 1000000000000L));
        MVC_LIST.put(13, new MCV(1200000000000L, 2000000000000L));
        MVC_LIST.put(14, new MCV(2400000000000L, 4000000000000L));
        MVC_LIST.put(15, new MCV(5000000000000L, 8000000000000L));
        MVC_LIST.put(16, new MCV(12000000000000L, 16000000000000L));
    }
}
