package com.joaozao.picpaysimplificado.service;

import com.joaozao.picpaysimplificado.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction(User sender, BigDecimal amount) {
        // Faz uma requisição GET para a API de autorização e espera receber um Map como resposta
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) { //se a requisição foi bem sucedida
            // Extrai o campo "status" do JSON e converte para String
            String status = (String) authorizationResponse.getBody().get("status");

            // Extrai o objeto "data" do JSON e faz o cast para Map<String, Boolean>
            Map<String, Boolean> data = (Map<String, Boolean>) authorizationResponse.getBody().get("data");

            return "success".equalsIgnoreCase(status) && data.get("authorization");
        }
        else return false;
    }
}
