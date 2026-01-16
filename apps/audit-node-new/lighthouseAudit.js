// lighthouseMVP.js
import puppeteer from 'puppeteer';
import lighthouse from 'lighthouse';
import PDFDocument from 'pdfkit';
import { createWriteStream } from 'fs';
import { URL } from 'url';

const RECOMMENDATIONS = {
  performance: "Optimiser les images, minifier le JS/CSS et réduire les scripts bloquants pour améliorer la performance.",
  accessibility: "Vérifier les contrastes, labels de formulaires et navigation clavier pour améliorer l'accessibilité.",
  seo: "Optimiser les métadonnées, balises alt et structure des titres pour améliorer le SEO.",
  'best-practices': "Suivre les meilleures pratiques web : HTTPS, pas de JS obsolète, headers sécurisés.",
  pwa: "Ajouter un manifest.json et un service worker pour transformer le site en PWA."
};

async function runAudit(url) {
  if (!url) {
    console.error("Erreur : vous devez fournir une URL en argument.");
    console.error("Exemple : node lighthouseMVP.js https://example.com");
    return;
  }

  console.log(`🔍 Lancement de l'audit pour : ${url}`);

  const browser = await puppeteer.launch({ headless: true });
  const port = new URL(browser.wsEndpoint()).port;

  try {
    const result = await lighthouse(url, { port, output: 'json', logLevel: 'info' });
    const lhr = result.lhr;

    if (!lhr) {
      console.error("Erreur : Lighthouse n'a pas renvoyé de résultats valides !");
      return;
    }

    // Récupérer les scores et les recommandations si < 90%
    const scores = {};
    const recommendations = {};

    for (const category of ['performance', 'accessibility', 'seo', 'best-practices', 'pwa']) {
      const score = lhr.categories[category]?.score ?? 0;
      scores[category] = Math.round(score * 100);
      if (scores[category] < 90) {
        recommendations[category] = RECOMMENDATIONS[category];
      }
    }

    // Générer le PDF
    const pdfPath = `audit_${Date.now()}.pdf`;
    const doc = new PDFDocument({ margin: 50 });
    doc.pipe(createWriteStream(pdfPath));

    doc.fontSize(20).text(`Audit Lighthouse`, { underline: true });
    doc.moveDown();
    doc.fontSize(14).text(`URL auditée : ${url}`);
    doc.moveDown();

    for (const [category, score] of Object.entries(scores)) {
      doc.fontSize(14).text(`${category.toUpperCase()}: ${score}%`);
      if (recommendations[category]) {
        doc.fontSize(12).fillColor('red').text(`Recommandation: ${recommendations[category]}`);
      }
      doc.moveDown();
      doc.fillColor('black'); // reset color
    }

    doc.end();
    console.log(`✅ Audit terminé ! PDF généré : ${pdfPath}`);

  } catch (err) {
    console.error("Erreur pendant l'audit :", err);
  } finally {
    await browser.close();
  }
}

// Exécuter le script avec l'URL passée en argument
const urlArg = process.argv[2];
runAudit(urlArg);
