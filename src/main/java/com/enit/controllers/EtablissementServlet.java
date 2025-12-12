// src/main/java/com/enit/controllers/EtablissementServlet.java
package com.enit.controllers;

import com.enit.entities.EtablissementPartenaire;
import com.enit.service.EtablissementPartenaireServiceLocal;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/api/etablissements/*")
public class EtablissementServlet extends HttpServlet {

    @EJB
    private EtablissementPartenaireServiceLocal etabService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String body = req.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        try {
            EtablissementPartenaire etab = parseEtablissement(body);

            // Validation obligatoire
            if (etab.getNom() == null || etab.getNom().trim().isEmpty()) {
                sendError(resp, 400, "Le champ 'nom' est obligatoire");
                return;
            }
            if (etab.getPays() == null || etab.getPays().trim().isEmpty()) {
                sendError(resp, 400, "Le champ 'pays' est obligatoire");
                return;
            }
            if (etab.getType() == null) {
                sendError(resp, 400, "Le champ 'type' est obligatoire (UNIVERSITE, ECOLE_SUPERIEURE, etc.)");
                return;
            }

            EtablissementPartenaire saved = etabService.create(etab);

            resp.setStatus(201);
            resp.getWriter().write(
                "{\"success\":true,\"message\":\"Établissement créé avec succès !\"," +
                "\"etablissement\":{\"id\":" + saved.getId() +
                ",\"nom\":\"" + escape(saved.getNom()) + "\",\"pays\":\"" + escape(saved.getPays()) + "\"}}"
            );

        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, 400, "Erreur : " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        List<EtablissementPartenaire> list = etabService.findAllActive();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            EtablissementPartenaire e = list.get(i);
            json.append(String.format("{\"id\":%d,\"nom\":\"%s\",\"sigle\":\"%s\",\"pays\":\"%s\",\"ville\":\"%s\",\"type\":\"%s\"}",
                    e.getId(),
                    escape(e.getNom()),
                    escape(e.getSigle() != null ? e.getSigle() : ""),
                    escape(e.getPays()),
                    escape(e.getVille() != null ? e.getVille() : ""),
                    e.getType()));
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");

        resp.getWriter().write("{\"success\":true,\"etablissements\":" + json + "}");
    }

    // Parsing JSON robuste avec regex (gère espaces, sauts de ligne, etc.)
    private EtablissementPartenaire parseEtablissement(String json) throws Exception {
        EtablissementPartenaire etab = new EtablissementPartenaire();

        etab.setNom(extractString(json, "nom"));
        etab.setSigle(extractString(json, "sigle"));
        etab.setPays(extractString(json, "pays"));
        etab.setVille(extractString(json, "ville"));
        etab.setAdresse(extractString(json, "adresse"));
        etab.setTelephone(extractString(json, "telephone"));
        etab.setEmail(extractString(json, "email"));
        etab.setSiteWeb(extractString(json, "siteWeb"));
        etab.setType(extractString(json, "type"));



        return etab;
    }

    private String extractString(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*?)\"";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write("{\"success\":false,\"message\":\"" + escape(message) + "\"}");
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}