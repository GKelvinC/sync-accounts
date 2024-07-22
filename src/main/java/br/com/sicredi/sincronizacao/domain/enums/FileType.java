package br.com.sicredi.sincronizacao.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    CSV("csv"),
    XML("xml"),
    JSON("json");

    private final String type;
}
