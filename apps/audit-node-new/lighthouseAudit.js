import lighthouse from "lighthouse";
import puppeteer from "puppeteer";
import { URL } from "url";

export function validateUrl(input) {
  try {
    const url = new URL(input);

    if (!["http:", "https:"].includes(url.protocol)) {
      throw new Error();
    }

    return url.toString();
  } catch {
    throw new Error("Invalid URL");
  }
}

export async function runAudit(targetUrl) {
  const browser = await puppeteer.launch({
    args: ["--no-sandbox", "--disable-setuid-sandbox"],
    headless: true
  });

  try {
    const { lhr } = await lighthouse(targetUrl, {
      port: new URL(browser.wsEndpoint()).port,
      onlyCategories: ["performance", "accessibility", "seo"]
    });

    return {
      performance: lhr.categories.performance.score,
      accessibility: lhr.categories.accessibility.score,
      seo: lhr.categories.seo.score
    };
  } finally {
    await browser.close();
  }
}
