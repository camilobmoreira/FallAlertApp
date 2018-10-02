package br.com.aimcol.fallalertapp.model;

import java.util.List;

import br.com.aimcol.fallalertapp.dto.ElderlyDTO;
import lombok.Data;

@Data
public class FallHistory {

    private ElderlyDTO elderlyDTO;
    private List<Fall> falls;
}
