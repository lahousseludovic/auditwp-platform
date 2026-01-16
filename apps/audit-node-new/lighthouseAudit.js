// lighthouseAudit.js
import puppeteer from 'puppeteer';
import lighthouse from 'lighthouse';
import PDFDocument from 'pdfkit';
import { createWriteStream } from 'fs';
import { URL } from 'url';

async function runAudit(url) {
  console.log(`Lancement de l'audit pour : ${url}`);

  // 1️⃣ Lancer Puppeteer
  const browser = await puppeteer.launch({ headless: true });
  const port = new URL(browser.wsEndpoint()).port;

  try {
    // 2️⃣ Lancer Lighthouse
    const result = await lighthouse(url, {
      port,
      output: 'json',
      logLevel: 'info'
    });

    const lhr = result.lhr;
    if (!lhr) {
      console.error("Lighthouse n'a pas renvoyé de résultats valides !");
      return;
    }

    // 3️⃣ Récupérer les scores
    const scores = {
      performance: lhr.categories.performance?.score ?? 0,
      accessibility: lhr.categories.accessibility?.score ?? 0,
      seo: lhr.categories.seo?.score ?? 0,
      'best-practices': lhr.categories['best-practices']?.score ?? 0,
      pwa: lhr.categories.pwa?.score ?? 0
    };

    console.log("Scores Lighthouse :", scores);

    // 4️⃣ Générer le PDF
    const doc = new PDFDocument();
    const pdfPath = 'audit.pdf';
    doc.pipe(createWriteStream(pdfPath));

    doc.fontSize(20).text(`Audit Lighthouse pour : ${url}`, { underline: true });
    doc.moveDown();

    for (const [category, score] of Object.entries(scores)) {
      doc.fontSize(14).text(`${category}: ${score * 100}%`);
    }

    doc.end();
    console.log(`PDF généré : ${pdfPath}`);

  } catch (err) {
    console.error("Erreur pendant l'audit :", err);
  } finally {
    await browser.close();
  }
}

// Exécuter le script sur une URL exemple
const url = process.argv[2] || 'https://example.com';
runAudit(url);
