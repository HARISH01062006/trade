# TradeScore - Progressive Web App (PWA)

A mobile-first, soft neumorphic Progressive Web App designed for traders to evaluate setups before entering trades.

## Files Included
- `index.html`: Complete PWA web application with responsive soft neumorphic UI, calibrated SVG arc gauge, live checklist calculator, and local storage persistence.
- `manifest.json`: Web App Manifest enabling "Add to Home Screen" on iOS, Android, and Desktop Chrome/Edge.
- `sw.js`: Service Worker for offline capability and instant caching.
- `icon-192.svg` & `icon-512.svg`: App icons for mobile home screen installation.

## How to Run or Deploy as a Website
1. **Instant Static Hosting**:
   - Upload this folder to **GitHub Pages**, **Vercel**, **Netlify**, or **Cloudflare Pages**.
2. **Local Preview**:
   - Open `index.html` in any browser, or run `npx serve pwa-website` or `python3 -m http.server`.
3. **PWA Installation**:
   - On iOS Safari: Tap Share -> **Add to Home Screen**.
   - On Chrome / Edge / Android: Tap the **Install** prompt or browser menu -> **Install App**.
