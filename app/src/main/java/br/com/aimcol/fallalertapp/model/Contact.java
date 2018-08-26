package br.com.aimcol.fallalertapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contact {
    private ContactType type;
    private String contact;
}
