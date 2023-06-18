package backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticRequest {
    private Long user;
    private String car;
    private String startTime;
    private String endTime;
    private StatisticType type;
}
