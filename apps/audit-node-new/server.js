import express from "express";
import { validateUrl, runAudit } from "./lighthouseAudit.js";

const app = express();
app.use(express.json());

app.post("/audit", async (req, res) => {
console.log(`req : ${req}`);
console.log(`res : ${res}`);
console.log(`req body : ${req.body}`);


  const { url } = req.body;

  console.log(`url : ${url}`);


  if (!url) {
    return res.status(400).json({ error: "url is required" });
  }

  try {
    const validatedUrl = validateUrl(url);
    const scores = await runAudit(validatedUrl);

    res.json({
      url: validatedUrl,
      scores
    });

  } catch (err) {
    res.status(400).json({
      error: err.message
    });
  }
});

const PORT = 3001;
app.listen(PORT, () => {
  console.log(`Audit HTTP server running on port ${PORT}`);
});
