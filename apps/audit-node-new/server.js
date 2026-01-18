import express from "express";
import { validateUrl, runAudit } from "./lighthouseAudit.js";

const app = express();

/**
 * Middleware JSON
 * Permet de lire req.body en JSON
 */
app.use(express.json());

/**
 * Endpoint principal d'audit
 * POST /audit
 *
 * Body attendu :
 * {
 *   "url": "https://example.com"
 * }
 */
app.post("/audit", async (req, res) => {

  const { url } = req.body;

  /**
   * Validation rapide de la présence de l'URL
   */
  if (!url) {
    return res.status(400).json({
      error: "url is required"
    });
  }

  try {
    /**
     * Validation et normalisation de l'URL
     * - format valide
     * - http / https uniquement
     */
    const validatedUrl = validateUrl(url);

    /**
     * Lancement de l'audit Lighthouse
     * Retourne :
     * {
     *   url,
     *   scores,
     *   vitals
     * }
     */
    const auditResult = await runAudit(validatedUrl);

    /**
     * Réponse HTTP claire et stable
     * ⚠️ On renvoie directement le résultat Lighthouse structuré
     * sans le retransformer ici
     */
    return res.status(200).json(auditResult);

  } catch (err) {
    /**
     * Gestion d'erreur centralisée
     * Toujours renvoyer un message clair au backend
     */
    console.error("❌ Audit error :", err.message);

    return res.status(400).json({
      error: err.message
    });
  }
});

/**
 * Démarrage du serveur HTTP
 */
const PORT = 3001;
app.listen(PORT, () => {
  console.log(`🚀 Audit HTTP server running on port ${PORT}`);
});
