# Neshtek Static Website (neshtek.com)

This is a static website starter for neshtek.com with the pages:
- Home (`index.html`)
- Services (`services.html`)
- WebCenter (`webcenter.html`)
- Resources (`resources.html`) + sample blog posts in `/blog`
- About (`about.html`)
- Contact (`contact.html`)
- Privacy (`privacy.html`)
- Terms (`terms.html`)

## Quick preview (local)
Just open `index.html` in your browser.

## Deploy to GitHub Pages
1. Create a GitHub repository (example: `neshtek-website`)
2. Upload all files/folders from this zip to the repo root
3. Go to **Settings → Pages**
   - Source: `main` branch
   - Folder: `/ (root)`
4. Add Custom Domain: `neshtek.com`
5. In BigRock DNS:
   - Apex `neshtek.com`: add A records to GitHub Pages IPs (shown in GitHub Pages settings/docs)
   - `www`: CNAME to `<your-github-username>.github.io`
6. After DNS propagates, enable **Enforce HTTPS**

## Contact form
The Contact page uses Formspree placeholder:
- Replace `https://formspree.io/f/YOUR_FORM_ID` with your real form endpoint.

## Calendly
Replace the placeholder with your Calendly iframe embed in `contact.html` under the “Book a call” section.


## Logo
This package uses your provided logo at `assets/img/neshtek-logo.png` and favicon `assets/img/favicon-32.png`.
