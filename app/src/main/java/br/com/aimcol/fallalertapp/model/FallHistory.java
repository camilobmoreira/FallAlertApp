package br.com.aimcol.fallalertapp.model;

import java.util.List;

import br.com.aimcol.fallalertapp.dto.ElderlyDTO;
import lombok.Data;

@Data
public class FallHistory {
    public static final String FALL_HISTORY_JSON = "fallHistoryJson";

    private String key;
    private ElderlyDTO elderly;
    private List<Fall> falls;
}
