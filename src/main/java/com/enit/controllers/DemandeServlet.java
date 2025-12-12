package com.enit.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.enit.entities.Demande;
import com.enit.entities.EtablissementPartenaire;
import com.enit.entities.User;
import com.enit.service.DemandeServiceLocal;
import com.enit.service.EtablissementPartenaireServiceLocal;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/demandes/*")
public class DemandeServlet extends HttpServlet {

    @EJB private DemandeServiceLocal demandeService;
    @EJB private EtablissementPartenaireServiceLocal etabService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        User user = getCurrentUser(req);
        if (user == null || !"ENSEIGNANT".equals(user.getRole())) {
            sendError(resp, 401, "Accès refusé");
            return;
        }

        String body = readBody(req);
        try {
            Demande d = new Demande();
            d.setTitre(getValue(body, "titre"));
            d.setTypeConvention(getValue(body, "type"));
            
            d.setObjectif(getValue(body, "objectif"));
            d.setDescription(getValue(body, "description"));
            d.setCommentaires(getValue(body, "commentaires"));

            String etabIdStr = getValue(body, "etablissementPartenaireId");
            if (etabIdStr == null) {
                sendError(resp, 400, "etablissementPartenaireId manquant");
                return;
            }
            Long etabId = Long.parseLong(etabIdStr);
            EtablissementPartenaire etab = etabService.findById(etabId);
            if (etab == null) {
                sendError(resp, 400, "Établissement non trouvé");
                return;
            }
            d.setEtablissementPartenaire(etab);

            Demande saved = demandeService.createDemande(d, user);

            resp.setStatus(201);
            resp.getWriter().write("{\"success\":true,\"message\":\"Demande soumise !\",\"demande\":{\"id\":" + saved.getId() +
                ",\"reference\":\"" + saved.getReference() + "\"}}");

        } catch (Exception e) {
            sendError(resp, 400, "Erreur : " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        User user = getCurrentUser(req);
        if (user == null || !"ENSEIGNANT".equals(user.getRole())) {
            sendError(resp, 401, "Non authentifié");
            return;
        }

        List<Demande> demandes = demandeService.findDemandesByEnseignant(user);
        String json = demandes.stream().map(this::toJson).collect(java.util.stream.Collectors.joining(",", "[", "]"));
        resp.getWriter().write("{\"success\":true,\"demandes\":" + json + "}");
    }

    private String toJson(Demande d) {
        return String.format("{\"id\":%d,\"reference\":\"%s\",\"titre\":\"%s\",\"statut\":\"%s\"," +
                "\"dateSoumission\":\"%s\",\"typeConvention\":\"%s\"}",
            d.getId(), d.getReference(), escape(d.getTitre()),
            d.getStatut(), d.getDateSoumission().toString(), d.getTypeConvention());
    }

    private User getCurrentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null ? (User) s.getAttribute("user") : null;
    }

    private String readBody(HttpServletRequest req) throws IOException {
        return req.getReader().lines().collect(java.util.stream.Collectors.joining());
    }

    private String getValue(String json, String key) {
        String k = "\"" + key + "\":\"";
        int i = json.indexOf(k);
        if (i == -1) return null;
        int start = i + k.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? null : json.substring(start, end);
    }

    private void sendError(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write("{\"success\":false,\"message\":\"" + escape(msg) + "\"}");
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}