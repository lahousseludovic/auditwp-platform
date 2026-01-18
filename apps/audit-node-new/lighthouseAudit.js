import lighthouse from "lighthouse";
import puppeteer from "puppeteer";
import { URL } from "url";

/**
 * Vérifie et normalise une URL fournie par l'utilisateur
 * - Doit être valide
 * - Doit commencer par http:// ou https://
 */
export function validateUrl(input) {
  try {
    const url = new URL(input);

    // Sécurité : on n'autorise que HTTP / HTTPS
    if (!["http:", "https:"].includes(url.protocol)) {
      throw new Error();
    }

    // On retourne l'URL normalisée
    return url.toString();
  } catch {
    throw new Error("Invalid URL");
  }
}

/**
 * Lance un audit Lighthouse complet sur une URL
 * Retourne :
 * - Scores globaux (performance, seo, accessibility, best practices)
 * - Web Vitals détaillés (LCP, CLS, FCP, TBT, etc.)
 */
export async function runAudit(targetUrl) {

  // Lancement de Chromium via Puppeteer
  // Important pour Docker : --no-sandbox obligatoire
  const browser = await puppeteer.launch({
    headless: true,
    args: [
      "--no-sandbox",
      "--disable-setuid-sandbox"
    ]
  });

  try {
    console.log("🔍 Audit Lighthouse en cours pour :", targetUrl);

    // Récupération du port Chromium utilisé par Puppeteer
    const port = new URL(browser.wsEndpoint()).port;

    // Lancement de Lighthouse
    const { lhr } = await lighthouse(targetUrl, {
      port,
      output: "json",
      onlyCategories: [
        "performance",
        "seo",
        "accessibility",
        "best-practices"
      ]
    });

    // Raccourci vers les audits Lighthouse
    const audits = lhr.audits;

    /**
     * Construction d'une réponse claire et stable
     * Cette structure est idéale pour :
     * - ton backend Spring Boot
     * - le front
     * - la génération de règles humaines
     * - le PDF
     */
    const result = {
      url: lhr.finalUrl,

      // Scores globaux (0 → 100)
      scores: {
        performance: Math.round(lhr.categories.performance.score * 100),
        seo: Math.round(lhr.categories.seo.score * 100),
        accessibility: Math.round(lhr.categories.accessibility.score * 100),
        bestPractices: Math.round(
          lhr.categories["best-practices"].score * 100
        )
      },

      // Web Vitals et métriques clés (en millisecondes sauf CLS)
      vitals: {
        // First Contentful Paint
        fcp: audits["first-contentful-paint"].numericValue,

        // Largest Contentful Paint (critique SEO / UX)
        lcp: audits["largest-contentful-paint"].numericValue,

        // Cumulative Layout Shift (stabilité visuelle)
        cls: audits["cumulative-layout-shift"].numericValue,

        // Total Blocking Time (JS bloquant)
        tbt: audits["total-blocking-time"].numericValue,

        // Speed Index (vitesse perçue)
        speedIndex: audits["speed-index"].numericValue,

        // Time To Interactive
        tti: audits["interactive"].numericValue
      }
    };

    console.log("Audit terminé avec succès");
    return result;

  } catch (error) {
    console.error("Erreur lors de l'audit Lighthouse", error);
    throw error;
  } finally {
    // Toujours fermer le navigateur
    await browser.close();
  }
}
