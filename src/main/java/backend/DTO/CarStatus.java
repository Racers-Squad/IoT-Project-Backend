package backend.DTO;

import lombok.Getter;

@Getter
public enum CarStatus {
    RESERVED("Занято"), FREE("Свободно");

    private final String label;

    CarStatus(String label) {
        this.label = label;
    }

}
