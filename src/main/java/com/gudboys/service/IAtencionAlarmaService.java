package com.gudboys.service;

import com.gudboys.dto.request.AtenderAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;

public interface IAtencionAlarmaService {

    AlarmaResponseDTO atenderAlarma(Long alarmaId, AtenderAlarmaRequestDTO dto);
}
